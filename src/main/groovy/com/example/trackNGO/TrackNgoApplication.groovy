package com.example.trackNGO

import com.example.trackNGO.Repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession
import javax.sql.DataSource

@SpringBootApplication
class TrackNgoApplication {

    static void main(String[] args) {
        SpringApplication.run(TrackNgoApplication.class, args)
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName -> {
            User user = userRepository.findByUserName(inputName) as User;
            if (user != null) {
                return new User(user.getUserName(), user.getPassword(),
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }
}
@EnableWebSecurity
@Configuration
class WebSecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/rest/**").hasAuthority("SysAdmin")
            .antMatchers("/web/**").permitAll()
            .antMatchers("/api/**").permitAll()
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

    private static void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false)
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)
        }
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        // configure Web security...
    }
}

