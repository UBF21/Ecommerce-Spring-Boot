package com.examen.examen_cl2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception {
        build.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests().antMatchers("/producto/listar").permitAll()
                .antMatchers("/producto/comprar","/producto/detalle-compra/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers( "/producto/editar/**", "/producto/eliminar/**", "/producto/cargarForm","/producto/guardar").hasAuthority("ADMIN")
                .and().formLogin().loginPage("/login").defaultSuccessUrl("/").and()
                .exceptionHandling().accessDeniedPage("/errores/403").and().logout().deleteCookies("JSESSIONID").clearAuthentication(true).invalidateHttpSession(true);

    }
}
