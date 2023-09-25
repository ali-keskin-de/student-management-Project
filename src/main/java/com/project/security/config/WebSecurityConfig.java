package com.project.security.config;
import com.project.security.jwt.AuthEntryPointJwt;
import com.project.security.jwt.AuthTokenFilter;
import com.project.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //  Bu sınıfın bir yapılandırma sınıfı olduğunu belirtir.
@EnableWebSecurity // Bu sınıfın web güvenliği sağlayan bir sınıf olduğunu belirtir.
@EnableGlobalMethodSecurity(prePostEnabled = true)
//  Bu sınıfın yöntem düzeyinde güvenlik kontrolü sağlayan bir sınıf olduğunu belirtir.
//prePostEnabled parametresi, yöntemlerin öncesinde ve sonrasında güvenlik ifadeleri kullanılmasına izin verir.

@RequiredArgsConstructor // Bu sınıfın final alanları için bir kurucu metot oluşturur.
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService; // Bu bir kullanıcı detayları servisi nesnesidir. Kullanıcı bilgilerini veritabanından almak için kullanılır.
    private final AuthEntryPointJwt unauthorizedHandler; // Bu bir kimlik doğrulama giriş noktası nesnesidir. Yetkisiz erişim durumunda nasıl davranılacağını belirler.

    @Bean
    // Bu yöntemin döndürdüğü nesnenin bir bean olduğunu belirtir. Bean, Spring Boot uygulamasının yönettiği ve yeniden kullandığı bir nesnedir.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager(); // Bir kimlik doğrulama yöneticisi nesnesi döndürür. Kimlik doğrulama yöneticisi, kullanıcı kimlik doğrulaması için kullanılır.

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and() // CORS (Cross-Origin Resource Sharing) desteğini etkinleştirir. CORS, farklı kaynaklardan gelen isteklere izin vermek için kullanılır.
                .csrf().disable() // CSRF (Cross-Site Request Forgery) korumasını devre dışı bırakır. CSRF, sahte istekler göndererek kullanıcıların yetkilerini kötüye kullanmaya çalışan bir saldırı türüdür.
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and() // İstisna durumlarında unauthorizedHandler nesnesini kullanarak kimlik doğrulama giriş noktasını belirler.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() // Oturum yönetimini ayarlar. Oturum oluşturma politikasını STATELESS olarak belirler. Bu, her istek için kimlik doğrulamasının yapılması gerektiği anlamına gelir.
                .authorizeRequests().antMatchers(AUTH_WHITE_LIST).permitAll() // Yetkilendirme isteklerini ayarlar. AUTH_WHITE_LIST dizisindeki URL'ler için herkese izin verir.
                .anyRequest().authenticated(); // Diğer tüm istekler için kimliği doğrulanmış olma şartı koyar.
        http.headers().frameOptions().sameOrigin(); // HTTP başlıklarını ayarlar. Frame seçeneklerini sameOrigin olarak belirler. Bu, iframe içinde gösterilecek sayfaların aynı kaynaktan olması gerektiği anlamına gelir.
        http.authenticationProvider(authenticationProvider()); // Kimlik doğrulama sağlayıcısını ayarlar. authenticationProvider() yönteminden dönen nesneyi kullanır.
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // Filtre zincirine bir filtre ekler. authenticationJwtTokenFilter() yönteminden dönen nesneyi, UsernamePasswordAuthenticationFilter sınıfından önce ekler. Bu filtre, JWT token'ı doğrulamak için kullanılır.
        return http.build(); // HttpSecurity nesnesini inşa eder ve döndürür.
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(); // Bir AuthTokenFilter nesnesi oluşturur ve döndürür. Bu nesne, JWT token'ı doğrulamak için kullanılır.
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Bir PasswordEncoder nesnesi oluşturur ve döndürür. Bu nesne, şifreleri şifrelemek ve karşılaştırmak için kullanılır.
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); // Bir DaoAuthenticationProvider nesnesi oluşturur. Bu nesne, kullanıcı kimlik doğrulaması için kullanılır.
        authenticationProvider.setUserDetailsService(userDetailsService); // Kullanıcı detayları servisini ayarlar. userDetailsService nesnesini kullanır.
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // Şifre kodlayıcısını ayarlar. passwordEncoder() yönteminden dönen nesneyi kullanır.
        return authenticationProvider; // DaoAuthenticationProvider nesnesini döndürür.
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() { // Bir WebMvcConfigurer nesnesi oluşturur. Bu nesne, web uygulamasının yapılandırılması için kullanılır.
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                registry.addMapping("/**") // tüm URL'lere eşleme ekler
                        .allowedOrigins("*") // bütün kaynaklara izin verir, farklı sunucular veya domainler
                        .allowedHeaders("*") // Authorization, Content-Type gibi başlıklara izin verir
                        .allowedMethods("*");// Http Methodlarına izin verir ( GET- POST - PUT - DELETE )
            }
        };
    }


    private static final String[] AUTH_WHITE_LIST = {
            "/",
            "index.html",
            "/css/**",
            "/images/**",
            "/js/**",
            "/contactMessages/save",
            "/auth/login"
    }; // Yetkilendirme gerektirmeyen URL'lerin listesi

}