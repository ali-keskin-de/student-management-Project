package com.project.service.user;
import com.project.entity.user.User;
import com.project.exception.BadRequestException;
import com.project.payload.mappers.UserMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.request.authentication.LoginRequest;
import com.project.payload.request.business.UpdatePasswordRequest;
import com.project.payload.response.authentication.AuthResponse;
import com.project.payload.response.user.UserResponse;
import com.project.repository.user.UserRepository;
import com.project.security.jwt.JwtUtils;
import com.project.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    //*****************************login işlemi**************************************************

    public ResponseEntity<AuthResponse> authenticateUser(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        //validation için ilk yapılması gereken managera gitmek. domino taşını başlatan olay bu.
        //kullanıcıyı valide etmek için authentication gerek. bunu yaptık
        //user'ı pat diye authenticationManager.authenticate içine koyamıyoruz. UserPasswordAuthToken cinsinden obje istiyor
        //authentice edilirse authentication objesi oluşuyor. burda autentice olmazsa yazdığımız custom exception atılacak.
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        //valide edilen kullanıcı contexte atılıyor.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = "Bearer " + jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();//normalde object dönüyor
        //roller grantedauth şeklinde string lazım. çevireceğiz

        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)//bu method stringe çeviriyor
                .collect(Collectors.toSet());
        Optional<String> role = roles.stream().findFirst();

        AuthResponse.AuthResponseBuilder authResponse = AuthResponse.builder();//bu böyle AuthResponse.builder(); yazılıp bırakılabilir.
        //burada datatype AuthResponse.AuthResponseBuilder oluyor.
        //zincir gibi yapamam çünkü bu yapı farklı, ara class tek tek setleniyor. amaç bizim bu kullanımı görmemiz
        //// ara bir class oluşturuyor.(içindeki fieldlar setlenmemiş class)//optional da kabul ediyor
        authResponse.username(userDetails.getUsername());
        authResponse.token(token.substring(7));
        //neden böyle yaptık? Tokeni bearerile setlemek istersem öyle yaparım.
        // ön tarafa giden bearersiz olsun dersek subs yapıyoruz
        authResponse.name(userDetails.getName());
        authResponse.ssn(userDetails.getSsn());
        //role optional yapıda.o yüzden if present ile alıyoruz
        role.ifPresent(authResponse::role);//burda hala araformda, return ederken build ile tamamlanmalı.

        return ResponseEntity.ok(authResponse.build());
    }


    //*******************************find user by username********************************

//findbyusername() frontend için yazılacak.authentice edilen kullanıcının bilgilerini forntende göndereceğiz
    //jwtToken browserakayıtlı. firefox silip chromea geçersek, jwt token yoksa, frontedn app bu endpointe gelecek.
    //login olan kullanıcın bilgisinden jwt tokeni alacak.

    public UserResponse findByUsername(String username) {
        User user = userRepository.findByUsernameEquals(username);

        //dto user değişimi için mapper classlara ihtiyaç var

        return userMapper.mapUserToUserResponse(user);
    }

    //****************************update Password*********************************************
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = userRepository.findByUsernameEquals(username);
        //built in ise hiçbir şeyi değişemez,şifre de dahil
        if (Boolean.TRUE.equals(user.getBuilt_in())) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }
    }
}