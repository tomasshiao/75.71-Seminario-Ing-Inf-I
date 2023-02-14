package com.example.trackNGO.Model

import org.hibernate.annotations.GenericGenerator

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import java.time.LocalDateTime

@Entity
class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")

    private Long id
    private String name
    private LocalDateTime createdDate
    private BigDecimal balance

    @OneToMany(mappedBy = "organization", fetch = FetchType.EAGER)
    private Set<OrganizationPerson> organizationPersons = new HashSet<OrganizationPerson>()

    @OneToMany(mappedBy = "organization", fetch = FetchType.EAGER)
    private Set<OrganizationEvent> organizationEvents = new HashSet<OrganizationEvent>()

    @OneToMany(mappedBy = "organization", fetch = FetchType.EAGER)
    private Set<OrganizationTransaction> organizationTransactions = new HashSet<OrganizationTransaction>()

    @OneToMany(mappedBy = "organization", fetch = FetchType.EAGER)
    private Set<OrganizationDonation> organizationDonations = new HashSet<OrganizationDonation>()

    Organization(){}

    Organization(String name){
        this.name = name
        this.createdDate = LocalDateTime.now()
        this.balance = 0
        this
    }

    Long getId(){
        this.id
    }

    String getName(){
        this.name
    }

    LocalDateTime getCreatedDate(){
        this.createdDate
    }

    BigDecimal getBalance(){
        this.balance
    }

    HashSet<OrganizationPerson> getOrgPersons(){
        this.organizationPersons
    }

    HashSet<OrganizationEvent> getOrgEvents(){
        this.organizationEvents
    }

    HashSet<OrganizationTransaction> getOrgTransactions(){
        this.organizationTransactions
    }

    HashSet<Person> getOrgAdmins(){
        this.organizationPersons.stream().filter(collaborator -> collaborator.getPerson().getProfile() == Profile.SYSADMIN) as HashSet<Person>
    }

    BigDecimal setBalance(BigDecimal newBalance){
        this.balance = newBalance
    }

    HashSet<OrganizationDonation> getOrgDonations(){
        this.organizationDonations
    }

    Map<String, Object> toDTO(){
        [
                "id": this.getId(),
                "createdDate": this.getCreatedDate(),
                "collaborators": this.getOrgPersons(),
                "transactions": this.getOrgTransactions(),
                "events": this.getOrgEvents(),
                "donations": this.getOrgDonations(),
                "name": this.getName(),
                "admins": this.getOrgAdmins(),
                "balance": this.getBalance()
        ] as Map<String, Object>
    }
}
