package com.example.trackNGO.Model

import net.minidev.json.annotate.JsonIgnore
import org.hibernate.annotations.GenericGenerator

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import java.time.LocalDateTime


@Entity
class OrganizationDonation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id
    private LocalDateTime createdDate

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="org_id")
    private Organization organization

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="donation_id")
    private Donation donation

    @JsonIgnore
    Organization getOrganization(){
        this.organization
    }

    @JsonIgnore
    Donation getDonation(){
        this.donation
    }

    OrganizationDonation(){}

    OrganizationDonation(Organization organization, Donation donation){
        this.organization = organization
        this.donation = donation
        this.createdDate = LocalDateTime.now()
        this
    }

    Long getId(){
        this.id
    }

    LocalDateTime getCreatedDate(){
        this.createdDate
    }

    Map<String, Object> toDTO(){
        [
                "orgDonationId": this.getId(),
                "donationId": this.getDonation().getId(),
                "createdDate": this.getCreatedDate(),
                "organizationId": this.getOrganization().getId()
        ] as Map<String, Object>
    }

}
