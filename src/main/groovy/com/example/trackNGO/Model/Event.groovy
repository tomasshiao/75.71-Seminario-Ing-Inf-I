package com.example.trackNGO.Model

import org.hibernate.annotations.GenericGenerator

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
class Event{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id
    private LocalDateTime createdDate
    private LocalDate eventDate
    private String address
    private EventType eventType
    private String eventName
    private LocalTime eventTime
    private Long eventNumber

    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
    private Set<OrganizationEvent> organizationEvents = new HashSet<OrganizationEvent>()

    Event(){}

    Event(String eventName, String address, EventType eventType, LocalDate eventDate, LocalTime eventTime, Long eventNumber){
        this.eventName = eventName
        this.address = address
        this.eventType = eventType
        this.eventDate = eventDate
        this.eventTime = eventTime
        this.createdDate = LocalDateTime.now()
        this.eventNumber = eventNumber
        this
    }

    Long getId(){
        this.id
    }

    LocalDateTime getCreatedDate(){
        this.createdDate
    }

    LocalDate getEventDate(){
        this.eventDate
    }

    LocalTime getEventTime(){
        this.eventTime
    }

    String getEventAddress(){
        this.address
    }

    EventType getEventType(){
        this.eventType
    }

    String getEventName(){
        this.eventName
    }

    OrganizationEvent getOrganizationEvent(){
        this.organizationEvents.first()
    }

    Long getEventNumber(){
        this.eventNumber
    }

    Map<String, Object> toViewDTO(){
        [
                "id": this.getId(),
                "createdDate": this.getCreatedDate(),
                "fields": [
                        [
                                "fieldName": "Event Number",
                                "fieldValue": this.getEventNumber()
                        ],
                        [
                                "fieldName": "Event Name",
                                "fieldValue": this.getEventName()
                        ],
                        [
                                "fieldName": "Event Date",
                                "fieldValue": this.getEventDate()
                        ],
                        [
                                "fieldName": "Event Time",
                                "fieldValue": this.getEventTime()
                        ],
                        [
                                "fieldName": "Event Address",
                                "fieldValue": this.getEventAddress()
                        ],
                        [
                                "fieldName": "Event Type",
                                "fieldValue": this.getEventType()
                        ]
                ],
                "orgEvent": this.getOrganizationEvent()
        ] as Map<String, Object>
    }

    Map<String, Object> toDTO(){
        [
                "id": this.getId(),
                "createdDate": this.getCreatedDate(),
                "eventNumber": this.getEventNumber(),
                "eventName": this.getEventName(),
                "eventDate": this.getEventDate(),
                "eventTime": this.getEventTime(),
                "address": this.getEventAddress(),
                "eventType": this.getEventType(),
                "orgEvent": this.getOrganizationEvent()
        ] as Map<String, Object>
    }
}