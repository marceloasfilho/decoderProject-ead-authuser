package com.ead.authuser.repository;

import com.ead.authuser.model.UserCourseModel;
import com.ead.authuser.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserCourseRepository extends JpaRepository<UserCourseModel, UUID> {
    boolean existsByUserModelAndCourseId(UserModel userModel, UUID courseId);

    @Query(value = "SELECT * FROM tb_user_course WHERE user_id=: userId", nativeQuery = true)
    List<UserCourseModel> findAllUserCourseIntoUser(@Param("userId") UUID userId);
}
