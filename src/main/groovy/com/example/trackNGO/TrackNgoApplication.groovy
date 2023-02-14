package com.example.trackNGO

import com.example.trackNGO.Model.Collaborator
import com.example.trackNGO.Model.Event
import com.example.trackNGO.Model.EventType
import com.example.trackNGO.Model.Organization
import com.example.trackNGO.Model.OrganizationPerson
import com.example.trackNGO.Model.OrganizationEvent
import com.example.trackNGO.Model.Profile
import com.example.trackNGO.Repositories.EventRepository
import com.example.trackNGO.Repositories.OrganizationEventRepository
import com.example.trackNGO.Repositories.OrganizationRepository
import com.example.trackNGO.Repositories.OrganizationPersonRepository
import com.example.trackNGO.Repositories.CollaboratorRepository
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
    CommandLineRunner initData(EventRepository eventRepository,
                               OrganizationEventRepository organizationEventsRepository,
                               OrganizationRepository organizationRepository,
                               OrganizationPersonRepository organizationPersonRepository,
                               CollaboratorRepository collaboratorRepository){
        (arg) -> {
            // Inicializo datos pre cargados
            Event evento1 = new Event("Evento De Prueba", "Calle Falsa 123", EventType.CHARITY, LocalDate.now().plusDays(2), LocalTime.of(04,23), 1)
            eventRepository.save(evento1)

            Organization org1 = new Organization("Organización Falsa")
            Organization org2 = new Organization("Asociación Ilícita")
            organizationRepository.save(org1)
            organizationRepository.save(org2)

            OrganizationEvent orgEvent = new OrganizationEvent(org1, evento1)
            organizationEventsRepository.save(orgEvent)

            Collaborator collaborator1 = new Collaborator("cat@pu.lta", passwordEncoder().encode("trebuchet"))
            Collaborator collaborator2 = new Collaborator("admin@admin.com", passwordEncoder().encode("admin123"))
            collaborator2.setProfile(Profile.SYSADMIN)
            collaboratorRepository.save(collaborator1)
            collaboratorRepository.save(collaborator2)

            OrganizationPerson orgPerson1 = new OrganizationPerson(org1, collaborator1)
            OrganizationPerson orgPerson2 = new OrganizationPerson(org2, collaborator2)
            organizationPersonRepository.save(orgPerson1)
            organizationPersonRepository.save(orgPerson2)
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
                //Boolean isAdmin = collaborator.profile == Profile.SYSADMIN
                //String authority = isAdmin ? "ADMIN" : "USER"
                return new User(collaborator.getName(), collaborator.getPassword(),
                        AuthorityUtils.createAuthorityList("USER"))
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
            .antMatchers("/api/logout", "/web/createTxn.html", "/web/createDonation.html", "/web/event.html*", "/web/person.html*", "/web/transaction.html*", "/web/donation.html*", "/web/mainPage.html*").hasAuthority("USER")
            .antMatchers("/api/logout", "/web/createTxn.html", "/web/createDonation.html", "/web/event.html*", "/web/person.html*", "/web/transaction.html*", "/web/donation.html*", "/web/mainPage.html*").hasAuthority("ADMIN")
            .antMatchers("/api/login").permitAll()
            .antMatchers("/api/collaborators").permitAll()
            .antMatchers("/api/organizations").permitAll()
            .antMatchers("/api/organizationCollaborators").permitAll()
            .antMatchers("/web/images/**").permitAll()
            .antMatchers("/web/styles/**").permitAll()
            .antMatchers("/web/scripts/**").permitAll()
            .antMatchers("/web/login.html").permitAll()
            .antMatchers("/web/createOrg.html").permitAll()
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

