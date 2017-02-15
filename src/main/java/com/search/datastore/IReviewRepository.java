package com.search.datastore;

import com.search.dtos.SearchResponse;
import com.search.entity.IReview;

import java.util.List;

/**
 * Created by jitendra.k on 14/02/17.
 */
public interface IReviewRepository<T> extends IRepository<T> {

    List<SearchResponse> getTopMatch(List<String> patterns, int count);
    void initialize(String[] args);
}
