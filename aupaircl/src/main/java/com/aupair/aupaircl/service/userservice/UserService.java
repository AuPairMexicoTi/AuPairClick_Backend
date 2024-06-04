package com.aupair.aupaircl.service.userservice;

import com.aupair.aupaircl.controller.profilecontroller.profiledto.CountryDTO;
import com.aupair.aupaircl.controller.usercontroller.userdto.FindHostDTO;
import com.aupair.aupaircl.controller.usercontroller.userdto.UserDTO;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountry;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountryRepository;
import com.aupair.aupaircl.model.rol.Rol;
import com.aupair.aupaircl.model.rol.RolRepository;
import com.aupair.aupaircl.model.user.User;
import com.aupair.aupaircl.model.user.UserRepository;
import com.aupair.aupaircl.service.mailservice.MailService;
import com.aupair.aupaircl.service.userservice.mapperuser.MapperUser;
import com.aupair.aupaircl.utils.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

@Service
@Transactional
@Slf4j
public class UserService {
private final UserRepository userRepository;
private final RolRepository rolesRepository;
private static final String AuPairType= "aupair";
    private static final String FamilyType = "family";
    private final MailService mailService;
    private final AuPairPreferredCountryRepository auPairPreferredCountryRepository;

@Autowired
public UserService(UserRepository userRepository,
                   RolRepository rolesRepository, MailService mailService,AuPairPreferredCountryRepository auPairPreferredCountryRepository){
    this.userRepository = userRepository;
    this.rolesRepository = rolesRepository;
    this.mailService = mailService;
    this.auPairPreferredCountryRepository = auPairPreferredCountryRepository;
}
    @Transactional(rollbackFor={SQLException.class})
    public ResponseEntity<CustomResponse> registerUser(UserDTO userDTO){
    try {
            User user = new User();
            if(this.userRepository.existsByEmail(userDTO.getEmail())){
                return ResponseEntity.ok(new CustomResponse("User already exists",400,true,null));
            }
            Rol rol = this.rolesRepository.findByRoleName(userDTO.getIsType()).get();
            if( rol == null){
            return ResponseEntity.ok(new CustomResponse("Role invalido ",400,true,null));
            }
            if(Objects.equals(userDTO.getIsType(), AuPairType)){

                user.setEmail(userDTO.getEmail());
                user.setPassword(userDTO.getPassword());
                user.setRole(rol);
                user.setUsername(userDTO.getUsername());
                user.setResetToken("");
                user.setResetTokenExpires(new Date());
                User userSaved= this.userRepository.save(user);
                    if(userSaved !=null){
                        this.mailService.verifyAccount(userDTO.getEmail());
                    }
                     return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse( false, HttpStatus.OK.value(), "Au pair registrado"));

            }else {

                user.setEmail(userDTO.getEmail());
                user.setPassword(userDTO.getPassword());
                user.setRole(rol);
                user.setUsername(userDTO.getUsername());
                user.setResetToken("");
                user.setResetTokenExpires(new Date());
                User userSaved= this.userRepository.save(user);
                if(userSaved !=null){
                    this.mailService.verifyAccount(userDTO.getEmail());
                }
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse( false, HttpStatus.OK.value(), "Familia registrada"));
            }
    }catch (Exception ex){
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse( false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio en el servidor"));
    }
    }
    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> getPreferencesCountryByUser(String email){
        List<AuPairPreferredCountry> auPairPreferredCountry = this.auPairPreferredCountryRepository.findByAuPairProfile_User_Email(email);
        List<CountryDTO> countryDTOS = MapperUser.mapAuPairPreferredCountry(auPairPreferredCountry);
        if(!auPairPreferredCountry.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Ciudades de preferencia",200,false,countryDTOS));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Sin registros",400,false,null));
        }
    }
    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> findHostFamilies(FindHostDTO findHostDTO){
        try {
            return null;
        }catch (Exception e){
            log.error("Error al filtrar: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"Algo ocurrio al filtrar"));
        }
    }

}
