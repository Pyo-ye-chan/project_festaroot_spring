package com.study.app.domains.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.study.app.domains.review.dto.FestivalReviewDTO;
import com.study.app.domains.review.dto.FestivalReviewImageDTO;
import com.study.app.domains.review.dto.ReviewReportDTO;
import com.study.app.domains.storage.UploadService;

@Service
public class ReviewService {

	@Autowired
	private ReviewDAO reviewDAO;
	
	@Autowired
	private UploadService uploadService;

    public List<FestivalReviewDTO> getReviews(Long content_id, String sortType) {

        List<FestivalReviewDTO> reviews =
                reviewDAO.selectReviews(content_id, sortType);

        for (FestivalReviewDTO review : reviews) {
            List<FestivalReviewImageDTO> images =
                    reviewDAO.selectReviewImages(review.getReview_id());

            review.setImages(images);
        }

        return reviews;
    }

    @Transactional
    public int addReview(FestivalReviewDTO reviewDTO, List<MultipartFile> images) throws Exception {

        int result = reviewDAO.insertReview(reviewDTO);

        if (images != null) {
            for (MultipartFile image : images) {
                if (image != null && !image.isEmpty()) {

                    String imageUrl =
                            uploadService.upload(image, "reviews");

                    FestivalReviewImageDTO imageDTO =
                            new FestivalReviewImageDTO();

                    imageDTO.setReview_id(reviewDTO.getReview_id());
                    imageDTO.setImage_url(imageUrl);

                    reviewDAO.insertReviewImage(imageDTO);
                }
            }
        }

        return result;
    }

    @Transactional
    public int updateReview(
            FestivalReviewDTO reviewDTO,
            List<MultipartFile> newImages,
            List<String> existingImageUrlsToKeep) throws Exception {

        int result = reviewDAO.updateReview(reviewDTO);

        if (result == 0) {
            return 0;
        }

        reviewDAO.deleteReviewImages(reviewDTO.getReview_id());

        if (existingImageUrlsToKeep != null) {
            for (String imageUrl : existingImageUrlsToKeep) {
                if (imageUrl != null && !imageUrl.trim().isEmpty()) {

                    FestivalReviewImageDTO imageDTO =
                            new FestivalReviewImageDTO();

                    imageDTO.setReview_id(reviewDTO.getReview_id());
                    imageDTO.setImage_url(imageUrl);

                    reviewDAO.insertReviewImage(imageDTO);
                }
            }
        }

        if (newImages != null) {
            for (MultipartFile image : newImages) {
                if (image != null && !image.isEmpty()) {

                    String imageUrl =
                            uploadService.upload(image, "reviews");

                    FestivalReviewImageDTO imageDTO =
                            new FestivalReviewImageDTO();

                    imageDTO.setReview_id(reviewDTO.getReview_id());
                    imageDTO.setImage_url(imageUrl);

                    reviewDAO.insertReviewImage(imageDTO);
                }
            }
        }

        return result;
    }

    @Transactional
    public int deleteReview(Long review_id, String member_id) {
        return reviewDAO.deleteReview(review_id, member_id);
    }

    @Transactional
    public String reportReview(ReviewReportDTO reportDTO) {

        int count =
                reviewDAO.countReportByMember(
                        reportDTO.getReview_id(),
                        reportDTO.getMember_id()
                );

        if (count > 0) {
            return "DUPLICATE";
        }

        reviewDAO.insertReport(reportDTO);
        reviewDAO.increaseReportCount(reportDTO.getReview_id());

        return "SUCCESS";
    }

}
