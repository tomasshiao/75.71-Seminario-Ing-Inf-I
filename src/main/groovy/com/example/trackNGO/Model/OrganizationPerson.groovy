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
import javax.persistence.OneToMany
import java.time.LocalDateTime

@Entity
class OrganizationPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id
    private LocalDateTime createdDate
    private Person person

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="org_id")
    private Organization organization

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="collaborator_id")
    private Collaborator collaborator

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="friend_id")
    private Friend friend


    @OneToMany(mappedBy = "organizationPerson", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>()

    @OneToMany(mappedBy = "organizationPerson", fetch = FetchType.EAGER)
    private Set<Donation> donations = new HashSet<>()

    @JsonIgnore
    Organization getOrganization(){
        this.organization
    }

    @JsonIgnore
    Person getPerson(){
        this.person
    }

    @JsonIgnore
    Set<Transaction> getTransactions(){
        this.transactions
    }

    @JsonIgnore
    Set<Donation> getDonations(){
        this.donations
    }

    OrganizationPerson(){}

    OrganizationPerson(Organization organization, Collaborator collaborator){
        this.organization = organization
        this.collaborator = collaborator
        this.friend = null
        this.person = collaborator
        this.createdDate = LocalDateTime.now()
    }

    OrganizationPerson(Organization organization, Friend friend){
        this.organization = organization
        this.collaborator = null
        this.friend = friend
        this.person = friend
        this.createdDate = LocalDateTime.now()
    }

    Long getId(){
        this.id
    }

    LocalDateTime getCreatedDate(){
        this.createdDate
    }

    Map<String, Object> toDTO(){
        [
                "orgId": this.getOrganization().getId(),
                "personId": this.getPerson().getId(),
                "orgPersonId": this.getId(),
                "person": this.getPerson().toDTO(),
                "createdDate": this.getCreatedDate(),
                "organization": this.getOrganization().toDTO()
        ] as Map<String, Object>
    }
}
