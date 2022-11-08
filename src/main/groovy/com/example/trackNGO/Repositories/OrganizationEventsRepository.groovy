package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.OrganizationEvents
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface OrganizationEventsRepository extends JpaRepository<OrganizationEvents, Long> {
}