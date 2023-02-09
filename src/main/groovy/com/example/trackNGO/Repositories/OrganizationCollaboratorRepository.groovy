package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.OrganizationCollaborator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface OrganizationCollaboratorRepository extends JpaRepository<OrganizationCollaborator, Long>{

}