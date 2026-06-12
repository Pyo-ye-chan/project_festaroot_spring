package com.study.app.domains.review;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.study.app.domains.review.dto.FestivalReviewDTO;
import com.study.app.domains.review.dto.ReviewReportDTO;
import com.study.app.utils.JWTUtil;

@RestController
@RequestMapping("/review")
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private JWTUtil jwt;
	
    private String getMemberId(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }

        String token = authorization.substring(7);
        return jwt.getSubject(token);
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<?> getReviews(
            @PathVariable Long contentId,
            @RequestParam(defaultValue = "최신순") String sortType) {
    	System.out.println("where");

        HashMap<String, Object> result = new HashMap<>();

        List<FestivalReviewDTO> reviews =
                reviewService.getReviews(contentId, sortType);

        result.put("success", true);
        result.put("message", "후기 목록 조회 성공");
        result.put("list", reviews);
        result.put("totalCount", reviews.size());
        System.out.println("result:" + result);

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> addReview(
            @RequestPart("review") FestivalReviewDTO reviewDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestHeader(value = "Authorization", required = false) String authorization) {

        HashMap<String, Object> result = new HashMap<>();

        try {
            String memberId = getMemberId(authorization);

            if (memberId == null) {
                result.put("success", false);
                result.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(401).body(result);
            }

            reviewDTO.setMember_id(memberId);

            int insertResult =
                    reviewService.addReview(reviewDTO, images);

            result.put("success", insertResult > 0);
            result.put("message", "후기 등록 완료");
            result.put("review_id", reviewDTO.getReview_id());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();

            result.put("success", false);
            result.put("message", "후기 등록 중 오류 발생");

            return ResponseEntity.status(500).body(result);
        }
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @RequestPart("review") FestivalReviewDTO reviewDTO,
            @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
            @RequestPart(value = "existingImageUrlsToKeep", required = false) List<String> existingImageUrlsToKeep,
            @RequestHeader(value = "Authorization", required = false) String authorization) {

        HashMap<String, Object> result = new HashMap<>();

        try {
            String memberId = getMemberId(authorization);

            if (memberId == null) {
                result.put("success", false);
                result.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(401).body(result);
            }

            reviewDTO.setReview_id(reviewId);
            reviewDTO.setMember_id(memberId);

            int updateResult =
                    reviewService.updateReview(
                            reviewDTO,
                            newImages,
                            existingImageUrlsToKeep
                    );

            if (updateResult == 0) {
                result.put("success", false);
                result.put("message", "수정 권한이 없거나 존재하지 않는 후기입니다.");
                return ResponseEntity.status(403).body(result);
            }

            result.put("success", true);
            result.put("message", "후기 수정 완료");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();

            result.put("success", false);
            result.put("message", "후기 수정 중 오류 발생");

            return ResponseEntity.status(500).body(result);
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader(value = "Authorization", required = false) String authorization) {

        HashMap<String, Object> result = new HashMap<>();

        try {
            String memberId = getMemberId(authorization);

            if (memberId == null) {
                result.put("success", false);
                result.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(401).body(result);
            }

            int deleteResult =
                    reviewService.deleteReview(reviewId, memberId);

            if (deleteResult == 0) {
                result.put("success", false);
                result.put("message", "삭제 권한이 없거나 존재하지 않는 후기입니다.");
                return ResponseEntity.status(403).body(result);
            }

            result.put("success", true);
            result.put("message", "후기 삭제 완료");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();

            result.put("success", false);
            result.put("message", "후기 삭제 중 오류 발생");

            return ResponseEntity.status(500).body(result);
        }
    }

    @PostMapping("/{reviewId}/report")
    public ResponseEntity<?> reportReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewReportDTO reportDTO,
            @RequestHeader(value = "Authorization", required = false) String authorization) {

        HashMap<String, Object> result = new HashMap<>();

        try {
            String memberId = getMemberId(authorization);

            if (memberId == null) {
                result.put("success", false);
                result.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(401).body(result);
            }

            reportDTO.setReview_id(reviewId);
            reportDTO.setMember_id(memberId);

            String reportResult =
                    reviewService.reportReview(reportDTO);

            if ("DUPLICATE".equals(reportResult)) {
                result.put("success", false);
                result.put("message", "이미 신고한 후기입니다.");
                return ResponseEntity.badRequest().body(result);
            }

            result.put("success", true);
            result.put("message", "신고가 접수되었습니다.");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();

            result.put("success", false);
            result.put("message", "신고 처리 중 오류 발생");

            return ResponseEntity.status(500).body(result);
        }
    }
	
	

}
