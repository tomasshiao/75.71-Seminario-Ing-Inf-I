package com.example.trackNGO.Model

import java.time.LocalDateTime

interface Person {
    Long getId()
    LocalDateTime getCreatedDate()
    String getName()
    Map<String, Object> toDTO()
    Map<String, Object> toViewDTO()
    Profile getProfile()
    String getRecordType()
}