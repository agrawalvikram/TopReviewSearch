package com.search.entity;

/**
 * Created by jitendra.k on 14/02/17.
 */
public interface IReview {

    Long getId();
    void setId(Long id);
    String getProductId();
    void setProductId(String productId);
    String getUserId();
    void setUserId(String userId) ;
    String getProfileName() ;
    void setProfileName(String profileName);
    String getHelpfulness();
    void setHelpfulness(String helpfulness);
    Double getScore();
    void setScore(Double score);
    Long getTime();
    void setTime(Long time);
    String getSummary();
    void setSummary(String summary);
    String getText();
    void setText(String text);
}
