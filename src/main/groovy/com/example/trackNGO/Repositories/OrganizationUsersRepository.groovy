package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.OrganizationUsers
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface OrganizationUsersRepository extends JpaRepository<OrganizationUsers, Long>{

}