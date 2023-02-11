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
class PersonDonation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id
    private LocalDateTime createdDate
    private Person person

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="donation_id")
    private Donation donation

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="collaborator_id")
    private Collaborator collaborator

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="friend_id")
    private Friend friend

    @JsonIgnore
    Donation getDonation(){
        this.donation
    }

    @JsonIgnore
    Person getPerson(){
        this.person
    }

    PersonDonation(){}

    PersonDonation(Donation donation, Collaborator collaborator){
        this.donation = donation
        this.collaborator = collaborator
        this.friend = null
        this.person = collaborator
        this.createdDate = LocalDateTime.now()
        this
    }

    PersonDonation(Donation donation, Friend friend){
        this.donation = donation
        this.collaborator = null
        this.friend = friend
        this.person = friend
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
