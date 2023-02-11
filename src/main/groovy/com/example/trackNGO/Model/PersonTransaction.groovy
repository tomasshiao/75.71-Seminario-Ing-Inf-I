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
import javax.persistence.OneToOne
import java.time.LocalDateTime

@Entity
class PersonTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id
    private LocalDateTime createdDate

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="transaction_id")
    private Transaction transaction

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = AbstractPerson.class)
    @JoinColumn(name="person_id")
    private Person person

    @JsonIgnore
    Transaction getTransaction(){
        this.transaction
    }

    @JsonIgnore
    Person getPerson(){
        this.person
    }

    PersonTransaction(){}

    PersonTransaction(Transaction transaction, Person person){
        this.transaction = transaction
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
                "personTransactionId": this.getId(),
                "transaction": this.getTransaction().toDTO(),
                "createdDate": this.getCreatedDate(),
                "person": this.getPerson().toDTO()
        ] as Map<String, Object>
    }
}
