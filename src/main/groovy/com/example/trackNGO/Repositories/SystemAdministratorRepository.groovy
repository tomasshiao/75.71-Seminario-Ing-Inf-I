package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.SystemAdministrator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface SystemAdministratorRepository extends JpaRepository<SystemAdministrator, Long> {
    SystemAdministrator findByUserName(@Param("userName")String userName)
}