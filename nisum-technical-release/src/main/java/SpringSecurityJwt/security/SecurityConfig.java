package SpringSecurityJwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration // Le indica al contenedor de spring que esta es una clase de seguridad al momento de arrancar la aplicacion
@EnableWebSecurity // Indicamos que se activa la seguridad web en nuestra aplicacion, ademas la clase contiene la configuracion referente a la seguridad
public class SecurityConfig {
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint){
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean //verifica la informacion de los usuarios q c loguearan en nuestra app
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean// Se encarga de encriptar nuestras contraseñas
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // Incorpora el filtro de seguridad que ya tenemos creado
    JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }

/*
    @Bean // establece una cadena de filtros de seguridad en la aplicacion, se determinarán los permisos
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .exceptionHandling()//permitimos el manejo de exceptions
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)//establece un punto de entrada personalizado de authenticación, para el manejo de authenticaciones no autorizadas.
                .and()
                .sessionManagement()// permite la gestion de sessiones
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()//toda peticion http debe ser autorizada
                .requestMatchers("/api/auth/**").permitAll()// les permito a todos loguearse
                .anyRequest().authenticated()// cualquier otra petición (que no sea login) debe estar authenticada
                .and()
                .httpBasic();
        // disables the X-Frame-Options header to allow the H2 console to be displayed in an iframe
        http.headers().frameOptions().disable();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }*/
// este es el original de mysql

    @Bean // establece una cadena de filtros de seguridad en la aplicacion, se determinarán los permisos
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .exceptionHandling()//permitimos el manejo de exceptions
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)//establece un punto de entrada personalizado de authenticación, para el manejo de authenticaciones no autorizadas.
                .and()
                .sessionManagement()// permite la gestion de sessiones
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()//toda peticion http debe ser autorizada
                .requestMatchers("/doc/swagger-ui/**","swagger-ui/**", "swagger-ui**", "/v3/api-docs/**", "/v3/api-docs**").permitAll()
                .requestMatchers("/api/auth/login","/api/auth/registerAdmin","/api/auth/registerUser").permitAll()
                .requestMatchers(antMatcher( "/h2-console/**")).permitAll()
                .requestMatchers(HttpMethod.GET     ,"/api/auth/lista").hasAuthority("ADMIN")
                .anyRequest().authenticated()// cualquier otra petición (que no sea login) debe estar authenticada
                .and()
                .httpBasic();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

/*
    @Bean
    public SecurityFilterChain h2FilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/doc/swagger-ui/**").permitAll()
                        .requestMatchers("swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions().sameOrigin());


        return http.build();
    }
*/


}
