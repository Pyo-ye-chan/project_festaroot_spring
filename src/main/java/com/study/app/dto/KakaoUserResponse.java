package com.study.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoUserResponse {
    private Long id;
    @JsonProperty("connected_at")
    private String connectedAt;
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConnectedAt() {
        return connectedAt;
    }

    public void setConnectedAt(String connectedAt) {
        this.connectedAt = connectedAt;
    }

    public KakaoAccount getKakaoAccount() {
        return kakaoAccount;
    }

    public void setKakaoAccount(KakaoAccount kakaoAccount) {
        this.kakaoAccount = kakaoAccount;
    }

    public static class KakaoAccount {
        @JsonProperty("profile_nickname_needs_agreement")
        private Boolean profileNicknameNeedsAgreement;
        private Profile profile;
        @JsonProperty("has_email")
        private Boolean hasEmail;
        @JsonProperty("email_needs_agreement")
        private Boolean emailNeedsAgreement;
        @JsonProperty("is_email_valid")
        private Boolean isEmailValid;
        @JsonProperty("is_email_verified")
        private Boolean isEmailVerified;
        private String email;

        // Getters and Setters
        public Boolean getProfileNicknameNeedsAgreement() {
            return profileNicknameNeedsAgreement;
        }

        public void setProfileNicknameNeedsAgreement(Boolean profileNicknameNeedsAgreement) {
            this.profileNicknameNeedsAgreement = profileNicknameNeedsAgreement;
        }

        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }

        public Boolean getHasEmail() {
            return hasEmail;
        }

        public void setHasEmail(Boolean hasEmail) {
            this.hasEmail = hasEmail;
        }

        public Boolean getEmailNeedsAgreement() {
            return emailNeedsAgreement;
        }

        public void setEmailNeedsAgreement(Boolean emailNeedsAgreement) {
            this.emailNeedsAgreement = emailNeedsAgreement;
        }

        public Boolean getIsEmailValid() {
            return isEmailValid;
        }

        public void setIsEmailValid(Boolean isEmailValid) {
            this.isEmailValid = isEmailValid;
        }

        public Boolean getIsEmailVerified() {
            return isEmailVerified;
        }

        public void setIsEmailVerified(Boolean isEmailVerified) {
            this.isEmailVerified = isEmailVerified;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class Profile {
        private String nickname;
        @JsonProperty("thumbnail_image_url")
        private String thumbnailImageUrl;
        @JsonProperty("profile_image_url")
        private String profileImageUrl;

        // Getters and Setters
        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getThumbnailImageUrl() {
            return thumbnailImageUrl;
        }

        public void setThumbnailImageUrl(String thumbnailImageUrl) {
            this.thumbnailImageUrl = thumbnailImageUrl;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }
    }
}
