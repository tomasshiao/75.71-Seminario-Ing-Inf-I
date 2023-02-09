package com.example.trackNGO.Model

import org.hibernate.annotations.GenericGenerator

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
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

    Event(){}

    Event(String eventName, String address, EventType eventType, LocalDate eventDate, LocalTime eventTime){
        this.eventName = eventName
        this.address = address
        this.eventType = eventType
        this.eventDate = eventDate
        this.eventTime = eventTime
        this.createdDate = LocalDateTime.now()
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

    Map<String, Object> toDTO(){
        ["id": this.getId(), "createdDate": this.getCreatedDate(), "eventDate": this.getEventDate(), "eventTime": this.getEventTime(), "eventAddress": this.getEventAddress(), "eventType": this.getEventType(), "eventName": this.getEventName()] as Map<String, Object>
    }
}