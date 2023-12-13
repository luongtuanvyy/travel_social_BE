package com.app.service.serviceImpl;

import com.app.dto.AccountDto;
import com.app.entity.Account;
import com.app.mapper.AccountMapper;
import com.app.payload.response.*;
import com.app.repository.AccountRepository;
import com.app.repository.BlogRepository;
import com.app.repository.FollowRepository;
import com.app.repository.LikeRepository;
import com.app.security.TokenProvider;
import com.app.security.UserPrincipal;
import com.app.service.AuthService;
import com.app.type.EAuthProvider;
import com.app.type.ERole;
import io.jsonwebtoken.Claims;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    BlogRepository blogRepository;
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    CloudinaryService cloudinaryService;

    @Override
    public APIResponse login(Account account) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Account acc = accountRepository.findByEmail(account.getEmail()).orElse(null);
            if(acc == null){
                return APIResponse.builder().message("Username or password is incorrect").success(false).build();
            }
            if(!acc.getIsActivated()) {
                return APIResponse.builder().message("Account has been blocked").success(false).build();
            }

            String token = tokenProvider.generateToken(acc);
            AccountDto accDto = accountMapper.accountDto(acc);
            Integer id = acc.getId();
//
//            accDto.setTotalBlog(blogRepository.countByCreatedBy(acc.getEmail()));
//            accDto.setTotalLike(likeRepository.countByAccountId(id));
//            accDto.setTotalFollow(followRepository.countByAccountId(id));
            AuthResponse authResponse = new AuthResponse(token, accDto);
            return  APIResponse.builder().message("Success").success(true).data(authResponse).build();
        }catch (Exception ex){
            return APIResponse.builder().message(ex.getMessage()).success(false).build();
        }
    }
    @Override
    public APIResponse logout() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(null);
                return APIResponse.builder().message("Logout successful").success(true).build();
            } else {
                return APIResponse.builder().message("User not logged in").success(false).build();
            }
        } catch (Exception ex) {
            return APIResponse.builder().message("Logout failed").success(false).build();
        }
    }

    @Override
    public APIResponse register(Account account) {
        try {
            Account exists =  accountRepository.findByEmail(account.getEmail()).orElse(null);
            if(exists != null){
                return APIResponse.builder().message("Email is exists").success(false).build();
            }
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            accountRepository.save(account);
            return APIResponse.builder().message("Register successfully!").success(true).build();
        } catch (Exception e) {
            return new FailureAPIResponse(e.getMessage());
        }
    }

    public boolean checkExistAccount (Account account) {
        Account exists =  accountRepository.findByEmail(account.getEmail()).orElse(null);
        return exists != null;
    }

    @Override
    public APIResponse registerCompany(Account account) {
        if(checkExistAccount(account)){
            return APIResponse.builder().message("Email is exists").success(false).build();
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setRole(ERole.ROLE_COMPANY);
        accountRepository.save(account);
        return APIResponse.builder().message("Register successfully!").success(true).build();
    }

    @Override
    public APIResponse registerCompany(Account account, MultipartFile file) {
        if(checkExistAccount(account)){
            return APIResponse.builder().message("Email is exists").success(false).build();
        }
        CloudinaryResponse response = cloudinaryService.uploadFile(file, "User");
        String encoderPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encoderPassword);
        account.setCloudinaryId(response.getCloudinaryId());
        account.setAvatar(response.getUrl());
        account.setRole(ERole.ROLE_COMPANY);
        accountRepository.save(account);
        return APIResponse.builder().message("Register successfully!").success(true).build();
    }

    @Override
    public APIResponse register(Account account, MultipartFile file) {
        if(checkExistAccount(account)){
            return APIResponse.builder().message("Email is exists").success(false).build();
        }
        CloudinaryResponse response = cloudinaryService.uploadFile(file, "User");
        String encoderPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encoderPassword);
        account.setCloudinaryId(response.getCloudinaryId());
        account.setAvatar(response.getUrl());
        accountRepository.save(account);
        return APIResponse.builder().message("Register successfully!").success(true).build();
    }

    @Override
    public APIResponse updateInformation(UserPrincipal userPrincipal, Account account, MultipartFile file) {
        Account exists = accountRepository.findById(userPrincipal.getId()).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });
        exists.setIsActivated(account.getIsActivated());
        if(file != null){
            if(exists.getCloudinaryId() != null){
                cloudinaryService.deleteFile(exists.getCloudinaryId());
            }
            CloudinaryResponse response = cloudinaryService.uploadFile(file, "User");
            exists.setCloudinaryId(response.getCloudinaryId());
            exists.setAvatar(response.getUrl());
        }
        exists = accountRepository.save(exists);
        return APIResponse.builder().message("update successful").success(true).data(exists).build();
    }

    @Override
    public APIResponse googleToken(String token) throws Exception {
        String payload = token.split("\\.")[1];
        String decodedPayload = new String(Base64.decodeBase64(payload));
        JSONObject payloadJson = new JSONObject(decodedPayload);
        String name = payloadJson.getString("name");
        String picture = payloadJson.getString("picture");
        String email = payloadJson.getString("email");
        String sign_in_provider = payloadJson.getJSONObject("firebase").getString("sign_in_provider");
        String user_id = payloadJson.getString("user_id");
        Account acc = accountRepository.findByEmail(email).orElse(null);

        if(acc != null){
            System.out.println("pas"+acc.getPassword());
            try {
//                Authentication authentication = authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(acc.getEmail(), acc.getPassword())
//                );
//                SecurityContextHolder.getContext().setAuthentication(authentication);
                if(!acc.getIsActivated()) {
                    return APIResponse.builder().message("Account has been blocked").success(false).build();
                }

                String etoken = tokenProvider.generateToken(acc);
                AccountDto accDto = accountMapper.accountDto(acc);
                Integer id = acc.getId();
                accDto.setTotalBlog(blogRepository.countByCreatedBy(acc.getEmail()));
                accDto.setTotalLike(likeRepository.countByAccountId(id));
                accDto.setTotalFollow(followRepository.countByAccountId(id));
                AuthResponse authResponse = new AuthResponse(etoken, accDto);
                return  APIResponse.builder().message("Success").success(true).data(authResponse).build();
            }catch (Exception ex){
                return APIResponse.builder().message(""+ex).success(false).build();
            }
        }else {
            Account account = new Account();
            account.setEmail(email);
            account.setName(name);
            account.setAvatar(picture);
            account.setProviderId(user_id);
            if (sign_in_provider.equals("google.com")) {
                account.setProvider(EAuthProvider.google);
            } else {
                account.setProvider(EAuthProvider.facebook);
            }

            String encoderPassword = passwordEncoder.encode(user_id);
            account.setPassword(encoderPassword);
            Account accc = accountRepository.save(account);
            String etoken = tokenProvider.generateToken(accc);
            AccountDto accDto = accountMapper.accountDto(accc);
            Integer id = accc.getId();
            accDto.setTotalBlog(blogRepository.countByCreatedBy(accc.getEmail()));
            accDto.setTotalLike(likeRepository.countByAccountId(id));
            accDto.setTotalFollow(followRepository.countByAccountId(id));
            AuthResponse authResponse = new AuthResponse(etoken, accDto);
            return  APIResponse.builder().message("Success").success(true).data(authResponse).build();
        }
    }

//    @Override
//    public APIResponse getAccount(String token) {
//        Account account;
//        Claims claims =  tokenProvider.getDataFromToken(token);
//        for (Map.Entry<String, Object> entry : claims.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
//        APIResponse response = new APIResponse(claims);
//        return response;
//    }

    @Override
    public APIResponse getUserRequest(HttpServletRequest request) {
        AccountDto accDto;
        String token = getTokenFromHeader(request);
        int id;
        if (token != null){
            System.out.println(token);
            id = tokenProvider.getIdFromToken(token);
            Account account =  accountRepository.findById(id).orElse(null);
            accDto = accountMapper.accountDto(account);
        }
        else {
            return APIResponse.builder().message("You need login").success(false).build();
        }
        APIResponse response = new APIResponse(accDto);

        return response;
    }

    public String getTokenFromHeader(HttpServletRequest request) {
        // Lấy header authorization
        String authorization = request.getHeader("Authorization");

        // Kiểm tra header authorization tồn tại và có dạng Bearer <token>
        if (authorization != null && authorization.startsWith("Bearer ")) {
            // Lấy token từ header
            String token = authorization.substring(7);
            // Trả về token
            return token;
        } else {
            // Không có token
            return null;
        }
    }
}

