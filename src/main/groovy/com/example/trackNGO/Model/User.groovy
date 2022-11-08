package com.example.trackNGO.Model

import org.hibernate.annotations.GenericGenerator

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id
    private Date createdDate
    private Profile profile
    private String userName
    private String password

    User(){}

    User(String userName, String password, Profile profile){
        this.userName = userName
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

    String getUserName(){
        this.userName
    }

    String getPassword(){
        this.password
    }

    Map<String, Object> getProfile(){
        this.profile.toDTO()
    }

    Map<String, Object> toDTO(){
        ["username": this.getUserName(), "id": this.getId(), "profile": this.getProfile(), "createdDate": this.getCreatedDate()] as Map<String, Object>
    }
}
