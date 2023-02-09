package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface EventRepository extends JpaRepository<Event, Long> {
    Event findByEventName(@Param("eventName")String eventName)
}