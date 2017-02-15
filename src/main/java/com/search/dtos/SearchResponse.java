package com.search.dtos;

import com.search.entity.IReview;
import lombok.*;

/**
 * Created by jitendra.k on 14/02/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private IReview review;
    private Double score;
}
