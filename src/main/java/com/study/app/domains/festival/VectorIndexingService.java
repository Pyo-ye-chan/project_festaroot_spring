package com.study.app.domains.festival;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.study.app.utils.GeminiService;

/**
 * 축제 데이터를 벡터화하여 Qdrant에 저장하는 서비스
 * 증분 업데이트 로직 포함: modified_time이 갱신된 데이터만 처리
 */
@Service
public class VectorIndexingService {

    private static final Logger log = LoggerFactory.getLogger(VectorIndexingService.class);

    @Autowired private FestivalDAO festivalDAO;
    @Autowired private GeminiService geminiService;
    @Autowired private RestTemplate restTemplate;

    @Value("${spring.ai.vectorstore.qdrant.host}")
    private String qdrantHost;

    @Value("${spring.ai.vectorstore.qdrant.port}")
    private int qdrantPort;

    @Value("${spring.ai.vectorstore.qdrant.collection-name}")
    private String collectionName;

    /**
     * 새로운 정보나 수정된 축제 데이터를 벡터화하여 Qdrant에 저장합니다.
     */
    public void indexAllFestivals() {
        // 인덱싱이 필요한 데이터만 가져옴 (modified_time > indexed_modified_time)
        List<Map<String, Object>> festivals = new ArrayList<>(festivalDAO.getFestivalsToIndex());
        log.info("인덱싱 대상 축제 수: {}건", festivals.size());

        if (festivals.isEmpty()) {
            log.info("새로 인덱싱할 데이터가 없습니다.");
            return;
        }

        int successCount = 0;
        for (Map<String, Object> f : festivals) {
            try {
                Long contentId = Long.parseLong(String.valueOf(f.get("CONTENT_ID")));
                String modifiedTime = convertToString(f.get("MODIFIED_TIME"));
                String title = convertToString(f.get("TITLE"));
                String overview = convertToString(f.get("OVERVIEW"));
                String themes = convertToString(f.get("THEMES"));
                String region = convertToString(f.get("REGION_CODE"));

                // 1. 임베딩용 텍스트 합성
                String combinedText = String.format(
                    "축제명: %s | 지역코드: %s | 테마: %s | 설명: %s",
                    title, region, (themes != null && !themes.isEmpty() ? themes : "없음"), overview
                );

                // 2. Gemini를 통한 임베딩 생성
                List<Double> vector = geminiService.getEmbedding(combinedText);
                if (vector == null) {
                    log.error("축제 '{}' 임베딩 생성 실패", title);
                    continue;
                }

                // 3. Qdrant 저장 (Upsert)
                boolean success = upsertToQdrant(String.valueOf(contentId), vector, Map.of(
                    "content_id", String.valueOf(contentId),
                    "title", title,
                    "region_code", region,
                    "themes", themes
                ));

                if (success) {
                    // 4. DB에 인덱싱 완료 상태 기록 (INDEXED_MODIFIED_TIME 업데이트)
                    festivalDAO.updateIndexedModifiedTime(contentId, modifiedTime);
                    
                    successCount++;
                    log.info("[{}/{}] 축제 '{}' 인덱싱 및 DB 상태 업데이트 완료", successCount, festivals.size(), title);
                }

                Thread.sleep(300);

            } catch (Exception e) {
                log.error("축제 인덱싱 중 예외 발생: {}", e.getMessage());
            }
        }
        log.info("인덱싱 프로세스 완료! 총 {}건 중 {}건 성공", festivals.size(), successCount);
    }

    /**
     * Qdrant REST API를 호출하여 벡터와 페이로드를 저장합니다.
     */
    private boolean upsertToQdrant(String id, List<Double> vector, Map<String, Object> payload) {
        try {
            String url = String.format("http://%s:6333/collections/%s/points?wait=true", qdrantHost, collectionName);
            
            long numericId;
            try {
                numericId = Long.parseLong(id);
            } catch (NumberFormatException e) {
                numericId = Math.abs(id.hashCode());
            }

            Map<String, Object> point = Map.of(
                "id", numericId,
                "vector", vector,
                "payload", payload
            );
            
            Map<String, Object> requestBody = Map.of("points", List.of(point));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
            
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Qdrant 통신 실패: {}", e.getMessage());
            return false;
        }
    }

    private String convertToString(Object obj) {
        if (obj == null) return "";
        if (obj instanceof Clob) {
            try {
                Clob clob = (Clob) obj;
                return clob.getSubString(1, (int) clob.length());
            } catch (Exception e) { return ""; }
        }
        return String.valueOf(obj);
    }
}
