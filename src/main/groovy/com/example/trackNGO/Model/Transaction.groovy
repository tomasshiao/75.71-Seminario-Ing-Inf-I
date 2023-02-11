package com.example.trackNGO.Model

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
class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id
    private LocalDateTime createdDate
    private BigDecimal amount
    private String txnName
    private TransactionType type
    private String description
    private TransactionStatus status
    private String rejectionReason

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="organizationPerson_id")
    private OrganizationPerson organizationPerson

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="personTransaction_id")
    private PersonTransaction personTransaction


    Transaction(){}

    Transaction(BigDecimal amount, TransactionType type){
        LocalDateTime now = LocalDateTime.now()
        String nowString = String.valueOf(now)
        String noSymbolsNow = nowString.substring(0, 4) + nowString.substring(5,7) + nowString.substring(8,10) + nowString.substring(11,13)+nowString.substring(14,16)+nowString.substring(17,19) + nowString.substring(20,23)
        this.txnName = type.toString() + "-" + noSymbolsNow
        this.createdDate = LocalDateTime.now()
        this.type = type
        this.amount = amount
        this.status = TransactionStatus.PROCESSING
        this.rejectionReason = ""
        this.personTransaction  = null
        this
    }

    Transaction(BigDecimal amount, TransactionType type, String description){
        LocalDateTime now = LocalDateTime.now()
        String nowString = String.valueOf(now)
        String noSymbolsNow = nowString.substring(0, 4) + nowString.substring(5,7) + nowString.substring(8,10) + nowString.substring(11,13)+nowString.substring(14,16)+nowString.substring(17,19) + nowString.substring(20,23)
        this.txnName = type.toString() + "-" + noSymbolsNow
        this.createdDate = LocalDateTime.now()
        this.type = type
        this.amount = amount
        this.description = description
        this.personTransaction = null
        this
    }

    Long getId(){
        this.id
    }

    LocalDateTime getCreatedDate(){
        this.createdDate
    }

    String getName(){
        this.name
    }

    BigDecimal getTxnAmount(){
        this.amount
    }

    TransactionType getTxnType(){
        this.type
    }

    String getTxnDescription(){
        this.description
    }

    TransactionStatus updateStatus(TransactionStatus newStatus){
        this.status = newStatus
        this.status
    }

    TransactionStatus getStatus(){
        this.status
    }

    String setRejectionReason(String rejectionReason){
        this.rejectionReason = rejectionReason
        this.rejectionReason
    }

    String getRejectionReason(){
        this.rejectionReason
    }

    Person getTxnPerson(){
        this.personTransaction.getPerson()
    }

    PersonTransaction setPersonTransaction(PersonTransaction personTransaction){
        this.personTransaction = personTransaction
    }

    PersonTransaction getPersonTransaction(){
        this.personTransaction
    }

    Map<String, Object> toDTO(){
        [
                "txnId": this.getId(),
                "name": this.getName(),
                "createdDate": this.getCreatedDate(),
                "amount": this.getTxnAmount(),
                "type": this.getTxnType(),
                "description": this.getTxnDescription(),
                "status": this.getStatus(),
                "rejectionReason": this.getRejectionReason(),
                "txnPerson": this.getTxnPerson().toDTO()
        ] as Map<String, Object>
    }
}
