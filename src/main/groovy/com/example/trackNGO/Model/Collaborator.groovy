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

    @OneToMany(mappedBy = "collaborator", fetch = FetchType.EAGER)
    private Set<PersonTransaction> personTransactions = new HashSet<>()

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
    Map<String, Object> toDTO(){
        [
                "collaboratorName": this.getName(),
                "id": this.getId(),
                "profile": this.getProfile(),
                "createdDate": this.getCreatedDate()
        ] as Map<String, Object>
    }
}
