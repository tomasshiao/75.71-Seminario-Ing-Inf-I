package com.example.trackNGO.Model

import org.hibernate.annotations.GenericGenerator

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import java.time.LocalDateTime

@Entity
class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")

    private Long id
    private LocalDateTime createdDate
    private Boolean isRecurrent
    private DonationRecurrency recurrencyType
    private DonationType donationType
    private DonationStatus status
    private Person donor

    Donation(){}

    Donation(DonationRecurrency recurrencyType, DonationType donationType, Person donor){
        this.createdDate = LocalDateTime.now()
        if(donationType != DonationType.MONETARY){
            this.isRecurrent = false
            this.recurrencyType = DonationRecurrency.ONE_TIME
        } else {
            this.isRecurrent = recurrencyType != DonationRecurrency.ONE_TIME
            this.recurrencyType = recurrencyType
        }
        this.donationType = donationType
        this.status = DonationStatus.ACTIVE
        this.donor = donor
        this
    }

    Long getId(){
        this.id
    }

    LocalDateTime getCreatedDate(){
        this.createdDate
    }

    Boolean getIsRecurrent(){
        this.isRecurrent
    }

    DonationRecurrency getRecurrencyType(){
        this.recurrencyType
    }

    DonationType getDonationType(){
        this.donationType
    }

    DonationStatus getStatus(){
        this.status
    }

    DonationStatus setStatus(DonationStatus newStatus){
        this.status = newStatus
        this.status
    }

    Person getDonor(){
        this.donor
    }

    Map<String, Object> toDTO(){
        [
                "id": this.getId(),
                "createdDate": this.getCreatedDate(),
                "isRecurrent": this.getIsRecurrent(),
                "recurrencyType": this.getRecurrencyType(),
                "donationType": this.getDonationType(),
                "status": this.getStatus(),
                "donor": this.getDonor()
        ] as Map<String, Object>
    }
}
