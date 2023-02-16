package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.OrganizationTransaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface OrganizationTransactionRepository extends JpaRepository<OrganizationTransaction, Long> {

}