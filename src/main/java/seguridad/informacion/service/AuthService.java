package seguridad.informacion.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import seguridad.informacion.entitys.Usuario;
import seguridad.informacion.repository.UsuarioRepository;
import seguridad.informacion.request.AuthResponse;
import seguridad.informacion.request.LoginRequest;

@Service
public class AuthService 
{
    private static final int MAX_FAILED_ATTEMPTS = 3;
    
    private final UsuarioRepository usuario;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UsuarioRepository usuario, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.usuario = usuario;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse login(LoginRequest request)
    {
        Usuario usua = usuario.findByUsername(request.getUsername()).orElseThrow();
        
        // Verificar si la cuenta ya esta logueada
        if (usua.getLogueado() == 1)
        {
        	System.out.println("cuenta en uso");
            throw new LockedException("Cuenta en uso.");
        }
        
        // Verificar si la cuenta está inactiva
        if (usua.getEstado().equals("Inactivo")) {
            throw new RuntimeException("Cuenta inactiva.");
        }
        
        // Verificar si la cuenta está bloqueada
        if (!usua.getAccountnonlocked()) {
            throw new LockedException("Cuenta bloqueada debido a múltiples intentos fallidos. Contacte al administrador.");
        }
        
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword()));

            usua.setIntentosfallidos(0);
            usua.setLogueado(1);
            usuario.save(usua);

            String token = jwtService.getToken((UserDetails) usua);

            String role = usua.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(token);
            authResponse.setRole(role);

            return authResponse;

        } catch (BadCredentialsException e)
        {
            int failedAttempts = usua.getIntentosfallidos() + 1;
            usua.setIntentosfallidos(failedAttempts);
            
            if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                usua.setAccountnonlocked(false);
            }
            
            usuario.save(usua);
            
            throw new BadCredentialsException("Credenciales incorrectas");
        }
    }
}