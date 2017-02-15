package com.search.datastore;

import com.google.inject.Inject;
import com.search.dtos.Review;
import com.search.dtos.SearchResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jitendra.k on 14/02/17.
 */
@Slf4j
public class DataStore {

    IReviewRepository<Review> reviewRepository;

    /**
     * Instantiates a new Data store.
     *
     * @param reviewRepository the review repository
     */
    @Inject
    public DataStore(IReviewRepository<Review> reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void initialize(String[] args) {
        reviewRepository.initialize(args);
        Long memorySize = Runtime.getRuntime().totalMemory()/(1024*1024);
        log.info("Total runtime memory: " +  memorySize  + " MB");
    }

    /**
     * Gets results.
     *
     * @param patternString the pattern string
     * @param countString   the count string
     * @return the results
     */
    public List<SearchResponse> getResults(String patternString, String countString) {
        Integer count = Integer.parseInt(countString);
        String[] split = patternString.split(",");

        Long startTime = System.nanoTime();
        List<SearchResponse> topMatch = reviewRepository.getTopMatch(Arrays.asList(split), count);
        Long endTime = System.nanoTime();
        log.info("Searched the query in " + ((endTime - startTime) / 1000000) + " milliseconds");
        return topMatch;
    }
}
