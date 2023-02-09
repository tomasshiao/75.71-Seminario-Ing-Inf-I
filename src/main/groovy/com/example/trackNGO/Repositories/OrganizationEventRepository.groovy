package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.OrganizationEvent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface OrganizationEventRepository extends JpaRepository<OrganizationEvent, Long> {
}