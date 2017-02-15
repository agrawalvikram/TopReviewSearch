package com.search;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.search.datastore.DataStore;
import com.search.datastore.IReviewRepository;
import com.search.datastore.InMemoryReviewRepository;
import com.search.dtos.Review;
import com.search.entity.IReview;

/**
 * Created by jitendra.k on 14/02/17.
 */
public class AppModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(IReview.class).to(Review.class);
        binder.bind(new TypeLiteral<IReviewRepository<Review>>() {}).to(InMemoryReviewRepository.class);
        binder.bind(DataStore.class);
    }
}
