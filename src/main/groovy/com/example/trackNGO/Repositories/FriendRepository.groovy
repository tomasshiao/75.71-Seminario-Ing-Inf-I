package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.Friend
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface FriendRepository extends JpaRepository<Friend, Long>{
    Friend findByFullName(@Param("fullName") String fullName)
}