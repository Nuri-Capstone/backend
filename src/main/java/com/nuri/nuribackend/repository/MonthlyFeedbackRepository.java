package com.nuri.nuribackend.repository;

import com.nuri.nuribackend.domain.Feedback.MonthlyFeedback;
import com.nuri.nuribackend.dto.Feedback.MonthlyFeedbackFromGPT;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MonthlyFeedbackRepository extends MongoRepository<MonthlyFeedback, String> {

    @Query("{ 'userId': ?0, $expr: { $and: [ { $eq: [ { $year: '$date' }, ?1 ] }, { $eq: [ { $month: '$date' }, ?2 ] } ] } }")
    MonthlyFeedback findByUserIdAndYearAndMonth(Long userId, int year, int month);

}
