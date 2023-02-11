package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.Person
import com.example.trackNGO.Model.OrganizationPerson
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface OrganizationPersonRepository extends JpaRepository<OrganizationPerson, Long>{
    OrganizationPerson findByPerson(@Param("person_id") Person person)
}