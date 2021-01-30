package com.example.demo.security.config;

import com.example.demo.security.JwtTokenFilter;
import com.example.demo.security.JwtTokenServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final JwtTokenServices jwtTokenServices;

    public SecurityConfig(JwtTokenServices jwtTokenServices) {
        this.jwtTokenServices = jwtTokenServices;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic().disable() //disables the HTTP Basic auth
                .csrf().disable()       //disables CSRF protection
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //disables session and cookie creation
                .and()
                .authorizeRequests()
                    .antMatchers("/auth/signin").permitAll()                         //allowed by anyone
                    .antMatchers(HttpMethod.GET, "/vehicles/**").authenticated()     // allowed only when signed in
                    .antMatchers(HttpMethod.DELETE, "/vehicles/**").hasRole("ADMIN") //allowed if signed in with ADMIN role
                    .anyRequest().denyAll()                                                       //anything else is denied
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenServices), UsernamePasswordAuthenticationFilter.class);//jwt filter
    }

    /**
     * @Bean , here: expose Spring Security's AuthenticationManager to other classes, so it can the autowired if needed
     * */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
