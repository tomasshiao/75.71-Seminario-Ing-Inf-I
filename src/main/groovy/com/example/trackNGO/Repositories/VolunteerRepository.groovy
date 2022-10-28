package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.Volunteer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    Volunteer findByUserName(@Param("userName")String userName)
}
