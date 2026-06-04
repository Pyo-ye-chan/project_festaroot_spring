package com.study.app.domains.ai;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/recommendations")
    public List<Map<String, Object>> getRecommendations(@RequestAttribute("id") String memberId) {
        return recommendationService.getPersonalizedRecommendations(memberId);
    }
}
