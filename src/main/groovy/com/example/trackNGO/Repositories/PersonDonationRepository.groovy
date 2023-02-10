package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.PersonDonation
import org.springframework.data.jpa.repository.JpaRepository

interface PersonDonationRepository extends JpaRepository<PersonDonation, Long>{

}