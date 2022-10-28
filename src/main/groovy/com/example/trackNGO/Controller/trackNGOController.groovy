package com.example.trackNGO.Controller

import com.example.trackNGO.Repositories.SystemAdministratorRepository
import com.example.trackNGO.Repositories.VolunteerRepository
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
}
