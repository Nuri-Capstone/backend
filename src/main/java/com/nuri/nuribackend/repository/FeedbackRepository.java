package com.nuri.nuribackend.repository;

import com.nuri.nuribackend.domain.Feedback.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends MongoRepository<Feedback, String> {
}
