package com.example.trackNGO.Controller

import com.example.trackNGO.Model.Collaborator
import com.example.trackNGO.Model.Donation
import com.example.trackNGO.Model.DonationRecurrency
import com.example.trackNGO.Model.DonationType
import com.example.trackNGO.Model.Event
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
import org.springframework.web.bind.annotation.RequestParam
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

    // GET
    // Lists to display in main page
    @RequestMapping(path = "/organization/{id}", method = RequestMethod.GET)
    Map<String, Object> getOrgDataById(@PathVariable Long id, Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<>()
        if (!isGuest(authentication)) {
            dto.put("hasPermission", true) // Por ahora todos los perfiles autenticados pueden acceder
            Organization organization = organizationRepository.findById(id).orElse(null)
            dto.put("organizationExists", organization != null)
            if(organization != null){
                Collaborator collaborator = collaboratorRepository.findByCollaboratorName(authentication.getName())
                List<Event> events = organization.getOrgEvents()
                        .stream()
                        .map(orgEvent -> orgEvent.getEvent().toDTO())
                        .collect(Collectors.toList())
                List<Friend> friends = organization.getOrgPersons()
                        .stream()
                        .filter(orgPerson -> orgPerson.getPerson().class == Friend.class)
                        .map(orgPerson -> orgPerson.getPerson().toDTO())
                        .collect(Collectors.toList())
                List<Transaction> transactions = organization.getOrgTransactions()
                        .stream()
                        .map(orgTxn -> orgTxn.getTransaction().toDTO())
                        .collect(Collectors.toList())
                List<Donation> donations = organization.getOrgDonations()
                        .stream()
                        .map(orgDonation -> orgDonation.getDonation().toDTO())
                        .collect(Collectors.toList())
                dto.put("organization", organization.toDTO())
                dto.put("collaborator", collaborator.toDTO())
                dto.put("events", events)
                dto.put("friends", friends)
                dto.put("transactions", transactions)
                dto.put("donations", donations)
            }
        } else {
            dto.put("hasPermission", false)
        }
        return dto
    }

    // Record By Id
    @RequestMapping(path = "/donation/{id}", method = RequestMethod.GET)
    Map<String, Object> getDonationById(@PathVariable Long id, Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<>()
        if(!isGuest(authentication)){
            dto.put("hasPermission", true)
            Donation donation = donationRepository.findById(id).orElse(null)
            dto.put("recordExists", (donation != null))
            if(donation != null){
                Organization org = donation.getOrganizationDonation().getOrganization()
                Person donor = donation.getDonor()
                dto.put("donation", donation.toDTO())
                dto.put("organization", org.toDTO())
                dto.put("donor", donor.toDTO())
            }
        } else {
            dto.put("hasPermission", false)
        }
        return dto
    }

    @RequestMapping(path = "/event/{id}", method = RequestMethod.GET)
    Map<String, Object> getEventById(@PathVariable Long id, Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<>()
        if(!isGuest(authentication)){
            dto.put("hasPermission", true)
            Event event = eventRepository.findById(id).orElse(null)
            dto.put("recordExists", (event != null))
            if(event != null){
                Organization org = event.getOrganizationEvent().getOrganization()
                dto.put("event", event.toViewDTO())
                dto.put("organization", org.toDTO())
            }
        } else {
            dto.put("hasPermission", false)
        }
        return dto
    }

    @RequestMapping(path = "/person/{type}/{id}", method = RequestMethod.GET)
    Map<String, Object> getPersonById(@PathVariable String type, @PathVariable Long id, Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<>()
        if(!isGuest(authentication)){
            dto.put("hasPermission", true)
            if(type.toLowerCase() == "collaborator"){
                Collaborator collaborator = collaboratorRepository.findById(id).orElse(null)
                dto.put("recordExists", collaborator != null)
                if(collaborator != null){
                    dto.put("person", collaborator.toViewDTO())
                }
            } else if(type.toLowerCase() == "friend"){
                Friend friend = friendRepository.findById(id).orElse(null)
                dto.put("recordExists", friend != null)
                if(friend != null){
                    dto.put("person", friend.toViewDTO())
                }
            }
            if(dto.get("person") != null){
                OrganizationPerson orgPerson = organizationPersonRepository.findByPerson(person)
                Organization org = orgPerson.getOrganization()
                dto.put("organization", org.toDTO())
            }
        } else {
            dto.put("hasPermission", false)
        }
        return dto
    }

    @RequestMapping(path = "/transaction/{id}", method = RequestMethod.GET)
    Map<String, Object> getTransactionById(@PathVariable Long id, Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<>()
        if(!isGuest(authentication)){
            dto.put("hasPermission", true)
            Transaction txn = transactionRepository.findById(id).orElse(null)
            dto.put("recordExists", (txn != null))
            if(txn != null){
                Organization org = txn.getTxnOrg().getOrganization()
                Person txnPerson = txn.getTxnPerson()
                dto.put("transaction", txn.toViewDTO())
                dto.put("organization", org.toDTO())
                dto.put("transactionPerson", txnPerson.toViewDTO())
            }
        } else {
            dto.put("hasPermission", false)
        }
        return dto
    }

    // Find the organization the user belongs to
    @RequestMapping(path = "/collaborator/organization", method = RequestMethod.GET)
    Map<String, Object> getCollaboratorsOrganization(Authentication authentication){
        Collaborator collaborator = collaboratorRepository.findByCollaboratorName(authentication.getName())
        OrganizationPerson orgPerson = organizationPersonRepository.findByPerson(collaborator)
        Organization org = orgPerson.getOrganization()
        return ["orgId": org.getId()] as Map<String, Object>
    }

    // Find new user sign up
    @RequestMapping(path = "/api/findNewCollaborator/{name}", method = RequestMethod.GET)
    Map<String, Object> validateUserInOrg(@PathVariable String name){
        Map<String, Object> dto = new HashMap<String, Object>()
        Collaborator collaborator = collaboratorRepository.findByCollaboratorName(name)
        if(collaborator != null) {
            OrganizationPerson organizationPerson = organizationPersonRepository.findByPerson(collaborator)
            if(organizationPerson != null){
                Organization org = organizationPerson.getOrganization()
                if(org != null){
                    dto.put("userRegisteredInOrg", true)
                } else {
                    organizationPersonRepository.delete(organizationPerson)
                    collaboratorRepository.delete(collaborator)
                    dto.put("userRegisteredInOrg", false)
                }
            } else {
                collaboratorRepository.delete(collaborator)
                dto.put("userRegisteredInOrg", false)
            }
        } else {
            dto.put("userRegisteredInOrg", false)
        }
        return dto
    }

    // POST
    @RequestMapping(path = "/donations", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> createDonation(@RequestParam String donationType, @RequestParam Long donorId, @RequestParam String donationRecurrency, @RequestParam BigDecimal amount, Authentication authentication){
        if(isGuest(authentication)){
            return new ResponseEntity<>(MakeMap("errorMsg", "Usuario no autenticado."), HttpStatus.FORBIDDEN)
        }
        if(donationType.isBlank() || donationType == "" || donationType == null || donorId == null || donationRecurrency.isBlank() || donationRecurrency == "" || donationRecurrency == null || amount == null){
            return new ResponseEntity<>(MakeMap("errorMsg", "Parámetros Inválidos. Completar todos los campos necesarios."), HttpStatus.FORBIDDEN)
        }
        Collaborator collaborator = collaboratorRepository.findById(donorId).orElse(null)
        Friend friend = friendRepository.findById(donorId).orElse(null)
        if(collaborator == null && friend == null){
            return new ResponseEntity<>(MakeMap("errorMsg", "Donante Inválido"), HttpStatus.FORBIDDEN)
        }
        if(!(DonationType.values().contains(donationType.toUpperCase() as DonationType))){
            return new ResponseEntity<>(MakeMap("errorMsg", "Tipo de Donación Inválido"), HttpStatus.FORBIDDEN)
        }
        if(!(DonationRecurrency.values().contains(donationRecurrency.toUpperCase() as DonationRecurrency)) && (DonationType.MONETARY != (donationType.toUpperCase() as DonationType))){
            return new ResponseEntity<>(MakeMap("errorMsg", "Tipo de Recurrencia Inválido"), HttpStatus.FORBIDDEN)
        }
        if(amount <= 0 && (DonationType.MONETARY != (donationType.toUpperCase() as DonationType))){
            return new ResponseEntity<>(MakeMap("errorMsg", "Monto Inválido"), HttpStatus.FORBIDDEN)
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
        Set<PersonDonation> personDonationSet = new HashSet<PersonDonation>()
        personDonationSet.add(personDonation)
        donation.setPersonDonation(personDonationSet)
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
