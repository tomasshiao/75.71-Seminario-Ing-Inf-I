package com.example.trackNGO.Model

import org.hibernate.annotations.GenericGenerator

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import java.time.LocalDateTime
import java.util.stream.Collectors

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

    BigDecimal setBalance(BigDecimal newBalance){
        this.balance = newBalance
    }

    HashSet<OrganizationDonation> getOrgDonations(){
        this.organizationDonations
    }

    List<Collaborator> getCollaborators(){
        this.getOrgPersons().stream()
                .filter(op -> op.getPerson().toDTO().get("recordType") == "Collaborator")
                .map(op -> op.getPerson().toDTO())
                .collect(Collectors.toList())
    }

    List<Collaborator> getOrgAdmins(){
        this.getOrgPersons().stream()
                .filter(op -> (op.getPerson().toDTO().get("recordType") == "Collaborator" && op.getPerson().getProfile() == Profile.SYSADMIN))
                .map(op -> op.getPerson().toDTO())
                .collect(Collectors.toList())
    }

    Map<String, Object> toDTO(){
        [
                "id": this.getId(),
                "createdDate": this.getCreatedDate(),
                "collaborators": this.getCollaborators(),
                "transactions": this.getOrgTransactions().stream().map(orgTxn -> orgTxn.getTransaction().toDTO()).collect(Collectors.toList()),
                "events": this.getOrgEvents().stream().map(orgEv -> orgEv.getEvent().toDTO()).collect(Collectors.toList()),
                "donations": this.getOrgDonations().stream().map(orgDon -> orgDon.getDonation().toDTO()).collect(Collectors.toList()),
                "name": this.getName(),
                "admins": this.getOrgAdmins(),
                "balance": this.getBalance()
        ] as Map<String, Object>
    }
}
