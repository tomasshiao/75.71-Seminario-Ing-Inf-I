package com.example.trackNGO.Model

import org.hibernate.annotations.GenericGenerator

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import java.time.LocalDateTime

@Entity
class Friend implements Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id
    private LocalDateTime createdDate
    private String fullName

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

    Map<String, Object> toDTO(){
        [
                "collaboratorName": this.getName(),
                "id": this.getId(),
                "createdDate": this.getCreatedDate()
        ] as Map<String, Object>
    }
}
