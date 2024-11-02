package com.nuri.nuribackend.repository;

import com.nuri.nuribackend.domain.Feedback.MonthlyFeedback;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface MonthlyFeedbackRepository extends MongoRepository<MonthlyFeedback, String> {

    @Query("{ 'userId': ?0, $expr: { $and: [ { $eq: [ { $year: '$date' }, ?1 ] }, { $eq: [ { $month: '$date' }, ?2 ] } ] } }")
    MonthlyFeedback findByUserIdAndYearAndMonth(Long userId, int year, int month);
}
