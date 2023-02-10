package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.Donation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface DonationRepository extends JpaRepository<Donation, Long>{
    Donation findDonationById(@Param("donationId") Long id)
}