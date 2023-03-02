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
import javax.persistence.OneToOne
import java.time.LocalDateTime
import java.util.stream.Collectors

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
    private Long transactionNumber

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="organizationPerson_id")
    private OrganizationPerson organizationPerson

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="personTransaction_id")
    private Set<PersonTransaction> personTransactions = new HashSet<>()

    @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER)
    private Set<OrganizationTransaction> organizationTransactions = new HashSet<OrganizationTransaction>()

    Transaction(){}

    Transaction(BigDecimal amount, TransactionType type, Long transactionNumber){
        LocalDateTime now = LocalDateTime.now()
        String nowString = String.valueOf(now)
        String noSymbolsNow = nowString.substring(0, 4) + nowString.substring(5,7) + nowString.substring(8,10) + nowString.substring(11,13)+nowString.substring(14,16)+nowString.substring(17,19) + nowString.substring(20,23)
        this.txnName = type.toString() + "-" + noSymbolsNow
        this.createdDate = LocalDateTime.now()
        this.type = type
        this.amount = amount
        this.status = TransactionStatus.PROCESSING
        this.rejectionReason = ""
        this.transactionNumber = transactionNumber
        this
    }

    Long getId(){
        this.id
    }

    LocalDateTime getCreatedDate(){
        this.createdDate
    }

    String getName(){
        this.txnName
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

    String setTxnDescription(String description){
        this.description = description
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
        this.personTransactions.stream()
                .filter(personTxn -> personTxn.getTransaction().getId() == this.id)
                .collect(Collectors.toList())[0]?.getPerson()
    }

    Set<PersonTransaction> getPersonTransactions(){
        this.personTransactions
    }

    Set<PersonTransaction> setPersonTransaction(Set<PersonTransaction> personTransactions){
        this.personTransactions = personTransactions
    }

    OrganizationPerson getOrganizationPerson(){
        this.organizationPerson
    }

    OrganizationPerson setOrganizationPerson(OrganizationPerson orgPerson){
        this.organizationPerson = orgPerson
    }

    OrganizationTransaction getTxnOrg(){
        this.organizationTransactions.first()
    }

    Long getTransactionNumber(){
        this.transactionNumber
    }

    Map<String, Object> toViewDTO(){
        [
                "txnId": this.getId(),
                "createdDate": this.getCreatedDate(),
                "orgTxnId": this.getTxnOrg().getId(),
                "personTxnIdSet": this.getPersonTransactions().forEach(x -> x.getId()),
                "fields": [
                        [
                                "fieldName": "Name",
                                "fieldValue": this.getName()
                        ],
                        [
                                "fieldName": "Type",
                                "fieldValue": this.getTxnType()
                        ],
                        [
                                "fieldName": "Status",
                                "fieldValue": this.getStatus()
                        ],
                        [
                                "fieldName": "Transaction Person",
                                "fieldValue": this.getTxnPerson().getName()
                        ],
                        [
                                "fieldName": "Amount",
                                "fieldValue": this.getTxnAmount()
                        ],
                        [
                                "fieldName": "Description",
                                "fieldValue": this.getTxnDescription()
                        ],
                        [
                                "fieldName": "Rejection Reason",
                                "fieldValue": this.getRejectionReason()
                        ]
                ]
        ] as Map<String, Object>
    }

    Map<String, Object> toDTO(){
        [
            "txnId": this.getId(),
            "createdDate": this.getCreatedDate(),
            "orgTxnId": this.getTxnOrg().getId(),
            "personTxnIdSet": this.getPersonTransactions().forEach(x -> x.getId()),
            "Name": this.getName(),
            "type": this.getTxnType(),
            "status": this.getStatus(),
            "txnPersonId": this.getTxnPerson()?.getId(),
            "amount": this.getTxnAmount(),
            "description": this.getTxnDescription(),
            "rejectionReason": this.getRejectionReason(),
            "transactionNumber": this.getTransactionNumber()
        ] as Map<String, Object>
    }
}
