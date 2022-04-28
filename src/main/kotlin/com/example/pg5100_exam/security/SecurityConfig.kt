package com.example.pg5100_exam.security

import com.example.pg5100_exam.security.filter.CustomAuthenticationFilter
import com.example.pg5100_exam.security.filter.CustomAuthorisationFilter
import com.example.pg5100_exam.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@Configuration
class SecurityConfig(
    @Autowired private val userService: UserService,
    @Autowired private val passwordEncoder: BCryptPasswordEncoder
) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder)
    }

    override fun configure(http: HttpSecurity) {
        val authenticationFilter = CustomAuthenticationFilter(authenticationManagerBean())
        authenticationFilter.setFilterProcessesUrl("/api/login")
        http.csrf().disable()
        http.sessionManagement().disable()
        http.authorizeHttpRequests()
            .antMatchers("/api/login").permitAll()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/user/**").hasAnyAuthority("USER", "ADMIN")
            .antMatchers("/api/shelter/all-animals").hasAnyAuthority("USER", "ADMIN")
            .antMatchers(HttpMethod.GET, "/api/shelter/animal/**").hasAnyAuthority("USER", "ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/shelter/animal/**").hasAnyAuthority("ADMIN")
            .antMatchers(HttpMethod.PUT, "/api/shelter/animal/**").hasAnyAuthority("ADMIN")
            .antMatchers("/api/shelter/**").hasAuthority("ADMIN")
            .antMatchers("/api/authentication/**").hasAuthority("ADMIN")
        http.authorizeHttpRequests().anyRequest().authenticated()
        http.addFilter(authenticationFilter)
        http.addFilterBefore(CustomAuthorisationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}