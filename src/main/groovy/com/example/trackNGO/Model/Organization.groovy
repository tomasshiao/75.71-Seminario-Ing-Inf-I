package com.example.trackNGO.Model

import org.hibernate.annotations.GenericGenerator

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")

    private Long id
    private String name

    @OneToMany(mappedBy = "organization", fetch = FetchType.EAGER)
    private Set<OrganizationCollaborator> organizationCollaborators = new HashSet<OrganizationCollaborator>()

    @OneToMany(mappedBy = "organization", fetch = FetchType.EAGER)
    private Set<OrganizationEvent> organizationEvents = new HashSet<OrganizationEvent>()

    Organization(){}

    Organization(String name){
        this.name = name
        this
    }

    Long getId(){
        this.id
    }

    String getName(){
        this.name
    }

    HashSet<OrganizationCollaborator> getOrgCollaborators(){
        this.organizationCollaborators
    }

    HashSet<OrganizationEvent> getOrgEvents(){
        this.organizationEvents
    }

    HashSet<Collaborator> getOrgAdmins(){
        this.organizationCollaborators.stream().filter(collaborator -> collaborator.getCollaborator().getProfile() == "SysAdmin") as HashSet<Collaborator>
    }

    Map<String, Object> toDTO(){
        [
                "id": this.getId(),
                "name": this.getName(),
                "collaborators": this.getOrgCollaborators(),
                "events": this.getOrgEvents(),
                "admins": this.getOrgAdmins()
        ] as Map<String, Object>
    }
}
