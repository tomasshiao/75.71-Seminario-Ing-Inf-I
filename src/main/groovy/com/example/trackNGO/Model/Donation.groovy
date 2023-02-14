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
import java.util.stream.Collectors

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
    private BigDecimal amount
    private Long donationNumber

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="organizationPerson_id")
    private OrganizationPerson organizationPerson

    @OneToMany(mappedBy = "donation", fetch = FetchType.EAGER)
    private Set<PersonDonation> personDonations = new HashSet<>()

    @OneToMany(mappedBy = "donation", fetch = FetchType.EAGER)
    private Set<OrganizationDonation> organizationDonations = new HashSet<OrganizationDonation>()

    Donation(){}

    Donation(DonationRecurrency recurrencyType, DonationType donationType, BigDecimal amount, Long donationNumber){
        this.donationNumber = donationNumber
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

    Set<PersonDonation> getPersonDonation(){
        this.personDonations
    }

    Set<PersonDonation> setPersonDonation(Set<PersonDonation> personDonations){
        this.personDonations = personDonations
    }

    Person getDonor(){
        this.personDonations.stream().map(personDonation -> personDonation.getPerson()).collect(Collectors.toList()).get(0)
    }

    Long getDonationNumber(){
        this.donationNumber
    }

    OrganizationPerson getOrganizationPerson(){
        this.organizationPerson
    }

    OrganizationDonation getOrganizationDonation(){
        this.organizationDonations.first()
    }

    Map<String, Object> toDTO(){
        [
                "id": this.getId(),
                "createdDate": this.getCreatedDate(),
                "orgPerson": this.getOrganizationPerson(),
                "orgDonation": this.getOrganizationDonation(),
                "isRecurrent": this.getIsRecurrent(),
                "donationNumber": this.getDonationNumber(),
                "recurrencyType": this.getRecurrencyType(),
                "donationType": this.getDonationType(),
                "status": this.getStatus(),
                "donor": this.getDonor().toDTO()
        ] as Map<String, Object>
    }
}
