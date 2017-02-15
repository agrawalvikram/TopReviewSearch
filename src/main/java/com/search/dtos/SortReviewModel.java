package com.search.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jitendra.k on 14/02/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortReviewModel {

    private Long docId;
    private Double score;
    private Double relevantScore;
}
