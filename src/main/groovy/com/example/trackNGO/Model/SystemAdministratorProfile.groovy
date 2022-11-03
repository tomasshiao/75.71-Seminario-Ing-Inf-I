package com.example.trackNGO.Model

import org.hibernate.annotations.GenericGenerator

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class SystemAdministratorProfile implements Profile{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")

    private Long id
    private final String profileName = "SysAdmin"

    SystemAdministratorProfile(){}

    @Override
    Long getId(){
        this.id
    }

    @Override
    String getProfileName(){
        this.profileName
    }

    @Override
    Map<String, Object> toDTO(){
        ["id": this.id, "profile": this.profileName] as Map<String, Object>
    }
}
