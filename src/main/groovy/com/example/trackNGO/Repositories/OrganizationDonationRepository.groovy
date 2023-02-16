package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.OrganizationDonation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface OrganizationDonationRepository extends JpaRepository<OrganizationDonation, Long> {

}