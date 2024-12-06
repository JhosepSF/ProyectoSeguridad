package seguridad.informacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import seguridad.informacion.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig
{
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final AuthenticationProvider authProvider;
	
	public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationProvider authProvider) {
		super();
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.authProvider = authProvider;
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    return http
	            .csrf(csrf -> csrf.disable())
	            .cors(cors -> cors.disable())
	            .authorizeHttpRequests(authRequest -> {
	            	
	                authRequest.requestMatchers(
	                        "/css/**", "/js/**", "/img/**",
	                        "/auth/**", "/dashboard/login"
	                        ).permitAll();
	                
	                authRequest.requestMatchers(
	                		"/empresas", "/empresa/**",
	                		"/matriz", "/evento/**"
	                		).hasAnyAuthority("USUARIO","ADMINISTRADOR");
	                
	                authRequest.requestMatchers(
	                		"/dashboard/**",
	                		"/rol/**", "usuarios/**"
	                		).hasAuthority("ADMINISTRADOR");
	                
	                authRequest.anyRequest().authenticated();
	            })
	            .formLogin(login -> login	
	                .loginPage("/login")
	                .defaultSuccessUrl("/dashboard")
	                .failureUrl("/login?error=true")
	                .permitAll()
	            )
	            .sessionManagement(sessionManager -> sessionManager
	                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
	            .authenticationProvider(authProvider)
	            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
	            .build();
	}
}