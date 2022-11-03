package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.SystemAdministratorProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface SystemAdministratorRepository extends JpaRepository<SystemAdministratorProfile, Long> {
    SystemAdministratorProfile findByUserName(@Param("userName")String userName)
}