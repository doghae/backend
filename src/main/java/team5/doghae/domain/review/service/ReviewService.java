package team5.doghae.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team5.doghae.domain.review.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;


}
