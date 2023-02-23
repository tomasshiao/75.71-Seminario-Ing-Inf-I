package com.example.trackNGO.Controller

import com.example.trackNGO.Model.Collaborator
import com.example.trackNGO.Model.Donation
import com.example.trackNGO.Model.DonationRecurrency
import com.example.trackNGO.Model.DonationStatus
import com.example.trackNGO.Model.DonationType
import com.example.trackNGO.Model.Event
import com.example.trackNGO.Model.EventType
import com.example.trackNGO.Model.Friend
import com.example.trackNGO.Model.Organization
import com.example.trackNGO.Model.OrganizationDonation
import com.example.trackNGO.Model.OrganizationEvent
import com.example.trackNGO.Model.OrganizationPerson
import com.example.trackNGO.Model.OrganizationTransaction
import com.example.trackNGO.Model.Person
import com.example.trackNGO.Model.PersonDonation
import com.example.trackNGO.Model.PersonTransaction
import com.example.trackNGO.Model.Profile
import com.example.trackNGO.Model.Transaction
import com.example.trackNGO.Model.TransactionStatus
import com.example.trackNGO.Model.TransactionType
import com.example.trackNGO.Repositories.DonationRepository
import com.example.trackNGO.Repositories.FriendRepository
import com.example.trackNGO.Repositories.OrganizationDonationRepository
import com.example.trackNGO.Repositories.OrganizationEventRepository
import com.example.trackNGO.Repositories.OrganizationRepository
import com.example.trackNGO.Repositories.OrganizationPersonRepository

import com.example.trackNGO.Repositories.CollaboratorRepository
import com.example.trackNGO.Repositories.EventRepository
import com.example.trackNGO.Repositories.OrganizationTransactionRepository
import com.example.trackNGO.Repositories.PersonDonationRepository
import com.example.trackNGO.Repositories.PersonTransactionRepository
import com.example.trackNGO.Repositories.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

@RestController
@RequestMapping("/api")
class TrackNGOController {
    @Autowired
    private PasswordEncoder passwordEncoder

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
    private OrganizationDonationRepository organizationDonationRepository

    @Autowired
    private OrganizationTransactionRepository organizationTransactionRepository

    @Autowired
    private PersonDonationRepository personDonationRepository

    @Autowired
    private PersonTransactionRepository personTransactionRepository

    @Autowired
    private TransactionRepository transactionRepository

    // GET
    // Lists to display in main page
    @RequestMapping(path = "/organization/{id}", method = RequestMethod.GET)
    ResponseEntity<Map<String, Object>> getOrgDataById(@PathVariable Long id, Authentication authentication){
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
                        .filter(orgPerson -> orgPerson.getPerson().getRecordType() == "Friend")
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
            } else {
                return new ResponseEntity<>(MakeMap("errorMsg", "No existe usuario"), HttpStatus.FORBIDDEN)
            }
        } else {
            dto.put("hasPermission", false)
            return new ResponseEntity<>(MakeMap("errorMsg", "No tiene permisos"), HttpStatus.UNAUTHORIZED)
        }
        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED)
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
                dto.put("donation", donation.toViewDTO())
                dto.put("status", donation.getStatus().toString())
                dto.put("organization", org.toDTO())
                dto.put("orgId", org.getId())
                dto.put("donor", donor?.toDTO())
                dto.put("isRecurrent", donation.getIsRecurrent())
                dto.put("donationNumber", donation.getDonationNumber())
                dto.put("type", donation.getDonationType().toString())
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
                dto.put("eventNumber", event.getEventNumber())
                dto.put("orgId", org.getId())
            }
        } else {
            dto.put("hasPermission", false)
        }
        return dto
    }

    @RequestMapping(path = "/person/{type}/{id}", method = RequestMethod.GET)
    Map<String, Object> getPersonById(@PathVariable String type, @PathVariable Long id, Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<>()
        if(!isGuest(authentication) && !stringParamIsNull(type)){
            dto.put("hasPermission", true)
            if(type.toLowerCase() == "collaborator"){
                Collaborator collaborator = collaboratorRepository.findById(id).orElse(null)
                dto.put("recordExists", collaborator != null)
                if(collaborator != null){
                    dto.put("person", collaborator.toViewDTO())
                    dto.put("collaborator", collaborator.getId())
                }
            } else if(type.toLowerCase() == "friend"){
                Friend friend = friendRepository.findById(id).orElse(null)
                dto.put("recordExists", friend != null)
                if(friend != null){
                    dto.put("person", friend.toViewDTO())
                    dto.put("friend", friend.getId())
                }
            }
            if(dto.get("person") != null){
                if(dto.get("collaborator") != null){
                    Collaborator person = collaboratorRepository.findById(dto.get("collaborator") as Long).orElse(null)
                    OrganizationPerson orgPerson = organizationPersonRepository.findByPerson(person)
                    Organization org = orgPerson.getOrganization()
                    dto.put("organization", org.toDTO())
                    dto.put("orgId", org.getId())
                } else if (dto.get("friend") != null){
                    Friend person = friendRepository.findById(dto.get("friend") as Long).orElse(null)
                    OrganizationPerson orgPerson = organizationPersonRepository.findByPerson(person)
                    Organization org = orgPerson.getOrganization()
                    dto.put("organization", org.toDTO())
                    dto.put("orgId", org.getId())
                }
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
                dto.put("orgId", org.getId())
                dto.put("transactionPerson", txnPerson.toDTO())
                dto.put("transactionNumber", txn.getTransactionNumber())
            }
        } else {
            dto.put("hasPermission", false)
        }
        return dto
    }

    // Find the organization the user belongs to
    @RequestMapping(path = "/collaborator/organization", method = RequestMethod.GET)
    ResponseEntity<Map<String, Object>> getCollaboratorsOrganization(@RequestParam String username, Authentication authentication){
        validateIsGuest(authentication)
        if(stringParamIsNull(username)){
            return new ResponseEntity<>(MakeMap("errorMsg", "Parámetro de usuario inválido"), HttpStatus.BAD_REQUEST)
        }
        Collaborator collaborator = collaboratorRepository.findByCollaboratorName(username)
        if(collaborator == null){
            return new ResponseEntity<>(MakeMap("errorMsg", "Usuario inexistente"), HttpStatus.NOT_FOUND)
        }
        Map<String, Object> dto = new HashMap<>()
        //OrganizationPerson op = organizationPersonRepository.findAll().stream().filter(op -> op.getPerson().getName() == username).collect(Collectors.toList()).first()
        OrganizationPerson op = organizationPersonRepository.findByPerson(collaborator)
        Organization org = op.getOrganization()
        dto.put("orgId", org.getId())
        Boolean userFound = (org.getOrgPersons().find(x -> x.getPerson().getName() == username) != null)
        if(!userFound){
            return new ResponseEntity<>(["errorMsg": "Usuario no encontrado en ninguna organización"] as Map<String, Object>, HttpStatus.FORBIDDEN)
        } else {
            return new ResponseEntity<>(dto, HttpStatus.ACCEPTED)
        }
    }

    // Find new user sign up
    @RequestMapping(path = "/findNewCollaborator/{name}", method = RequestMethod.GET)
    Map<String, Object> validateUserInOrg(@PathVariable String name){
        if(name.isBlank() || name === "" || name == null){
            return ["errorMsg": "Nombre Inválido"]
        }
        Map<String, Object> dto = new HashMap<String, Object>()
        Collaborator collaborator = collaboratorRepository.findByCollaboratorName(name)
        dto.put("passwordConfirmed", false)
        if(collaborator != null) {
            OrganizationPerson organizationPerson = organizationPersonRepository.findByPerson(collaborator)
            if(organizationPerson != null){
                Organization org = organizationPerson.getOrganization()
                if(org != null){
                    dto.put("userRegisteredInOrg", true)
                    dto.put("orgId", org.getId())
                    dto.put("collaboratorId", collaborator.getId())
                    dto.put("passwordConfirmed", collaborator.getPasswordConfirmed())
                } else {
                    organizationPersonRepository.delete(organizationPerson)
                    collaboratorRepository.delete(collaborator)
                    dto.put("userRegisteredInOrg", false)
                    dto.put("orgId", null)
                    dto.put("collaboratorId", null)
                }
            } else {
                collaboratorRepository.delete(collaborator)
                dto.put("userRegisteredInOrg", false)
                dto.put("orgId", null)
                dto.put("collaboratorId", null)
            }
        } else {
            dto.put("userRegisteredInOrg", false)
            dto.put("orgId", null)
            dto.put("collaboratorId", null)
        }
        return dto
    }

    // Get Organization names
    @RequestMapping(path = "/organizations/names", method = RequestMethod.GET)
    ResponseEntity<Map<String, Object>> getOrgNames(){
        Map<String, Object> dto = new HashMap<>()
        List<String> takenOrgNames = new LinkedList<>()
        organizationRepository.findAll().forEach(org -> takenOrgNames.add(org.getName()))
        dto.put("takenOrgNames", takenOrgNames)
        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED)
    }

    // Get Collaborator names
    @RequestMapping(path = "/collaborators/names", method = RequestMethod.GET)
    ResponseEntity<Map<String, Object>> getCollaboratorsNames(Authentication authentication){
        validateIsGuest(authentication)
        Map<String, Object> dto = new HashMap<>()
        List<String> takenUsernames = new LinkedList<>()
        collaboratorRepository.findAll().forEach(collaborator -> takenUsernames.add(collaborator.getName()))
        dto.put("takenUsernames", takenUsernames)
        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED)
    }

    // POST - Creation of Records
    @RequestMapping(path = "/completeSignUp", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> completeSignUp(@RequestParam Long collaboratorId, @RequestParam Long orgId, @RequestParam String password, Authentication authentication){
        validateIsGuest(authentication)
        if(collaboratorId == null || orgId == null || stringParamIsNull(password)){
            return new ResponseEntity<>(MakeMap("errorMsg", "Parámetros Inválidos"), HttpStatus.BAD_REQUEST)
        }
        Map<String, Object> dto = new HashMap<>()
        Collaborator collaborator = collaboratorRepository.findById(collaboratorId).orElse(null)
        if(collaborator != null){
            collaborator.setPassword(passwordEncoder.encode(password))
            collaboratorRepository.save(collaborator)
            Organization organization = organizationRepository.findById(orgId).orElse(null)
            dto.put("collaboratorId", collaborator.getId())
            dto.put("orgId", organization.getId())
        } else {
            return new ResponseEntity<>(MakeMap("errorMsg", "Usuario no encontrado"), HttpStatus.NOT_FOUND)
        }
        return new ResponseEntity<>(dto, HttpStatus.CREATED)
    }

    @RequestMapping(path = "/collaborators/{orgId}/create", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> createCollaborator(@PathVariable Long orgId, @RequestParam String collaboratorName, @RequestParam String password, @RequestParam Boolean passwordConfirmed, Authentication authentication){
        validateIsGuest(authentication)
        if(stringParamIsNull(collaboratorName)){
            return new ResponseEntity<>(MakeMap("errorMsg", "Nombre de Usuario inválido"), HttpStatus.BAD_REQUEST)
        }
        if(collaboratorRepository.findByCollaboratorName(collaboratorName) != null){
            return new ResponseEntity<>(MakeMap("errorMsg", "Nombre de Usuario ya existente"), HttpStatus.FORBIDDEN)
        }
        if(orgId == null || organizationRepository.findById(orgId) == null){
            return new ResponseEntity<>(MakeMap("errorMsg", "No existe la organización"), HttpStatus.FORBIDDEN)
        }
        Collaborator collaborator = new Collaborator(collaboratorName, passwordEncoder.encode(password), Profile.VOLUNTEER, passwordConfirmed)
        collaboratorRepository.save(collaborator)
        Organization organization = organizationRepository.findById(orgId).orElse(null)
        OrganizationPerson organizationPerson = new OrganizationPerson(organization, collaborator)
        organizationPersonRepository.save(organizationPerson)
        Map<String,Object> response = new LinkedHashMap<>()
        response.put("collaborator", collaborator.toDTO())
        response.put("organizationPerson", organizationPerson.getId())
        return new ResponseEntity<>(response, HttpStatus.CREATED)
    }

    @RequestMapping(path = "/friends/{orgId}/create", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> createFriend(@PathVariable Long orgId, @RequestParam String friendName, Authentication authentication){
        validateIsGuest(authentication)
        if(stringParamIsNull(friendName)){
            return new ResponseEntity<>(MakeMap("errorMsg", "Nombre buscado inválido"), HttpStatus.BAD_REQUEST)
        }
        if(friendRepository.findByFullName(friendName) != null){
            return new ResponseEntity<>(MakeMap("errorMsg", "Nombre de Amigo ya existente"), HttpStatus.CONFLICT)
        }
        if(orgId == null || organizationRepository.findById(orgId) == null){
            return new ResponseEntity<>(MakeMap("errorMsg", "No existe la organización"), HttpStatus.FORBIDDEN)
        }
        Friend friend = new Friend(friendName)
        friendRepository.save(friend)
        Organization organization = organizationRepository.findById(orgId).orElse(null)
        OrganizationPerson organizationPerson = new OrganizationPerson(organization, friend)
        organizationPersonRepository.save(organizationPerson)
        Map<String,Object> response = new LinkedHashMap<>()
        response.put("friend", friend.toDTO())
        response.put("organizationPerson", organizationPerson.getId())
        return new ResponseEntity<>(response, HttpStatus.CREATED)
    }

    @RequestMapping(path = "/donations/{orgId}/create", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> createDonation(@PathVariable Long orgId, @RequestParam String donationType, @RequestParam Long donorId, @RequestParam String donationRecurrency, @RequestParam String amount, Authentication authentication){
        validateIsGuest(authentication)
        if(stringParamIsNull(donationType) || donorId == null || stringParamIsNull(donationRecurrency)){
            return new ResponseEntity<>(MakeMap("errorMsg", "Parámetros Inválidos. Completar todos los campos necesarios."), HttpStatus.BAD_REQUEST)
        }
        if(orgId == null || organizationRepository.findById(orgId) == null){
            return new ResponseEntity<>(MakeMap("errorMsg", "No existe la organización"), HttpStatus.NOT_FOUND)
        }
        Collaborator collaborator = collaboratorRepository.findById(donorId).orElse(null)
        Friend friend = friendRepository.findById(donorId).orElse(null)
        Organization org = organizationRepository.findById(orgId).orElse(null)
        if(collaborator == null && friend == null){
            return new ResponseEntity<>(MakeMap("errorMsg", "Donante Inválido"), HttpStatus.NOT_FOUND)
        }
        if(!(DonationType.values().contains(donationType.toUpperCase() as DonationType))){
            return new ResponseEntity<>(MakeMap("errorMsg", "Tipo de Donación Inválido"), HttpStatus.BAD_REQUEST)
        }
        if(!(DonationRecurrency.values().contains(donationRecurrency.toUpperCase() as DonationRecurrency)) && (DonationType.MONETARY != (donationType.toUpperCase() as DonationType))){
            return new ResponseEntity<>(MakeMap("errorMsg", "Tipo de Recurrencia Inválido"), HttpStatus.BAD_REQUEST)
        }
        if(stringParamIsNull(amount) && (DonationType.MONETARY == (donationType.toUpperCase() as DonationType))){
            return new ResponseEntity<>(MakeMap("errorMsg", "Monto Inválido"), HttpStatus.FORBIDDEN)
        }
        BigDecimal amountBigDecimal = new BigDecimal(amount)
        if(amountBigDecimal <= 0 && (DonationType.MONETARY == (donationType.toUpperCase() as DonationType))){
            return new ResponseEntity<>(MakeMap("errorMsg", "Monto Inválido"), HttpStatus.FORBIDDEN)
        }
        if(amountBigDecimal != 0 && (DonationType.MONETARY != (donationType.toUpperCase() as DonationType))){
            return new ResponseEntity<>(MakeMap("errorMsg", "Monto Inválido"), HttpStatus.FORBIDDEN)
        }
        Person donor = (collaborator == null) ? friend : collaborator
        OrganizationPerson organizationPerson = organizationPersonRepository.findByPerson(donor)
        DonationType type = donationType.toUpperCase() as DonationType
        DonationRecurrency recurrency = donationRecurrency.toUpperCase() as DonationRecurrency
        Long donationNumber = donationRepository.findAll().stream().count() + 1
        Donation donation = donationRepository.save(new Donation(recurrency, type, amountBigDecimal, donationNumber))
        donation.setOrganizationPerson(organizationPerson)
        PersonDonation personDonation = personDonationRepository.save(new PersonDonation(donor, donation))
        organizationDonationRepository.save(new OrganizationDonation(org, donation))
        Map<String,Object> response = new LinkedHashMap<>()
        if((DonationType.MONETARY == (donationType.toUpperCase() as DonationType))){
            donation.setStatus(DonationStatus.PENDING)
        } else {
            donation.setStatus(DonationStatus.NOT_APPLICABLE)
        }
        Set<PersonDonation> personDonationSet = new HashSet<PersonDonation>()
        personDonationSet.add(personDonation)
        donation.setPersonDonation(personDonationSet)
        donationRepository.save(donation)
        response.put("personDonationId", personDonation.getId())
        response.put("donationId", donation.getId())
        return new ResponseEntity<>(response, HttpStatus.CREATED)
    }

    @RequestMapping(path = "/donations/{id}/{orgId}/confirm")
    ResponseEntity<Map<String, Object>> confirmMonetaryDonation(@PathVariable Long orgId, @PathVariable Long id, Authentication authentication){
        validateIsGuest(authentication)
        if(id == null || donationRepository.findById(id) == null){
            return new ResponseEntity<>(MakeMap("errorMsg", "Donación Inexistente"), HttpStatus.BAD_GATEWAY)
        }
        if(orgId == null || organizationRepository.findById(orgId) == null){
            return new ResponseEntity<>(MakeMap("errorMsg", "No existe la organización"), HttpStatus.BAD_GATEWAY)
        }
        Donation donation = donationRepository.findById(id).orElse(null)
        if(donation.getDonationType() != DonationType.MONETARY){
            return new ResponseEntity<>(MakeMap("errorMsg", "Operación no válida para este tipo de donación"), HttpStatus.FORBIDDEN)
        }
        donation.setStatus(DonationStatus.ACTIVE)
        donationRepository.save(donation)
        Person donor = donation.getPersonDonation().first().getPerson()
        Integer txnNumber = transactionRepository.findAll().toList().size()
        Transaction transaction = new Transaction(amountBigDecimal, TransactionType.RECEIPT, txnNumber)
        transaction.setOrganizationPerson(organizationPerson)
        transactionRepository.save(transaction)
        organizationTransactionRepository.save(new OrganizationTransaction(org, transaction))
        response.put("txnId", transaction.getId())
        PersonTransaction personTransaction = new PersonTransaction(donor, transaction)
        personTransactionRepository.save(personTransaction)
        Set<PersonTransaction> personTxnSet = new HashSet<PersonTransaction>()
        personTxnSet.add(personTransaction)
        transaction.setPersonTransaction(personTxnSet)
        transactionRepository.save(transaction)
        BigDecimal orgBalance = org.getBalance()
        org.setBalance(amountBigDecimal + orgBalance)
        organizationRepository.save(org)
        transaction.updateStatus(TransactionStatus.PROCESSING)
        transactionRepository.save(transaction)
        return new ResponseEntity<>(MakeMap("errorMsg", "Success"), HttpStatus.ACCEPTED)
    }

    @RequestMapping(path = "/donations/{id}/{orgId}/cancel", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> cancelRecurrentDonation(@PathVariable Long orgId, @PathVariable Long id, Authentication authentication){
        validateIsGuest(authentication)
        if(id == null || donationRepository.findById(id) == null){
            return new ResponseEntity<>(MakeMap("errorMsg", "Donación Inexistente"), HttpStatus.BAD_GATEWAY)
        }
        if(orgId == null || organizationRepository.findById(orgId) == null){
            return new ResponseEntity<>(MakeMap("errorMsg", "No existe la organización"), HttpStatus.BAD_GATEWAY)
        }
        Donation donation = donationRepository.findById(id).orElse(null)
        donation.setStatus(DonationStatus.INACTIVE)
        donationRepository.save(donation)
        return new ResponseEntity<>(MakeMap("errorMsg", "Success"), HttpStatus.ACCEPTED)
    }

    @RequestMapping(path = "/events/{orgId}/create", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> createEvent(@PathVariable Long orgId, @RequestParam String eventName, @RequestParam String address, @RequestParam String eventType, @RequestParam String eventDate, @RequestParam String eventTimeHour, @RequestParam String eventTimeMin, Authentication authentication){
        validateIsGuest(authentication)
        if(orgId == null || stringParamIsNull(eventName) || stringParamIsNull(address) || stringParamIsNull(eventType) || stringParamIsNull(eventDate) || stringParamIsNull(eventTimeHour) || stringParamIsNull(eventTimeMin)){
            return new ResponseEntity<>(MakeMap("errorMsg", "Request inválido, algún parámetro es null"), HttpStatus.BAD_REQUEST)
        }
        if(orgId == null || organizationRepository.findById(orgId) == null){
            return new ResponseEntity<>(MakeMap("errorMsg", "No existe la organización"), HttpStatus.FORBIDDEN)
        }
        if(!(EventType.values().contains(eventType.toUpperCase() as EventType))){
            return new ResponseEntity<>(MakeMap("errorMsg", "Tipo de Evento Inválido"), HttpStatus.FORBIDDEN)
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        LocalDate dateOfEvent = LocalDate.parse(eventDate, formatter)
        if(!(dateOfEvent.isAfter(LocalDate.now()))){
            return new ResponseEntity<>(MakeMap("errorMsg", "Fecha Inválida"), HttpStatus.FORBIDDEN)
        }
        Integer eventTimeHourInt = eventTimeHour.toInteger()
        Integer eventTimeMinInt = eventTimeMin.toInteger()
        Organization organization = organizationRepository.findById(orgId).orElse(null)
        Integer eventNumber = eventRepository.findAll().toList().size()
        EventType typeOfEvent = eventType.toUpperCase() as EventType
        LocalTime eventTime = LocalTime.of(eventTimeHourInt, eventTimeMinInt)
        Event event = new Event(eventName, address, typeOfEvent, dateOfEvent, eventTime, eventNumber)
        eventRepository.save(event)
        organizationEventsRepository.save(new OrganizationEvent(organization, event))
        Map<String, Object> response = new HashMap<>()
        response.put("eventId", event.getId())
        return new ResponseEntity<>(response, HttpStatus.CREATED)
    }

    @RequestMapping(path = "/transactions/{orgId}/create", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> createTransaction(@PathVariable Long orgId, @RequestParam String transactionType, @RequestParam Long amount, @RequestParam Long transactionEntity, Authentication authentication){
        validateIsGuest(authentication)
        if(orgId == null || stringParamIsNull(transactionType) || amount == null || transactionEntity == null){
            return new ResponseEntity<>(MakeMap("errorMsg", "Request inválido, algún parámetro es null"), HttpStatus.BAD_REQUEST)
        }
        if(!(TransactionType.values().contains(transactionType.toUpperCase() as TransactionType))){
            return new ResponseEntity<>(MakeMap("errorMsg", "Tipo de Transacción Inválido"), HttpStatus.FORBIDDEN)
        }
        BigDecimal amountBigDecimal = new BigDecimal(amount)
        if(amountBigDecimal <= 0){
            return new ResponseEntity<>(MakeMap("errorMsg", "Monto Inválido"), HttpStatus.FORBIDDEN)
        }
        if(orgId == null || organizationRepository.findById(orgId) == null){
            return new ResponseEntity<>(MakeMap("errorMsg", "No existe la organización"), HttpStatus.FORBIDDEN)
        }
        Organization organization = organizationRepository.findById(orgId).orElse(null)
        Long orgAdminId = organization.getOrgAdmins().first().get('id') as Long
        Person orgAdmin = collaboratorRepository.findById(orgAdminId).orElse(null)
        OrganizationPerson organizationPerson = organizationPersonRepository.findByPerson(orgAdmin)
        TransactionType txnType = transactionType.toUpperCase() as TransactionType
        Integer txnNumber = transactionRepository.findAll().toList().size()
        Transaction transaction = new Transaction(amountBigDecimal, txnType, txnNumber)
        transaction.setOrganizationPerson(organizationPerson)
        transactionRepository.save(transaction)
        organizationTransactionRepository.save(new OrganizationTransaction(organization, transaction))
        PersonTransaction personTxn = new PersonTransaction(orgAdmin, transaction)
        personTransactionRepository.save(personTxn)
        Set<PersonTransaction> personTxnSet = new HashSet<PersonTransaction>()
        personTxnSet.add(personTxn)
        transaction.setPersonTransaction(personTxnSet)
        transactionRepository.save(transaction)
        if((txnType == TransactionType.PURCHASE) || (txnType == TransactionType.PAYMENT)) {
            if (organization.getBalance() < amountBigDecimal) {
                if(txnType == TransactionType.PAYMENT) {
                    Transaction transactionDebt = transactionRepository.save(new Transaction((amountBigDecimal - organization.getBalance()), TransactionType.DEBT, txnNumber))
                    transaction.updateStatus(TransactionStatus.ACCEPTED)
                    transactionRepository.save(transaction)
                    organizationTransactionRepository.save(new OrganizationTransaction(organization, transactionDebt))
                    organization.setBalance(0 as BigDecimal)
                    organizationRepository.save(organization)
                } else if(txnType == TransactionType.PURCHASE){
                    transaction.updateStatus(TransactionStatus.DENIED)
                    transactionRepository.save(transaction)
                }
            } else {
                organization.setBalance(amountBigDecimal - organization.getBalance())
                organizationRepository.save(organization)
                transaction.updateStatus(TransactionStatus.ACCEPTED)
                transactionRepository.save(transaction)
            }
        } else if((txnType == TransactionType.RECEIPT) || (txnType == TransactionType.SALE)){
            organization.setBalance(organization.getBalance() + amountBigDecimal)
            organizationRepository.save(organization)
            transaction.updateStatus(TransactionStatus.ACCEPTED)
            transactionRepository.save(transaction)
        }
        Map<String, Object> response = new HashMap<>()
        response.put("transactionId", transaction.getId())
        return new ResponseEntity<>(response, HttpStatus.CREATED)
    }

    @RequestMapping(path = "/organizations/create", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> createOrganization(@RequestParam String orgName, @RequestParam String adminUserName, @RequestParam String adminPassword, Authentication authentication){
        validateIsGuest(authentication)
        if(stringParamIsNull(orgName)){
            return new ResponseEntity<>(MakeMap("errorMsg", "Nombre de Organización inválido"), HttpStatus.BAD_REQUEST)
        }
        if(organizationRepository.findByName(orgName) != null){
            return new ResponseEntity<>(MakeMap("errorMsg", "Nombre de Organización ya existente"), HttpStatus.FORBIDDEN)
        }
        if(stringParamIsNull(adminUserName) || stringParamIsNull(adminPassword)){
            return new ResponseEntity<>(MakeMap("errorMsg", "Usuario Administrador inválido"), HttpStatus.BAD_REQUEST)
        }
        Collaborator admin = new Collaborator(adminUserName, passwordEncoder.encode(adminPassword), Profile.SYSADMIN, true)
        collaboratorRepository.save(admin)
        Organization organization = new Organization(orgName)
        organizationRepository.save(organization)
        organizationPersonRepository.save(new OrganizationPerson(organization, admin))
        Map<String,Object> response = new LinkedHashMap<>()
        response.put("orgId", organization.getId())
        response.put("organization", organization.toDTO())
        response.put("admin", admin.toDTO())
        return new ResponseEntity<>(response, HttpStatus.CREATED)
    }


    private static boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken
    }

    private static ResponseEntity<Map<String, Object>> validateIsGuest(Authentication authentication){
        if(isGuest(authentication)){
            return new ResponseEntity<>(MakeMap("errorMsg", "Usuario no autenticado."), HttpStatus.UNAUTHORIZED)
        }
    }

    private static boolean stringParamIsNull(String stringParam){
        return (stringParam == null || stringParam === "" || stringParam.isBlank())
    }

    private static Map<String,Object> MakeMap (String key, Object value){
        Map<String, Object> mapaCreado = new LinkedHashMap<>()
        mapaCreado.put (key, value)
        return mapaCreado
    }
}
