package com.example.trackNGO.Model

import org.hibernate.annotations.GenericGenerator

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.transaction.InvalidTransactionException
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
    private Set<OrganizationCollaborator> organizationCollaborators = new HashSet<OrganizationCollaborator>()

    @OneToMany(mappedBy = "organization", fetch = FetchType.EAGER)
    private Set<OrganizationEvent> organizationEvents = new HashSet<OrganizationEvent>()

    @OneToMany(mappedBy = "organization", fetch = FetchType.EAGER)
    private Set<OrganizationTransaction> organizationTransactions = new HashSet<OrganizationTransaction>()

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

    HashSet<OrganizationCollaborator> getOrgCollaborators(){
        this.organizationCollaborators
    }

    HashSet<OrganizationEvent> getOrgEvents(){
        this.organizationEvents
    }

    HashSet<OrganizationTransaction> getOrgTransactions(){
        this.organizationTransactions
    }

    HashSet<Collaborator> getOrgAdmins(){
        this.organizationCollaborators.stream().filter(collaborator -> collaborator.getCollaborator().getProfile() == "SysAdmin") as HashSet<Collaborator>
    }

    private updateBalance(Transaction txn){
        TransactionType txnType = txn.getTxnType()
        if(txnType == TransactionType.PAYMENT || txnType == TransactionType.PURCHASE){
            if(txn.getTxnAmount() > this.balance){
                txn.updateStatus(TransactionStatus.DENIED)
                txn.setRejectionReason("Saldo Insuficiente")
            } else {
                this.balance -= txn.getTxnAmount()
                txn.updateStatus(TransactionStatus.ACCEPTED)
            }
        } else if(txnType == TransactionType.RECEIPT || txnType == TransactionType.SALE){
            this.balance += txn.getTxnAmount()
            txn.updateStatus(TransactionStatus.ACCEPTED)
        } else {
            throw new InvalidTransactionException("Tipo de Transacción Inválido.")
        }
    }

    Map<String, Object> toDTO(){
        [
                "id": this.getId(),
                "name": this.getName(),
                "collaborators": this.getOrgCollaborators(),
                "events": this.getOrgEvents(),
                "admins": this.getOrgAdmins(),
                "balance": this.getBalance(),
                "createdDate": this.getCreatedDate()
        ] as Map<String, Object>
    }
}
