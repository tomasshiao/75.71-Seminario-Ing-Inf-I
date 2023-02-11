package com.example.trackNGO.Model

import org.hibernate.annotations.GenericGenerator

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
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
    private AbstractPerson donor
    private BigDecimal amount

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="organizationPerson_id")
    private OrganizationPerson organizationPerson

    Donation(){}

    Donation(DonationRecurrency recurrencyType, DonationType donationType, AbstractPerson donor, BigDecimal amount){
        this.createdDate = LocalDateTime.now()
        if(donationType != DonationType.MONETARY){
            this.isRecurrent = false
            this.recurrencyType = DonationRecurrency.ONE_TIME
        } else {
            this.isRecurrent = recurrencyType != DonationRecurrency.ONE_TIME
            this.recurrencyType = recurrencyType
        }
        this.amount = amount
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

    AbstractPerson getDonor(){
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
                "donor": this.getDonor().toDTO()
        ] as Map<String, Object>
    }
}
