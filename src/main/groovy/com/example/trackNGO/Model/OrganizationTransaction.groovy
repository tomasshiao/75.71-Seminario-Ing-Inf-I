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
class OrganizationTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id
    private LocalDateTime createdDate

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="org_id")
    private Organization organization

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="txn_id")
    private Transaction transaction

    @JsonIgnore
    Organization getOrganization(){
        this.organization
    }

    @JsonIgnore
    Transaction getTransaction(){
        this.transaction
    }

    OrganizationTransaction(){}

    OrganizationTransaction(Organization organization, Transaction transaction){
        this.organization = organization
        this.transaction = transaction
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
                "orgCollaboratorId": this.getId(),
                "transaction": this.getTransaction().toDTO(),
                "createdDate": this.getCreatedDate(),
                "organization": this.getOrganization().toDTO()
        ] as Map<String, Object>
    }

}
