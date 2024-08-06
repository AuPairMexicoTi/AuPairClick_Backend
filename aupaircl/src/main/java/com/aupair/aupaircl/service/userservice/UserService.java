package com.aupair.aupaircl.service.userservice;

import com.aupair.aupaircl.controller.mailcontroller.maildto.MailDTO;
import com.aupair.aupaircl.controller.profilecontroller.profiledto.CountryDTO;
import com.aupair.aupaircl.controller.usercontroller.userdto.UserDTO;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountry;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountryRepository;
import com.aupair.aupaircl.model.hostfamilypreferredcountry.HostFamilyPreferredCountry;
import com.aupair.aupaircl.model.hostfamilypreferredcountry.HostFamilyPreferredCountryRepository;
import com.aupair.aupaircl.model.recoverpassword.RecoverPassword;
import com.aupair.aupaircl.model.recoverpassword.RecoverPasswordRepository;
import com.aupair.aupaircl.model.rol.Rol;
import com.aupair.aupaircl.model.rol.RolRepository;
import com.aupair.aupaircl.model.user.User;
import com.aupair.aupaircl.model.user.UserRepository;
import com.aupair.aupaircl.service.mailservice.MailService;
import com.aupair.aupaircl.service.userservice.mapperuser.MapperUser;
import com.aupair.aupaircl.utils.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
private final PasswordEncoder passwordEncoder;
private static final String AuPairType= "aupair";
    private final MailService mailService;
    private final AuPairPreferredCountryRepository auPairPreferredCountryRepository;
    private final HostFamilyPreferredCountryRepository hostFamilyPreferredCountryRepository;
    private final RecoverPasswordRepository recoverPasswordRepository;

    @Autowired
public UserService(UserRepository userRepository,
                   RolRepository rolesRepository,
                   MailService mailService,HostFamilyPreferredCountryRepository hostFamilyPreferredCountryRepository,
                   AuPairPreferredCountryRepository auPairPreferredCountryRepository, PasswordEncoder passwordEncoder, RecoverPasswordRepository recoverPasswordRepository){
    this.userRepository = userRepository;
    this.rolesRepository = rolesRepository;
    this.mailService = mailService;
    this.auPairPreferredCountryRepository = auPairPreferredCountryRepository;
    this.hostFamilyPreferredCountryRepository = hostFamilyPreferredCountryRepository;
    this.passwordEncoder = passwordEncoder;
        this.recoverPasswordRepository = recoverPasswordRepository;
    }
    @Transactional(rollbackFor={SQLException.class})
    public ResponseEntity<CustomResponse> registerUser(UserDTO userDTO){
    try {
            User user = new User();
            if(this.userRepository.existsByUsername(userDTO.getUsername())){
                return ResponseEntity.ok(new CustomResponse("Nombre de usuario ya existe",400,true,null));
            }
            if(this.userRepository.existsByEmail(userDTO.getEmail())){
                return ResponseEntity.ok(new CustomResponse("El correo ya ah sido registrado",400,true,null));
            }
            Rol rol = this.rolesRepository.findByRoleName(userDTO.getIsType()).get();
            if( rol == null){
            return ResponseEntity.ok(new CustomResponse("Role invalido ",400,true,null));
            }
            if(Objects.equals(userDTO.getIsType(), AuPairType)){

                user.setEmail(userDTO.getEmail());
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                user.setRole(rol);
                user.setUsername(userDTO.getUsername());
                user.setResetToken("");
                user.setLocked(false);
                user.setResetTokenExpires(new Date());
                User userSaved= this.userRepository.save(user);
                    if(userSaved !=null){
                        this.mailService.verifyAccount(userDTO.getEmail());
                    }
                     return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse( false, HttpStatus.OK.value(), "Au pair registrado"));

            }else {

                user.setEmail(userDTO.getEmail());
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                user.setRole(rol);
                user.setUsername(userDTO.getUsername());
                user.setResetToken("");
                user.setLocked(false);
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
    public ResponseEntity<CustomResponse> getPreferencesCountryByFamily(String email){
        List<HostFamilyPreferredCountry> auPairPreferredCountry = this.hostFamilyPreferredCountryRepository.findByHostFamilyProfile_UserEmail(email);
        List<CountryDTO> countryDTOS = MapperUser.mapHostPreferredCountry(auPairPreferredCountry);
        if(!auPairPreferredCountry.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Ciudades de preferencia",200,false,countryDTOS));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Sin registros",400,false,null));
        }
    }
    @Transactional(rollbackFor={SQLException.class})
    public ResponseEntity<CustomResponse> updatePassword(UserDTO userDTO){
        try {
            Optional<User> user = this.userRepository.findByEmail(userDTO.getEmail());
            if (user.isPresent()){
                Optional<RecoverPassword> recoverPassword = this.recoverPasswordRepository.findByUser_Email(userDTO.getEmail());
                if (recoverPassword.isPresent() && recoverPassword.get().getIsVerification().equals(true)){
                    User userUpdate = user.get();
                    userUpdate.setPassword(userDTO.getPassword());
                    this.userRepository.save(userUpdate);
                    return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse( false, HttpStatus.OK.value(), "Contraseña actualizada"));
                }else{
                    log.error("No ah sido validado el codigo de recuperacion de contraseña");
                    return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse( false, HttpStatus.BAD_REQUEST.value(), "No ha sido validado el codigo de recuperacion de contraseña"));
                }
            }else{
                log.error("Usuario invalido");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse( false, HttpStatus.NOT_FOUND.value(), "Usuario invalido"));
            }
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse( false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio en el servidor"));
        }
    }
    @Transactional
    public ResponseEntity<CustomResponse> verifyRecoverPassword(MailDTO mailDTO){
        Optional<RecoverPassword> recoverPasswordSaved = this.recoverPasswordRepository.findByUser_Email(mailDTO.getEmail());
        if (recoverPasswordSaved.isPresent()) {
            if (recoverPasswordSaved.get().getIsVerification().equals(true)){
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(),"El codigo ya no es valido" ));
            }
            if (recoverPasswordSaved.get().getVerificationToken().equals(mailDTO.getCode())) {
                recoverPasswordSaved.get().setIsVerification(true);
                this.recoverPasswordRepository.saveAndFlush(recoverPasswordSaved.get());
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.OK.value(), "Código valido"));
            }else{
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Código invalido"));
            }
        }else {
            log.info("El usuario no tiene verificaciones");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.BAD_REQUEST.value(), "El usuario no tiene verificaciones"));
        }
    }
}
