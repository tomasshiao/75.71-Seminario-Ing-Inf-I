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
class Friend extends AbstractPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id
    private LocalDateTime createdDate
    private String fullName

    @OneToMany(mappedBy = "friend", fetch = FetchType.EAGER)
    private Set<PersonTransaction> personTransactions = new HashSet<>()

    Friend(){}

    Friend(String fullName){
        this.fullName = fullName
        this.createdDate = LocalDateTime.now()
        this
    }

    Long getId(){
        this.id
    }

    LocalDateTime getCreatedDate(){
        this.createdDate
    }

    String getName(){
        this.fullName
    }

    Profile getProfile(){
        Profile.NOT_APPLICABLE
    }

    Map<String, Object> toDTO(){
        [
                "friendName": this.getName(),
                "id": this.getId(),
                "createdDate": this.getCreatedDate()
        ] as Map<String, Object>
    }
}
