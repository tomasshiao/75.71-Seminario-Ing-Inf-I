package com.example.trackNGO.Model

import net.minidev.json.annotate.JsonIgnore
import org.hibernate.annotations.GenericGenerator

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class OrganizationCollaborator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id
    private Date createdDate

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="org_id")
    private Organization organization

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="collaborator_id")
    private Collaborator collaborator

    @JsonIgnore
    Organization getOrganization(){
        this.organization
    }

    @JsonIgnore
    Collaborator getCollaborator(){
        this.collaborator
    }

    OrganizationCollaborator(){}

    OrganizationCollaborator(Organization organization, Collaborator collaborator){
        this.organization = organization
        this.collaborator = collaborator
        this.createdDate = new Date()
    }

    Long getId(){
        this.id
    }

    Date getCreatedDate(){
        this.createdDate
    }

    Map<String, Object> toDTO(){
        [
                "orgCollaboratorId": this.getId(),
                "collaborator": this.getCollaborator().toDTO(),
                "createdDate": this.getCreatedDate(),
                "organization": this.getOrganization().toDTO()
        ] as Map<String, Object>
    }
}
