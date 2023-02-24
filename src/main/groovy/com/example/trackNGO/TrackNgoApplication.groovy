package com.example.trackNGO

import com.example.trackNGO.Model.Collaborator
import com.example.trackNGO.Model.Donation
import com.example.trackNGO.Model.DonationRecurrency
import com.example.trackNGO.Model.DonationType
import com.example.trackNGO.Model.Event
import com.example.trackNGO.Model.EventType
import com.example.trackNGO.Model.Friend
import com.example.trackNGO.Model.Organization
import com.example.trackNGO.Model.OrganizationDonation
import com.example.trackNGO.Model.OrganizationPerson
import com.example.trackNGO.Model.OrganizationEvent
import com.example.trackNGO.Model.OrganizationTransaction
import com.example.trackNGO.Model.PersonDonation
import com.example.trackNGO.Model.PersonTransaction
import com.example.trackNGO.Model.Profile
import com.example.trackNGO.Model.Transaction
import com.example.trackNGO.Model.TransactionType
import com.example.trackNGO.Repositories.DonationRepository
import com.example.trackNGO.Repositories.EventRepository
import com.example.trackNGO.Repositories.FriendRepository
import com.example.trackNGO.Repositories.OrganizationDonationRepository
import com.example.trackNGO.Repositories.OrganizationEventRepository
import com.example.trackNGO.Repositories.OrganizationRepository
import com.example.trackNGO.Repositories.OrganizationPersonRepository
import com.example.trackNGO.Repositories.CollaboratorRepository
import com.example.trackNGO.Repositories.OrganizationTransactionRepository
import com.example.trackNGO.Repositories.PersonDonationRepository
import com.example.trackNGO.Repositories.PersonTransactionRepository
import com.example.trackNGO.Repositories.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler
import org.springframework.security.web.header.HeaderWriterFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession
import java.time.LocalDate
import java.time.LocalTime

@SpringBootApplication
class TrackNgoApplication {

    static void main(String[] args) {
        SpringApplication.run(TrackNgoApplication.class, args)
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    CommandLineRunner initData(CollaboratorRepository collaboratorRepository,
                               DonationRepository donationRepository,
                               EventRepository eventRepository,
                               FriendRepository friendRepository,
                               OrganizationRepository organizationRepository,
                               OrganizationEventRepository organizationEventsRepository,
                               OrganizationPersonRepository organizationPersonRepository,
                               OrganizationDonationRepository organizationDonationRepository,
                               OrganizationTransactionRepository organizationTransactionRepository,
                               PersonDonationRepository personDonationRepository,
                               PersonTransactionRepository personTransactionRepository,
                               TransactionRepository transactionRepository){
        (arg) -> {
            // Inicializo datos pre cargados
            Event evento1 = new Event("Evento De Prueba", "Calle Falsa 123", EventType.CHARITY, LocalDate.now().plusDays(2), LocalTime.of(04,23), 1)
            Event evento2 = new Event("Evento De Prueba 2", "Calle Falsa 132", EventType.CHARITY, LocalDate.now().plusDays(2), LocalTime.of(05,39), 2)
            eventRepository.save(evento1)
            eventRepository.save(evento2)

            Organization org1 = new Organization("Organización Falsa")
            Organization org2 = new Organization("Asociación Ilícita")
            organizationRepository.save(org1)
            organizationRepository.save(org2)

            OrganizationEvent orgEvent = new OrganizationEvent(org1, evento1)
            OrganizationEvent orgEvent1 = new OrganizationEvent(org2, evento2)
            organizationEventsRepository.save(orgEvent)
            organizationEventsRepository.save(orgEvent1)

            Collaborator collaborator1 = new Collaborator("cat@pu.lta", passwordEncoder().encode("trebuchet"), Profile.VOLUNTEER, true)
            Collaborator collaborator2 = new Collaborator("admin@admin.com", passwordEncoder().encode("admin123"), Profile.SYSADMIN, true)
            collaboratorRepository.save(collaborator1)
            collaboratorRepository.save(collaborator2)

            Friend friend1 = new Friend("susana@horia")
            Friend friend2 = new Friend("elmo@squito")
            friendRepository.save(friend1)
            friendRepository.save(friend2)

            OrganizationPerson orgPerson1 = new OrganizationPerson(org1, collaborator1)
            OrganizationPerson orgPerson2 = new OrganizationPerson(org2, collaborator2)
            OrganizationPerson orgPerson3 = new OrganizationPerson(org1, friend1)
            OrganizationPerson orgPerson4 = new OrganizationPerson(org2, friend2)

            organizationPersonRepository.save(orgPerson1)
            organizationPersonRepository.save(orgPerson2)
            organizationPersonRepository.save(orgPerson3)
            organizationPersonRepository.save(orgPerson4)

            Donation donation1 = new Donation(DonationRecurrency.NOT_APPLICABLE, DonationType.MATERIAL_GOODS, 0 as BigDecimal, 1)
            Donation donation2 = new Donation(DonationRecurrency.SEASONAL, DonationType.MONETARY, 1000 as BigDecimal, 2)
            Donation donation3 = new Donation(DonationRecurrency.NOT_APPLICABLE, DonationType.NON_PERISHABLE_FOOD, 0 as BigDecimal, 3)
            Donation donation4 = new Donation(DonationRecurrency.BIENNIALLY, DonationType.MONETARY, 351.24 as BigDecimal, 4)

            donationRepository.save(donation1)
            donationRepository.save(donation2)
            donationRepository.save(donation3)
            donationRepository.save(donation4)

            organizationDonationRepository.save(new OrganizationDonation(org1, donation1))
            organizationDonationRepository.save(new OrganizationDonation(org1, donation2))
            organizationDonationRepository.save(new OrganizationDonation(org2, donation3))
            organizationDonationRepository.save(new OrganizationDonation(org2, donation4))

            PersonDonation personDonation1 = new PersonDonation(friend1, donation1)
            PersonDonation personDonation2 = new PersonDonation(collaborator1, donation1)
            PersonDonation personDonation3 = new PersonDonation(friend2, donation1)
            PersonDonation personDonation4 = new PersonDonation(collaborator2, donation1)

            personDonationRepository.save(personDonation1)
            personDonationRepository.save(personDonation2)
            personDonationRepository.save(personDonation3)
            personDonationRepository.save(personDonation4)

            Set<PersonDonation> personDonationSet1 = new HashSet<PersonDonation>()
            personDonationSet1.add(personDonation1)
            donation1.setPersonDonation(personDonationSet1)
            donation1.setOrganizationPerson(orgPerson1)
            Set<PersonDonation> personDonationSet2 = new HashSet<PersonDonation>()
            personDonationSet2.add(personDonation2)
            donation2.setPersonDonation(personDonationSet2)
            donation2.setOrganizationPerson(orgPerson2)
            Set<PersonDonation> personDonationSet3 = new HashSet<PersonDonation>()
            personDonationSet3.add(personDonation3)
            donation3.setPersonDonation(personDonationSet3)
            donation3.setOrganizationPerson(orgPerson3)
            Set<PersonDonation> personDonationSet4 = new HashSet<PersonDonation>()
            personDonationSet4.add(personDonation4)
            donation4.setPersonDonation(personDonationSet4)
            donation4.setOrganizationPerson(orgPerson4)

            donation2.setReceiptGenerated()

            donationRepository.save(donation1)
            donationRepository.save(donation2)
            donationRepository.save(donation3)
            donationRepository.save(donation4)


            Transaction transaction1 = new Transaction(1000 as BigDecimal, TransactionType.RECEIPT, 1)
            Transaction transaction2 = new Transaction(300 as BigDecimal, TransactionType.PURCHASE, 2)
            //Transaction transaction3 = new Transaction(351.24 as BigDecimal, TransactionType.RECEIPT, 5)
            Transaction transaction4 = new Transaction(33212.55 as BigDecimal, TransactionType.SALE, 3)
            Transaction transaction5 = new Transaction(331.91 as BigDecimal, TransactionType.PAYMENT, 4)

            transactionRepository.save(transaction1)
            transactionRepository.save(transaction2)
            //transactionRepository.save(transaction3)
            transactionRepository.save(transaction4)
            transactionRepository.save(transaction5)

            PersonTransaction personTransaction1 = new PersonTransaction(collaborator1, transaction1)
            PersonTransaction personTransaction2 = new PersonTransaction(collaborator1, transaction2)
            //PersonTransaction personTransaction3 = new PersonTransaction(collaborator2, transaction3)
            PersonTransaction personTransaction4 = new PersonTransaction(friend2, transaction4)
            PersonTransaction personTransaction5 = new PersonTransaction(collaborator2, transaction5)

            personTransactionRepository.save(personTransaction1)
            personTransactionRepository.save(personTransaction2)
            //personTransactionRepository.save(personTransaction3)
            personTransactionRepository.save(personTransaction4)
            personTransactionRepository.save(personTransaction5)

            Set<PersonTransaction> personTxnSet1 = new HashSet<PersonTransaction>()
            personTxnSet1.add(personTransaction1)
            transaction1.setPersonTransaction(personTxnSet1)
            Set<PersonTransaction> personTxnSet2 = new HashSet<PersonTransaction>()
            personTxnSet2.add(personTransaction2)
            transaction2.setPersonTransaction(personTxnSet2)
            /*Set<PersonTransaction> personTxnSet3 = new HashSet<PersonTransaction>()
            personTxnSet3.add(personTransaction3)
            transaction3.setPersonTransaction(personTxnSet3)*/
            Set<PersonTransaction> personTxnSet4 = new HashSet<PersonTransaction>()
            personTxnSet4.add(personTransaction4)
            transaction4.setPersonTransaction(personTxnSet4)
            Set<PersonTransaction> personTxnSet5 = new HashSet<PersonTransaction>()
            personTxnSet5.add(personTransaction5)
            transaction5.setPersonTransaction(personTxnSet5)

            transactionRepository.save(transaction1)
            transactionRepository.save(transaction2)
            //transactionRepository.save(transaction3)
            transactionRepository.save(transaction4)
            transactionRepository.save(transaction5)

            organizationTransactionRepository.save(new OrganizationTransaction(org1, transaction1))
            organizationTransactionRepository.save(new OrganizationTransaction(org1, transaction2))
            //organizationTransactionRepository.save(new OrganizationTransaction(org2, transaction3))
            organizationTransactionRepository.save(new OrganizationTransaction(org2, transaction4))
            organizationTransactionRepository.save(new OrganizationTransaction(org2, transaction5))

            org1.setBalance(700 as BigDecimal)
            organizationRepository.save(org1)
            org2.setBalance(33231.88 as BigDecimal)
            organizationRepository.save(org2)
        }
    }
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    CollaboratorRepository collaboratorRepository
    @Autowired
    PasswordEncoder passwordEncoder

    @Override
    void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName -> {
            Collaborator collaborator = collaboratorRepository.findByCollaboratorName(inputName)
            if (collaborator != null) {
                Boolean isAdmin = (collaborator.getProfile() == Profile.SYSADMIN)
                String authority = (isAdmin) ? 'ADMIN' : 'USER'
                return new User(collaborator.getName(), collaborator.getPassword(),
                        AuthorityUtils.createAuthorityList(authority))
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName)
            }
        })
    }
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig{
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/rest/**").hasAuthority("ADMIN")
            .antMatchers("/api/logout", "/web/createEvent.html*", "/web/createTxn.html*", "/web/createDonation.html*", "/web/event.html*", "/web/person.html*", "/web/transaction.html*", "/web/donation.html*", "/web/mainPage.html*").hasAnyAuthority('ADMIN', 'USER')
            .antMatchers("/api/organization/**", "/api/donation/**", "/api/event/**", "/api/person/**", "/api/transaction/**", "/api/collaborator/organization", "/api/collaborators/names", "/api/collaborators/*/create", "/api/friends/*/create", "/api/donations/*/create", "/api/events/*/create", "/api/transactions/*/create", "/api/donations/*/*/cancel", "/api/transactions/debt/generate/*", "/api/donations/*/*/confirm").hasAnyAuthority('ADMIN', 'USER')
            .antMatchers("/api/login").permitAll()
            .antMatchers("/api/organizations/names").permitAll()
            .antMatchers("/api/organizations/create").permitAll()
            .antMatchers("/api/findNewCollaborator/*").permitAll()
            .antMatchers("/api/completeSignUp").permitAll()
            .antMatchers("/web/images/**").permitAll()
            .antMatchers("/web/styles/**").permitAll()
            .antMatchers("/web/scripts/**").permitAll()
            .antMatchers("/web/login.html").permitAll()
            .antMatchers("/web/createOrg.html").permitAll()
            .antMatchers("/web/signUp.html").permitAll()
            .antMatchers("/favicon.ico").permitAll()
            .anyRequest().denyAll()

        http.formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/api/login")

        http.logout().logoutUrl("/api/logout")

        // turn off checking for CSRF tokens
        http.csrf().disable()

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req))

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()) as SecurityFilterChain

        http.build()
    }

    protected static void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false)
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)
        }
    }

    /*@Bean
    WebSecurityCustomizer webSecurityCustomizer(WebSecurity web) {
    }*/
}

