package com.example.trackNGO.Model

import javax.persistence.Entity
import java.time.LocalDateTime

interface Person {
    Long getId()
    LocalDateTime getCreatedDate()
    String getName()
    Map<String, Object> toDTO()
    Profile getProfile()
}