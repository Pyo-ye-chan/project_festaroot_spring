package com.study.app.domains.inquiry;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.study.app.domains.inquiry.dto.InquiryAttachmentDTO;
import com.study.app.domains.inquiry.dto.InquiryDTO;

@Mapper
public interface InquiryDAO {
    
    /**
     * 문의글 본문 저장 (User_inquiry 테이블)
     */
    int insertInquiry(InquiryDTO dto);
    
    /**
     * 첨부파일 정보 저장 (Inquiry_attachment 테이블)
     */
    int insertAttachments(List<InquiryAttachmentDTO> list);

    /**
     * 문의글 단건 조회
     */
    InquiryDTO selectInquiryById(Long inquiry_id);

    /**
     * 특정 문의글의 모든 첨부파일 조회
     */
    List<InquiryAttachmentDTO> selectAttachmentsByInquiryId(Long inquiry_id);

    /**
     * 특정 문의글의 첨부파일 DB 정보 삭제
     */
    int deleteAttachmentsByInquiryId(Long inquiry_id);

    /**
     * 문의글 삭제
     */
    int deleteInquiry(Long inquiry_id);

    /**
     * 문의글 수정
     */
    int updateInquiry(InquiryDTO dto);

    /**
     * 본인의 문의 내역 리스트 조회
     */
    List<InquiryDTO> getMyInquiryList(String member_id);
    
    /**
     * 문의 상세 조회
     */
    InquiryDTO inquiryDetail(Long inquiry_id);
}
