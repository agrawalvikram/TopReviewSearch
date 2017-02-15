package com.search.dtos;

import com.search.entity.IReview;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jitendra.k on 14/02/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review implements IReview {

    private Long id;
    private String productId;
    private String userId;
    private String profileName;
    private String helpfulness;
    private Double score;
    private Long time;
    private String summary;
    private String text;

}
