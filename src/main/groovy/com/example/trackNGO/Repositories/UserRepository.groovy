package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(@Param("userName")String userName)
}