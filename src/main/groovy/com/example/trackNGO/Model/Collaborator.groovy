package com.example.trackNGO.Model

import org.hibernate.annotations.GenericGenerator

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Collaborator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id
    private Date createdDate
    private Profile profile
    private String collaboratorName
    private String password

    Collaborator(){}

    Collaborator(String collaboratorName, String password, Profile profile){
        this.collaboratorName = collaboratorName
        this.password = password
        this.profile = profile
        this
    }

    Long getId(){
        this.id
    }

    Date getCreatedDate(){
        this.createdDate
    }

    String getCollaboratorName(){
        this.collaboratorName
    }

    String getPassword(){
        this.password
    }

    Map<String, Object> getProfile(){
        this.profile
    }

    Map<String, Object> toDTO(){
        ["collaboratorName": this.getCollaboratorName(), "id": this.getId(), "profile": this.getProfile(), "createdDate": this.getCreatedDate()] as Map<String, Object>
    }
}
