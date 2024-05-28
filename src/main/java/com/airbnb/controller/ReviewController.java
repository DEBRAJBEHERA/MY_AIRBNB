package com.airbnb.controller;

import com.airbnb.dto.ReviewDto;
import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.entity.Review;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private ReviewRepository reviewRepository;
    private PropertyRepository propertyRepository;
    public ReviewController(ReviewRepository reviewRepository, PropertyRepository propertyRepository) {
        this.reviewRepository = reviewRepository;
        this.propertyRepository = propertyRepository;
    }

    @PostMapping("/addReview/{propertyId}")
    public ResponseEntity<String>addReviews(@PathVariable long propertyId,
                                            @RequestBody Review review,
                                            @AuthenticationPrincipal PropertyUser user){//here getting the current user details
        //Review rev = reviewRepository.findReviewByUserIdAndPropertyId(propertyId, user.getId());


        Optional<Property> optionalProperty = propertyRepository.findById(propertyId);
        Property property = optionalProperty.get();//1st getting the property object
        Review r = reviewRepository.findReviewByUser(property, user);
        if (r!=null){
            return new ResponseEntity<>("Review already Added",HttpStatus.BAD_REQUEST);
        }

        review.setProperty(property);
        review.setPropertyUser(user);
        reviewRepository.save(review);
        return new ResponseEntity<>("Review added Successfully", HttpStatus.OK);
    }
    @GetMapping("/allReviews")
    public ResponseEntity<List<Review>>getUserReviews(@AuthenticationPrincipal PropertyUser user){
        List<Review> reviews = reviewRepository.findByPropertyUser(user);
       return new ResponseEntity<>(reviews,HttpStatus.OK);
    }
}
