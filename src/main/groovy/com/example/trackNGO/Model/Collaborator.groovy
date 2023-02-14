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
class Collaborator extends AbstractPerson implements Person{
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

    @Override
    Long getId(){
        this.id
    }

    @Override
    LocalDateTime getCreatedDate(){
        this.createdDate
    }

    @Override
    String getName(){
        this.collaboratorName
    }

    String getPassword(){
        this.password
    }

    @Override
    Profile getProfile(){
        this.profile
    }

    @Override
    Map<String, Object> toViewDTO(){
        [
                "id": this.getId(),
                "createdDate": this.getCreatedDate(),
                "fields": [
                        [
                                "fieldName": "Name",
                                "fieldValue": this.getName()
                        ],
                        [
                                "fieldName": "Profile",
                                "fieldValue": this.getProfile()
                        ],
                        [
                                "fieldName": "Record type",
                                "fieldValue": "Collaborator"
                        ]
                ]
        ] as Map<String, Object>
    }

    @Override
    Map<String, Object> toDTO(){
        [
            "id": this.getId(),
            "createdDate": this.getCreatedDate(),
            "fullName": this.getName(),
            "profile": this.getProfile(),
            "recordType": "Collaborator"
        ] as Map<String, Object>
    }
}
