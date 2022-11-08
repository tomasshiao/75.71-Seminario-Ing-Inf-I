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
    private Set<OrganizationUsers> organizationUsers = new HashSet<OrganizationUsers>()

    @OneToMany(mappedBy = "organization", fetch = FetchType.EAGER)
    private Set<OrganizationEvents> organizationEvents = new HashSet<OrganizationEvents>()

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

    HashSet<OrganizationUsers> getOrgUsers(){
        this.organizationUsers
    }

    HashSet<OrganizationEvents> getOrgEvents(){
        this.organizationEvents
    }

    HashSet<User> getOrgAdmins(){
        this.organizationUsers.stream().filter(user -> user.getUser().getProfile().get("Profile") == "SysAdmin") as HashSet<User>
    }

    Map<String, Object> toDTO(){
        [
                "id": this.getId(),
                "name": this.getName(),
                "users": this.getOrgUsers(),
                "events": this.getOrgEvents(),
                "admins": this.getOrgAdmins()
        ] as Map<String, Object>
    }
}
