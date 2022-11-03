package com.example.trackNGO.Model

import org.hibernate.annotations.GenericGenerator

import javax.persistence.*

@Entity
class VolunteerProfile implements Profile{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")

    private Long id
    private final String profileName = "Volunteer"

    VolunteerProfile(){}

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
