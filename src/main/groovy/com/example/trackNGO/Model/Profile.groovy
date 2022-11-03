package com.example.trackNGO.Model

interface Profile {
    String getProfileName()
    Long getId()
    Map<String, Object> toDTO()
}