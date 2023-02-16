package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.Organization
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Organization findByName(@Param("name") String name)
}