package com.example.trackNGO.Model

import org.hibernate.annotations.GenericGenerator

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.OneToMany
import java.time.LocalDateTime

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class AbstractPerson{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)

    private Long id
    private LocalDateTime createdDate

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    private Set<PersonTransaction> personTransactions = new HashSet<>()

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    private Set<PersonDonation> personDonations = new HashSet<>()

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    private Set<OrganizationPerson> organizationPersons = new HashSet<OrganizationPerson>()
}
