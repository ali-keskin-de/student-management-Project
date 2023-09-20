package com.project.service.user;


import com.project.entity.enums.RoleType;
import com.project.entity.user.UserRole;
import com.project.exception.ConflictException;
import com.project.payload.messages.ErrorMessages;
import com.project.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    // EGer bir method service katinda DTO degilde POJO dönüyorsa baska bir service'te kullanilacaktir, yani controller tarafinda bu methodun karsiligi olmayacak
    public UserRole  getUserRole(RoleType roleType){


        return userRoleRepository.findByEnumRoleEquals(roleType).orElseThrow(()->new ConflictException(ErrorMessages.ROLE_NOT_FOUND));
    }

}
