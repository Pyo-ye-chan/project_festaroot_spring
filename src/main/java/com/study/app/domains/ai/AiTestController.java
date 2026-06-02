//package com.study.app.domains.ai;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.ai.vectorstore.VectorStore;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.study.app.domains.theme.ThemeMasterDTO;
//import com.study.app.domains.theme.ThemeMaterService;
//
//@RestController
//public class AiTestController {
//
//    private final ChatModel chatModel;
//    private final VectorStore vectorStore;
//    private final ThemeMaterService themeService;
//    private final ObjectMapper objectMapper;
//
//    @Autowired
//    public AiTestController(ChatModel chatModel, VectorStore vectorStore, ThemeMaterService themeService, ObjectMapper objectMapper) {
//        this.chatModel = chatModel;
//        this.vectorStore = vectorStore;
//        this.themeService = themeService;
//        this.objectMapper = objectMapper;
//    }
//
//    /**
//     * 축제 소개글을 바탕으로 DB의 테마 마스터 목록에서 적절한 테마를 추출하는 테스트
//     */
//    @GetMapping("/api/ai/test-theme-extraction")
//    public Map<String, Object> testThemeExtraction(
//            @RequestParam(defaultValue = "인천 송도에서 펼쳐지는 화려한 불꽃과 음악의 향연, 기술과 문화가 결합된 야간 축제입니다.") String overview) {
//        
//        // 1. DB에서 전체 테마 목록 가져오기 (THEME_MASTER 테이블)
//        List<ThemeMasterDTO> themeList = themeService.getThemeList();
//        
//        // AI에게 줄 선택지 목록 (이름들만 콤마로 연결)
//        String themeNames = themeList.stream()
//                .map(ThemeMasterDTO::getTheme_name)
//                .collect(Collectors.joining(", "));
//
//        // 2. 프롬프트 구성
//        String prompt = String.format(
//            "너는 축제 분류 전문가야. 아래의 [축제 소개글]을 읽고, [테마 목록] 중에서 가장 잘 어울리는 항목을 최대 3개만 골라줘.\n\n" +
//            "[테마 목록]: %s\n\n" +
//            "[축제 소개글]: %s\n\n" +
//            "반드시 [테마 목록]에 있는 단어만 사용하고, 결과는 오직 JSON 형식으로만 보내줘.\n" +
//            "형식: {\"themes\": [\"테마명1\", \"테마명2\"]}",
//            themeNames, overview
//        );
//
//        // 3. AI 호출 (OpenAI)
//        String response = chatModel.call(prompt);
//        
//        try {
//            // AI 응답에서 마크다운 코드 블록 제거 (가끔 포함될 수 있음)
//            String jsonContent = response.replaceAll("```json|```", "").trim();
//            
//            // 4. 결과 파싱 (JSON -> Map)
//            Map<String, List<String>> parsedResponse = objectMapper.readValue(jsonContent, new TypeReference<>() {});
//            List<String> pickedThemeNames = parsedResponse.get("themes");
//
//            // 5. 추출된 테마 명칭을 다시 테마 코드로 변환 (매핑 작업)
//            List<Map<String, String>> resultThemes = new ArrayList<>();
//            for (String name : pickedThemeNames) {
//                themeList.stream()
//                        .filter(t -> t.getTheme_name().equals(name.trim()))
//                        .findFirst()
//                        .ifPresent(t -> {
//                            resultThemes.add(Map.of(
//                                "theme_code", t.getTheme_code(),
//                                "theme_name", t.getTheme_name()
//                            ));
//                        });
//            }
//
//            return Map.of(
//                "status", "success",
//                "inputOverview", overview,
//                "extractedThemes", resultThemes,
//                "aiRawResponse", response
//            );
//            
//        } catch (Exception e) {
//            return Map.of(
//                "status", "error",
//                "message", "결과 파싱 중 오류가 발생했습니다.",
//                "aiRawResponse", response,
//                "errorDetail", e.getMessage()
//            );
//        }
//    }
//
//    @GetMapping("/api/ai/test-process")
//    public String runTestProcess() {
//        return "기본 테스트 페이지입니다. 테마 추출 테스트는 /api/ai/test-theme-extraction?overview=내용 을 이용해주세요.";
//    }
//}
