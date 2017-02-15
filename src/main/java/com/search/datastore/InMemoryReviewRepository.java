package com.search.datastore;

import com.google.inject.Singleton;
import com.search.dtos.Review;
import com.search.dtos.SearchResponse;
import com.search.dtos.SortReviewModel;
import com.search.entity.IReview;
import com.search.helper.FileProcessor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;

/**
 * Created by jitendra.k on 14/02/17.
 */
@Singleton
@Slf4j
public class InMemoryReviewRepository implements IReviewRepository<Review> {

    private Map<String, HashMap<Long, Long>> wordToReviewMap = new HashMap<>();
    private Map<Long, IReview> reviewMap = new HashMap<>();

    @Override
    public Review findById(Class<Review> clazz, Serializable id) {
        return null;
    }

    @Override
    public List<SearchResponse> getTopMatch(List<String> patterns, int count) {
        patterns.replaceAll(String::toLowerCase);
        List<SearchResponse> result = new ArrayList<>();

        if(count == 0 || patterns == null || patterns.size() == 0)
            return result;

        Map<Long, Double> scoreMap = new HashMap<>();
        for (String str: patterns) {
            HashMap<Long, Long> docCountMap = wordToReviewMap.get(str);
            if(docCountMap != null) {
                log.info("Found " + docCountMap.size() + " documents havind word \"" + str + "\"");
                for (Long docId : docCountMap.keySet()) {
                    if(scoreMap.containsKey(docId) == false)
                        scoreMap.put(docId, 0.0);
                    scoreMap.put(docId, scoreMap.get(docId) + 1.0);
                }
            }
        }

        List<SortReviewModel> sortReviewModels = new ArrayList<>();

        for (Long docId: scoreMap.keySet()) {
            SortReviewModel model = new SortReviewModel(docId, scoreMap.get(docId) / patterns.size(),
                reviewMap.get(docId).getScore());
            sortReviewModels.add(model);
        }

        Collections.sort(sortReviewModels, new Comparator<SortReviewModel>() {
            @Override
            public int compare(SortReviewModel o1, SortReviewModel o2) {
                if(o2.getScore() - o1.getScore() < 0.0)
                    return -1;
                if(o2.getScore() - o1.getScore() > 0.0)
                    return 1;
                else if(o1.getScore().equals(o2.getScore()) && o2.getRelevantScore() - o1.getRelevantScore() < 0.0)
                    return -1;
                else if(o1.getScore().equals(o2.getScore()) && o2.getRelevantScore() - o1.getRelevantScore() > 0.0)
                    return 1;
                return 0;
            }
        });

        for (int i = 0; i < Math.min(count, sortReviewModels.size()); ++i) {
            result.add(new SearchResponse(reviewMap.get(sortReviewModels.get(i).getDocId()),
                scoreMap.get(sortReviewModels.get(i).getDocId()) / patterns.size()));
        }
        return result;
    }

    @Override
    public void initialize(String[] args) {

        long startTime = System.nanoTime();
        List<IReview> reviews = FileProcessor.readReviews(args[0], Integer.parseInt(args[1]));
//        List<IReview> reviews = FileProcessor.readReviewsFromJson(args[0]);
        long endTime = System.nanoTime();
        log.info("Read the file in " + ((endTime - startTime) / 1000000) + " milliseconds");

        startTime = System.nanoTime();
        createDsOnReviews(reviews);
//        FileProcessor.writeJsonTofile(reviews, "reviews.json");
        endTime = System.nanoTime();
        log.info("Created In memory datastructure in " + ((endTime - startTime) / 1000000) + " milliseconds");

    }

    private void createDsOnReviews(List<IReview> reviews) {
        if(reviews == null)
            return;
        for (IReview review: reviews) {
            reviewMap.put(review.getId(), review);
            processString(review.getSummary(), review);
            processString(review.getText(), review);
        }
        // NOTE: This was use to create random queries from review word space
//        makeQueries();
    }

    private void makeQueries() {
        List<String> allWords = new ArrayList<>();
        allWords.addAll(wordToReviewMap.keySet());
        FileProcessor.makeRandomQueriesFromWords(allWords, 10000, "query.txt");
    }

    private void processString(String data, IReview review) {
        if(data != null && data.length() > 0) {
            data = data.replaceAll("\\W+", " ").toLowerCase();
            String[] words = data.split("\\s+");
            for (int i = 0; i < words.length; i++) {
                String str = words[i].replaceAll("[^\\w]", "");
                if(FileProcessor.isStopWord(str))
                    continue;
                if(wordToReviewMap.containsKey(str) == false)
                    wordToReviewMap.put(str, new HashMap<>());
                HashMap<Long, Long> reviews = wordToReviewMap.get(str);
                reviews.put(review.getId(), 1L);
            }
        }
    }
}
