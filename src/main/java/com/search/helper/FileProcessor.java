package com.search.helper;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.search.dtos.Review;
import com.search.entity.IReview;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jitendra.k on 14/02/17.
 */
@Slf4j
public class FileProcessor {

    private static Set<String> stopWords = null;

    /**
     * Read reviews from json list.
     *
     * @param fileName the file name
     * @return the list
     */
    public static List<IReview> readReviewsFromJson(String fileName) {

        List<IReview> reviews = new ArrayList<IReview>();
        try {
            Gson gson = new Gson();
            InputStream inputStream = new FileInputStream(fileName);
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            reader.beginArray();
            while (reader.hasNext()) {
                IReview message = gson.fromJson(reader, Review.class);
                reviews.add(message);
            }
            reader.endArray();
            reader.close();
            return reviews;
        } catch (IOException e) {
            log.error("Unable to parse review file: " + e.getMessage());
        }
        return reviews;
    }

    /**
     * Function to read {@link Review} list from given file
     *
     * @param fileName    File name where {@link Review} list is present
     * @param reviewCount Number of {@link Review} to be read from the list present in file
     * @return the list of Reviews
     */
    public static List<IReview> readReviews(String fileName, Integer reviewCount) {

        List<IReview> reviews = new ArrayList<>();

        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            String line = null;
            Review review = null;
            Long id = 1L;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("product/productId:")) {
                    review = new Review();
                    review.setProductId(getValue(line, "product/productId:\\s+"));
                } else if (review != null && line.startsWith("review/userId:"))
                    review.setUserId(getValue(line, "review/userId:\\s+"));

                else if (review != null && line.startsWith("review/profileName:"))
                    review.setProfileName(getValue(line, "review/profileName:\\s+"));

                else if (review != null && line.startsWith("review/helpfulness:"))
                    review.setHelpfulness(getValue(line, "review/helpfulness:\\s+"));

                else if (review != null && line.startsWith("review/score:")) {
                    try {
                        review.setScore(Double.parseDouble(getValue(line, "review/score:\\s+")));
                    } catch(NumberFormatException e) {}
                }
                else if (review != null && line.startsWith("review/time:")) {
                    try {
                        review.setTime(Long.parseLong(getValue(line, "review/time:\\s+")));
                    } catch(NumberFormatException e) {}
                }
                else if(review != null && line.startsWith("review/summary:"))
                    review.setSummary(getValue(line, "review/summary:\\s+"));

                else if(review != null && line.startsWith("review/text:")) {
                    review.setText(getValue(line, "review/text:\\s+"));
                    review.setId(id++);
                    reviews.add(review);
                    review = null;
                    if(reviews.size() >= reviewCount)
                        break;
                }
            }
        } catch (Exception e) {
            log.error("Unable to parse review file: " + e.getMessage());
        }
        return reviews;
    }

    private static String getValue(String line, String pattern) {
        line = line.replaceFirst(pattern, "");
        line.trim();
        return line;
    }

    /**
     * Write json tofile.
     *
     * @param reviews  the reviews
     * @param fileName the file name
     */
    public static void writeJsonTofile(List<IReview> reviews, String fileName) {

        try {

            final OutputStream out = new FileOutputStream(fileName);

            Gson gson = new Gson();

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.setIndent("  ");
            writer.beginArray();
            for (IReview review : reviews) {
                gson.toJson(review, Review.class, writer);
            }
            writer.endArray();
            writer.close();

        } catch (IOException e) {
            log.error("Unable to write to a file: " + e.getMessage(), e);
        }
    }

    /**
     * Make random queries from words.
     *
     * @param words      the words
     * @param queryCount the query count
     * @param fileName   the file name
     */
    public static void makeRandomQueriesFromWords(List<String> words, int queryCount, String fileName) {
        try {

            int len = words.size();
            StringBuilder sb = new StringBuilder();
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");

            for (int i = 0; i < queryCount; ++i) {
                int randomNum = 1 + (int)(Math.random() * 19);
                for (int j = 0; j < randomNum; ++j) {
                    int randomIdx = 1 + (int)(Math.random() * (len - 1));
                    sb.append(words.get(randomIdx) + ",");
                }
                if(sb.length() > 0) {
                    sb.setLength(sb.length() - 1);
                    writer.println(sb.toString());
                }
                sb.setLength(0);
            }
            writer.close();

        } catch (IOException e) {
            log.error("Unable to write to a file: " + e.getMessage(), e);
        }
    }

    /**
     * Is stop word boolean.
     *
     * @param word the word
     * @return the boolean
     */
    public static boolean isStopWord(String word) {
        if(stopWords == null) {
            stopWords = new HashSet<>();
            try {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                URL url = loader.getResource("stopWords.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    stopWords.add(line.trim());
                }
            } catch (IOException e) {
                log.error("Unable to process stopwords");
            }
        }
        return stopWords.contains(word);
    }

}
