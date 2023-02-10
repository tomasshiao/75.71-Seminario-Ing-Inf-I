package com.example.trackNGO.Model

import org.hibernate.annotations.GenericGenerator

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import java.time.LocalDateTime

@Entity
class Collaborator implements Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id
    private LocalDateTime createdDate
    private Profile profile
    private String collaboratorName
    private String password

    Collaborator(){}

    Collaborator(String collaboratorName, String password, Profile profile){
        this.collaboratorName = collaboratorName
        this.password = password
        this.profile = profile
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
        this.collaboratorName
    }

    String getPassword(){
        this.password
    }

    Profile getProfile(){
        this.profile
    }

    Map<String, Object> toDTO(){
        [
                "collaboratorName": this.getName(),
                "id": this.getId(),
                "profile": this.getProfile(),
                "createdDate": this.getCreatedDate()
        ] as Map<String, Object>
    }
}
