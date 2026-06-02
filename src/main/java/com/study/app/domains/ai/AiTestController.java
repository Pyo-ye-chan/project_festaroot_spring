package com.study.app.domains.ai;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.app.domains.festival.ThemeMappingService;
import com.study.app.domains.festival.VectorIndexingService;

@RestController
public class AiTestController {

    @Autowired
    private ThemeMappingService themeMappingService;

    @Autowired
    private VectorIndexingService vectorIndexingService;

    /**
     * 테마가 없는 축제들에 대해 AI 테마 매핑 프로세스를 시작합니다.
     */
    @GetMapping("/api/ai/run-theme-mapping")
    public Map<String, Object> runThemeMapping() {
        try {
            themeMappingService.mapThemesForAllFestivals();
            return Map.of(
                "status", "success",
                "message", "테마 매핑 프로세스가 완료되었습니다. 로그를 확인하세요."
            );
        } catch (Exception e) {
            return Map.of(
                "status", "error",
                "message", "프로세스 실행 중 오류 발생: " + e.getMessage()
            );
        }
    }

    /**
     * 모든 축제 데이터를 벡터화하여 Qdrant에 저장합니다.
     */
    @GetMapping("/api/ai/run-vector-indexing")
    public Map<String, Object> runVectorIndexing() {
        try {
            vectorIndexingService.indexAllFestivals();
            return Map.of(
                "status", "success",
                "message", "벡터 인덱싱 프로세스가 완료되었습니다. Qdrant를 확인하세요."
            );
        } catch (Exception e) {
            return Map.of(
                "status", "error",
                "message", "인덱싱 중 오류 발생: " + e.getMessage()
            );
        }
    }

    @GetMapping("/api/ai/test-status")
    public String testStatus() {
        return "AI Test Controller is active. Use /api/ai/run-theme-mapping to start mapping.";
    }
}
