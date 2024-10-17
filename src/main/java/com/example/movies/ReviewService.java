package com.example.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Review createReview(String reviewBody, String imdbId) {
        Review review = reviewRepository.insert(new Review(reviewBody));

        mongoTemplate.update(Movie.class) /*Using mongoTemplate to call an update on our Movie class */
                .matching(Criteria.where("imdbId").is(imdbId))
                /*Here we match the same imdb that we have with the one we are receiving from the user*/
                .apply(new Update().push("reviewIds").value(review))/*then we want to apply those changes, we call the push inside the reviewIds array*/
                .first();

        return review;

    }
}
