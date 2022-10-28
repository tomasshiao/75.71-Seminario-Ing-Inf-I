package com.example.trackNGO.Model

import org.hibernate.annotations.GenericGenerator

import javax.persistence.*

@Entity
class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")

    private Long id
    private String userName
    private String password
    private String authority

    Volunteer(){}

    Volunteer(String userName, String password){
        this.userName = userName
        this.password = password
        this.authority = 'user'
        this
    }

    Long getId(){
        this.id
    }

    String getUserName(){
        this.userName
    }

    String getPassword(){
        this.password
    }

    String getAuthority(){
        this.authority
    }

    Map<String, Object> toDTO(){
        ["username": this.userName, "id": this.id, "auth": this.authority] as Map<String, Object>
    }
}
