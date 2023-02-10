package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.Collaborator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {
    Collaborator findByCollaboratorName(@Param("collaboratorName")String collaboratorName)
}