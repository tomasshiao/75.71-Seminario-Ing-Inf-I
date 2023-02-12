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
import javax.persistence.OneToOne
import java.time.LocalDateTime

@Entity
class PersonDonation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id
    private LocalDateTime createdDate

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="donation_id")
    private Donation donation

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = AbstractPerson.class)
    @JoinColumn(name="person_id")
    private Person person

    @JsonIgnore
    Donation getDonation(){
        this.donation
    }

    @JsonIgnore
    Person getPerson(){
        this.person
    }

    PersonDonation(){}

    PersonDonation(Donation donation, Person person){
        this.donation = donation
        this.person = person
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
                "personDonationId": this.getId(),
                "donation": this.getDonation().toDTO(),
                "createdDate": this.getCreatedDate(),
                "person": this.getPerson().toDTO()
        ] as Map<String, Object>
    }
}
