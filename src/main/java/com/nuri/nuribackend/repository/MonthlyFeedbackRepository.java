package com.nuri.nuribackend.repository;

import com.nuri.nuribackend.domain.Feedback.MonthlyFeedback;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyFeedbackRepository extends MongoRepository<MonthlyFeedback, String> {

}
