package com.example.trackNGO.Controller

import com.example.trackNGO.Repositories.OrganizationEventRepository
import com.example.trackNGO.Repositories.OrganizationRepository
import com.example.trackNGO.Repositories.OrganizationCollaboratorRepository

import com.example.trackNGO.Repositories.CollaboratorRepository
import com.example.trackNGO.Repositories.EventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/app")
class TrackNGOController {
    @Autowired
    private CollaboratorRepository collaboratorRepository

    @Autowired
    private EventRepository eventRepository

    @Autowired
    private OrganizationRepository organizationRepository

    @Autowired
    private OrganizationEventRepository organizationEventsRepository

    @Autowired
    private OrganizationCollaboratorRepository organizationCollaboratorRepository
}
