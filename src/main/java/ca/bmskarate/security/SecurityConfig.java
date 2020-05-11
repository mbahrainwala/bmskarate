package ca.bmskarate.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.authentication.AuthenticationManagerBeanDefinitionParser;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(new AuthenticationManagerBeanDefinitionParser.NullAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final TokenAuthenticationFilter tokenFilter = new TokenAuthenticationFilter();

        http.csrf().disable()
        .addFilterBefore(tokenFilter, BasicAuthenticationFilter.class)
        .authorizeRequests()
        .antMatchers("/heartBeat").permitAll()
        .antMatchers("/login").permitAll()
        .antMatchers("/register").permitAll()
        .antMatchers("/forgot").permitAll()
        .antMatchers("/*.html").permitAll()
        .antMatchers("/*.css").permitAll()
        .antMatchers("/*.js").permitAll()
        .antMatchers("/api/").authenticated().and().formLogin().loginPage("/index.html");
    }
}
