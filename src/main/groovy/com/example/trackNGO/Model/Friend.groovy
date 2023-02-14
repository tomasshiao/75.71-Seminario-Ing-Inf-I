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
class Friend extends AbstractPerson implements Person{
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
        this.fullName
    }

    @Override
    Profile getProfile(){
        Profile.NOT_APPLICABLE
    }

    @Override
    Map<String, Object> toViewDTO(){
        [
                "friendName": this.getName(),
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
                                "fieldValue": "Friend"
                        ]
                ]
        ] as Map<String, Object>
    }

    @Override
    Map<String, Object> toDTO() {
        [
                "id"         : this.getId(),
                "createdDate": this.getCreatedDate(),
                "fullName"   : this.getName(),
                "profile"    : this.getProfile(),
                "recordType" : "Friend"
        ] as Map<String, Object>
    }
}
