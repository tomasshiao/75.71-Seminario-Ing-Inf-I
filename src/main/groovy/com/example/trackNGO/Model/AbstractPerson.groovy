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
abstract class AbstractPerson implements Person{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id
    private LocalDateTime createdDate

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    private Set<PersonTransaction> personTransactions = new HashSet<>()

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    private Set<PersonDonation> personDonations = new HashSet<>()

}
