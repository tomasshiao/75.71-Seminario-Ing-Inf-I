package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.VolunteerProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface VolunteerRepository extends JpaRepository<VolunteerProfile, Long> {
    VolunteerProfile findByUserName(@Param("userName")String userName)
}
