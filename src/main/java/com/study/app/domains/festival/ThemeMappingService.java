package com.study.app.domains.festival;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.app.domains.festival.dto.FestivalDTO;
import com.study.app.domains.theme.ThemeMasterDTO;
import com.study.app.domains.theme.ThemeMasterDAO;
import com.study.app.utils.GeminiService;

@Service
public class ThemeMappingService {

    private static final Logger log = LoggerFactory.getLogger(ThemeMappingService.class);

    @Autowired private FestivalDAO festivalDAO;
    @Autowired private ThemeMasterDAO themeMasterDAO;
    @Autowired private GeminiService geminiService;
    @Autowired private ObjectMapper objectMapper;

    /**
     * 테마가 없는 모든 축제를 대상으로 Gemini를 호출하여 테마를 매핑합니다.
     */
    @Transactional
    public void mapThemesForAllFestivals() {
        // 1. DB에서 전체 테마 목록 가져오기 (AI에게 선택지를 주기 위함)
        List<ThemeMasterDTO> allThemes = themeMasterDAO.selectAllTheme();
        String themesJson = allThemes.stream()
                .map(t -> String.format("{\"code\":\"%s\", \"name\":\"%s\"}", t.getTheme_code(), t.getTheme_name()))
                .collect(Collectors.joining(", ", "[", "]"));

        // 2. 테마가 없는 축제 목록 가져오기
        List<FestivalDTO> targetFestivals = festivalDAO.getFestivalsWithoutTheme();
        log.info("테마 매핑 대상 축제 수: {}건", targetFestivals.size());

        for (FestivalDTO festival : targetFestivals) {
            try {
                // 3. 프롬프트 작성
                String prompt = String.format(
                    "너는 대한민국 관광/축제 전문가야. 아래 [축제 정보]를 읽고, 제공된 [테마 리스트] 중에서 가장 적합한 테마 코드를 1개에서 최대 3개까지 골라줘.\n\n" +
                    "[테마 리스트]: %s\n\n" +
                    "[축제 이름]: %s\n" +
                    "[축제 설명]: %s\n\n" +
                    "반드시 제공된 [테마 리스트]의 코드로만 응답하고, 결과는 오직 아래 JSON 형식으로만 보내줘.\n" +
                    "형식: {\"theme_codes\": [\"코드1\", \"코드2\"]}",
                    themesJson, festival.getTitle(), festival.getOverview()
                );

                // 4. Gemini 호출
                String aiResponse = geminiService.getCompletion(prompt);
                if (aiResponse == null) continue;

                // AI 응답에서 마크다운 코드 블록 제거
                String jsonContent = aiResponse.replaceAll("```json|```", "").trim();

                // 5. JSON 파싱 및 DB 저장
                Map<String, List<String>> result = objectMapper.readValue(jsonContent, new TypeReference<>() {});
                List<String> themeCodes = result.get("theme_codes");

                if (themeCodes != null) {
                    for (String code : themeCodes) {
                        festivalDAO.insertFestivalThemeMapping(festival.getContent_id(), code);
                    }
                    log.info("축제 '{}' (ID: {}) 테마 매핑 완료: {}", festival.getTitle(), festival.getContent_id(), themeCodes);
                }

                // API 호출 제한(Rate Limit)을 방지하기 위해 아주 잠깐 쉬어줌
                Thread.sleep(500); 

            } catch (Exception e) {
                log.error("축제 {} 매핑 중 오류 발생: {}", festival.getContent_id(), e.getMessage());
            }
        }
    }
}
