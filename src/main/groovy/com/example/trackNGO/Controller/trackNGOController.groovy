package com.example.trackNGO.Controller

import com.example.trackNGO.Repositories.OrganizationEventsRepository
import com.example.trackNGO.Repositories.OrganizationRepository
import com.example.trackNGO.Repositories.OrganizationUsersRepository
import com.example.trackNGO.Repositories.SystemAdministratorRepository
import com.example.trackNGO.Repositories.VolunteerRepository
import com.example.trackNGO.Repositories.UserRepository
import com.example.trackNGO.Repositories.EventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/app")
class TrackNGOController {

    @Autowired
    private VolunteerRepository volunteerRepository

    @Autowired
    private SystemAdministratorRepository systemAdministratorRepository

    @Autowired
    private UserRepository userRepository

    @Autowired
    private EventRepository eventRepository

    @Autowired
    private OrganizationRepository organizationRepository

    @Autowired
    private OrganizationEventsRepository organizationEventsRepository

    @Autowired
    private OrganizationUsersRepository organizationUsersRepository
}
