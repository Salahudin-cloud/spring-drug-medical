package com.example.drugmed.service;

import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.user.UserLoginRequest;
import com.example.drugmed.dto.user.UserRegisterRequest;
import com.example.drugmed.dto.user.UserResponse;
import com.example.drugmed.entity.User;
import com.example.drugmed.repository.UserRepository;
import com.example.drugmed.utils.BCrypt;
import jakarta.servlet.http.HttpSession;
import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final HttpSession session;

    public WebResponse<Void> register(UserRegisterRequest request) {
        User users = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()))
                .roleBase(request.getRole())
                .status(User.StatusUser.ANONYMOUS)
                .unitType(request.getUnitType())
                .build();

        userRepository.save(users);

        return WebResponse.<Void>builder()
                .message("Berhasil melakukan registrasi")
                .status(HttpStatus.OK.value())
                .build();
    }

    @Transactional
    public WebResponse<Void> login(UserLoginRequest request) {
        User user = getUser(request.getUsername());

        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username atau password salah");
        }

        if (user.getStatus() == User.StatusUser.PENDING) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Akun belum di-approve oleh kepala unit");
        }
        session.setAttribute("LoggedUser", user.getUsername());

        if (user.getRoleBase() != User.RoleBase.UNIT_CHIEF) {
            user.setStatus(User.StatusUser.PENDING);
            userRepository.save(user);
            return  WebResponse.<Void>builder()
                    .message("Berhasil melakukan login, silahkan menunggu approve dari admin")
                    .status(HttpStatus.OK.value())
                    .build();
        }

        return WebResponse.<Void>builder()
                .message("Berhasil melakukan login")
                .status(HttpStatus.OK.value())
                .build();
    }

    public WebResponse<List<UserResponse>> getAllUserByUnitType(String unitType){

        String username = (String) session.getAttribute("LoggedUser");

        User user = getUser(username);
        if (user.getRoleBase() != User.RoleBase.UNIT_CHIEF){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Anda tidak memiliki akses untuk ini");
        }

        List<User> listUser = userRepository.findByUnitType(User.UnitType.valueOf(unitType));
        if (listUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data pengguna dengan unit tipe " + unitType + " tidak ditemukan");
        }

        List<UserResponse> responses = listUser.stream()
                .filter(x -> x.getRoleBase() != User.RoleBase.UNIT_CHIEF)
                .map(x -> UserResponse.builder()
                        .id(x.getId())
                        .name(x.getName())
                        .username(x.getUsername())
                        .password(x.getPassword())
                        .unit(x.getUnitType())
                        .role(x.getRoleBase())
                        .status(x.getStatus())
                        .createdAt(x.getCreatedAt())
                        .updatedAt(x.getUpdatedAt())
                        .build()
                ).toList();

        return WebResponse.<List<UserResponse>>builder()
                .message("Data unit berhasil di dapatkan")
                .status(HttpStatus.OK.value())
                .data(responses)
                .build();
    }

    public WebResponse<Void> loginApprove(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pengguna dengan id " + id + " tidak ditemukan"));

        if (user.getStatus() == User.StatusUser.APPROVED) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pengguna sudah di approve sebelumnya");
        }

        user.setStatus(User.StatusUser.APPROVED);
        userRepository.save(user);

        return WebResponse.<Void>builder()
                .message("Berhasil uprove pengguna")
                .status(HttpStatus.OK.value())
                .build();

    }

    public WebResponse<Void> getCurrent() {

        if (session.getAttribute("LoggedUser") == null) {
            return WebResponse.<Void>builder()
                    .message("No user is logged in!")
                    .build();
        }


        return WebResponse.<Void>builder()
                .message("Current user : " + session.getAttribute("LoggedUser"))
                .build();
    }


    @Transactional
    public WebResponse<Void> logout() {
        String username = (String) session.getAttribute("LoggedUser");

        if (username == null) {
            return WebResponse.<Void>builder()
                    .message("No user is logged in!")
                    .build();
        }

        User user = getUser(username);
        System.out.println("username " + user.getUsername());

        if (user.getRoleBase() != User.RoleBase.UNIT_CHIEF) {
            user.setStatus(User.StatusUser.ANONYMOUS);
            userRepository.save(user);
        }

        session.invalidate();
        return WebResponse.<Void>builder()
                .message("Berhasil Logout")
                .build();
    }


    private User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username atau password salah"));
    }

}
