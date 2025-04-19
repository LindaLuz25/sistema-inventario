package com.authserve.authproduct.service;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import com.authserve.authproduct.models.UserEntity;
import com.authserve.authproduct.repository.RoleRepository;
import com.authserve.authproduct.repository.UserRepository;



@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public UserEntity create(UserEntity user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }


    @Transactional(timeout = 10, readOnly = false)
    public boolean delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        userRepository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UserEntity getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

}
