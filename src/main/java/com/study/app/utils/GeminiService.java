package com.study.app.utils;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Gemini API와 통신하여 텍스트 생성 및 데이터 분석을 수행하는 서비스
 */
@Service
public class GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);

    @Value("${spring.ai.google.genai.api-key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.embedding.url}")
    private String embeddingUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GeminiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Gemini 3.1 Flash Lite 모델을 사용하여 텍스트 생성을 수행합니다.
     */
    public String getCompletion(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = Map.of(
                "model", "models/gemini-3.1-flash-lite", // 사용자 지정 모델명 그대로 적용
                "contents", List.of(
                    Map.of("parts", List.of(
                        Map.of("text", prompt)
                    ))
                ),
                "generationConfig", Map.of(
                    "response_mime_type", "application/json"
                )
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            log.info("Gemini API 호출 중 (gemini-3.1-flash-lite)...");

            String urlWithKey = apiUrl + "?key=" + apiKey;
            String response = restTemplate.postForObject(urlWithKey, entity, String.class);

            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseMap.get("candidates");
            
            if (candidates == null || candidates.isEmpty()) {
                log.error("Gemini 응답에 candidates가 없습니다: {}", response);
                return null;
            }

            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            
            return (String) parts.get(0).get("text");

        } catch (Exception e) {
            log.error("Gemini API 호출 실패: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Gemini Embedding 1 모델을 사용하여 텍스트를 벡터로 변환합니다.
     */
    public List<Double> getEmbedding(String text) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = Map.of(
                "model", "models/text-embedding-004", // 사용자 지정 모델명 그대로 적용
                "content", Map.of("parts", List.of(Map.of("text", text)))
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            log.info("Gemini Embedding API 호출 중 (gemini-embedding-1)...");

            String urlWithKey = embeddingUrl + "?key=" + apiKey;
            Map<String, Object> response = restTemplate.postForObject(urlWithKey, entity, Map.class);

            Map<String, Object> embedding = (Map<String, Object>) response.get("embedding");
            if (embedding == null) {
                log.error("임베딩 응답에 데이터가 없습니다: {}", response);
                return null;
            }

            List<Double> values = (List<Double>) embedding.get("values");
            log.info("임베딩 벡터 추출 완료 (크기: {})", values.size());
            
            return values;

        } catch (Exception e) {
            log.error("Gemini Embedding 호출 실패: {}", e.getMessage());
            return null;
        }
    }
}
