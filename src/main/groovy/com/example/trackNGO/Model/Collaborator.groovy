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
    private Boolean passwordConfirmed

    Collaborator(){}

    Collaborator(String collaboratorName, String password, Profile profile, Boolean passwordConfirmed){
        this.collaboratorName = collaboratorName
        this.password = password
        this.profile = profile
        this.createdDate = LocalDateTime.now()
        this.passwordConfirmed = passwordConfirmed
        this
    }

    @Override
    String getRecordType(){
        "Collaborator"
    }

    Set<OrganizationPerson> getOrganizationPersons(){
        super.getOrganizationPersons()
    }

    Set<PersonDonation> getPersonDonations(){
        super.getPersonDonations()
    }

    Set<PersonTransaction> getPersonTransactions(){
        super.getPersonTransactions()
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

    String setPassword(String password){
        this.password = password
        this.passwordConfirmed = (password != null)
    }

    @Override
    Profile getProfile(){
        this.profile
    }

    /*Profile setProfile(Profile profile){
        this.profile = profile
    }*/

    Boolean getPasswordConfirmed(){
        this.passwordConfirmed
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
                                "fieldValue": this.getRecordType()
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
            "passwordConfirmed": this.getPasswordConfirmed(),
            "profile": this.getProfile(),
            "recordType": this.getRecordType()
        ] as Map<String, Object>
    }
}
