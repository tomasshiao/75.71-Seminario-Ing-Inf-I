package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.PersonTransaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface PersonTransactionRepository extends JpaRepository<PersonTransaction, Long> {

}