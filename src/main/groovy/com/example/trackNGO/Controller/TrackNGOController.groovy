package com.example.trackNGO.Controller

import com.example.trackNGO.Model.AbstractPerson
import com.example.trackNGO.Model.Collaborator
import com.example.trackNGO.Model.Donation
import com.example.trackNGO.Model.DonationRecurrency
import com.example.trackNGO.Model.DonationType
import com.example.trackNGO.Model.Friend
import com.example.trackNGO.Model.Organization
import com.example.trackNGO.Model.OrganizationPerson
import com.example.trackNGO.Model.Person
import com.example.trackNGO.Model.PersonDonation
import com.example.trackNGO.Model.Transaction
import com.example.trackNGO.Model.TransactionType
import com.example.trackNGO.Repositories.DonationRepository
import com.example.trackNGO.Repositories.FriendRepository
import com.example.trackNGO.Repositories.OrganizationEventRepository
import com.example.trackNGO.Repositories.OrganizationRepository
import com.example.trackNGO.Repositories.OrganizationPersonRepository

import com.example.trackNGO.Repositories.CollaboratorRepository
import com.example.trackNGO.Repositories.EventRepository
import com.example.trackNGO.Repositories.PersonDonationRepository
import com.example.trackNGO.Repositories.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import java.nio.file.Path
import java.util.stream.Collectors

@RestController
@RequestMapping("/api")
class TrackNGOController {
    @Autowired
    private CollaboratorRepository collaboratorRepository

    @Autowired
    private DonationRepository donationRepository

    @Autowired
    private EventRepository eventRepository

    @Autowired
    private FriendRepository friendRepository

    @Autowired
    private OrganizationRepository organizationRepository

    @Autowired
    private OrganizationEventRepository organizationEventsRepository

    @Autowired
    private OrganizationPersonRepository organizationPersonRepository

    @Autowired
    private PersonDonationRepository personDonationRepository

    @Autowired
    private TransactionRepository transactionRepository

    @RequestMapping(path = "/events", method = RequestMethod.GET)
    Map<String, Object> getEventsList(Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<>()
        if (!isGuest(authentication)) {
            Collaborator collaborator = collaboratorRepository.findByCollaboratorName(authentication.getName())
            OrganizationPerson organizationPerson = organizationPersonRepository.findByPerson(collaborator)
            dto.put("collaborator", collaborator.toDTO())
            if(organizationPerson != null){
                Long orgId = organizationPerson.toDTO().get("orgId") as Long
                Organization org = organizationRepository.findById(orgId) as Organization
                dto.put("organization", org.toDTO())
                dto.put("events", organizationEventsRepository
                        .findAll()
                        .stream()
                        .filter(orgEvent -> orgEvent.toDTO().get("orgId"))
                        .collect(Collectors.toList())
                )
            } else {
                dto.put("organization", "User is not in any Org.")
                dto.put("events", "No events.")
            }
        } else {
            dto.put("collaborator", "Guest")
        }
        return dto
    }

    @RequestMapping(path = "/donations/{donationType}/{donorId}/{donationRecurrency}/{amount}", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> createDonation(@PathVariable String donationType, @PathVariable Long donorId, @PathVariable String donationRecurrency, @PathVariable BigDecimal amount, Authentication authentication){
        if(isGuest(authentication)){
            return new ResponseEntity<>(MakeMap("error", "Usuario no autenticado."), HttpStatus.FORBIDDEN)
        }
        if(donationType.isBlank() || donationType == "" || donationType == null || donorId == null || donationRecurrency.isBlank() || donationRecurrency == "" || donationRecurrency == null || amount == null){
            return new ResponseEntity<>(MakeMap("error", "Parámetros Inválidos. Completar todos los campos necesarios."), HttpStatus.FORBIDDEN)
        }
        Collaborator collaborator = collaboratorRepository.findById(donorId).orElse(null)
        Friend friend = friendRepository.findById(donorId).orElse(null)
        if(collaborator == null && friend == null){
            return new ResponseEntity<>(MakeMap("error", "Donante Inválido"), HttpStatus.FORBIDDEN)
        }
        if(!(DonationType.values().contains(donationType.toUpperCase() as DonationType))){
            return new ResponseEntity<>(MakeMap("error", "Tipo de Donación Inválido"), HttpStatus.FORBIDDEN)
        }
        if(!(DonationRecurrency.values().contains(donationRecurrency.toUpperCase() as DonationRecurrency)) && (DonationType.MONETARY != (donationType.toUpperCase() as DonationType))){
            return new ResponseEntity<>(MakeMap("error", "Tipo de Recurrencia Inválido"), HttpStatus.FORBIDDEN)
        }
        if(amount <= 0 && (DonationType.MONETARY != (donationType.toUpperCase() as DonationType))){
            return new ResponseEntity<>(MakeMap("error", "Monto Inválido"), HttpStatus.FORBIDDEN)
        }
        Person donor = (collaborator == null) ? friend : collaborator
        DonationType type = donationType.toUpperCase() as DonationType
        DonationRecurrency recurrency = donationRecurrency.toUpperCase() as DonationRecurrency
        Long donationNumber = donationRepository.findAll().stream().count() + 1
        Donation donation = donationRepository.save(new Donation(recurrency, type, amount, donationNumber))
        PersonDonation personDonation = personDonationRepository.save(new PersonDonation(donation, donor))
        Map<String,Object> response = new LinkedHashMap<>()
        if((DonationType.MONETARY == (donationType.toUpperCase() as DonationType))){
            Transaction transaction = transactionRepository.save(new Transaction(amount, TransactionType.RECEIPT, 1))
            response.put("txnId", transaction.getId())
        }
        donation.setPersonDonation(personDonation)
        donationRepository.save(donation)
        response.put("personDonationId", personDonation.getId())
        response.put("donationId", donation.getId())
        return new ResponseEntity<>(response, HttpStatus.CREATED)
    }


    private static boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken
    }

    private static Map<String,Object> MakeMap (String key, Object value){
        Map<String, Object> mapaCreado = new LinkedHashMap<>()
        mapaCreado.put (key, value)
        return mapaCreado
    }
}
