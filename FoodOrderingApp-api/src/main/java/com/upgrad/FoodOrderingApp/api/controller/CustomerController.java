package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.PasswordCryptographyProvider;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService sup;
    @Autowired
    private PasswordCryptographyProvider pcp;

    //This is signup controller method for a customer.
    @RequestMapping(value="/customer/signup",method= RequestMethod.POST)
    public ResponseEntity<SignupCustomerResponse> signup( final SignupCustomerRequest s)throws SignUpRestrictedException {
        CustomerEntity c=new CustomerEntity();
        c.setUuid(UUID.randomUUID().toString());
        c.setFirstname(s.getFirstName());
        c.setLastname(s.getLastName());
        c.setContact_number(s.getContactNumber());
        c.setEmail(s.getEmailAddress());
        c.setPassword(s.getPassword());
        c.setSalt("1234@abc");
        CustomerEntity createdUserEntity=sup.saveCustomer(c);
        SignupCustomerResponse userResponse=new SignupCustomerResponse().id(createdUserEntity.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupCustomerResponse>(userResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value="/customer/login",method=RequestMethod.POST)
    public ResponseEntity<LoginResponse>login(@RequestHeader("authorization")final String authentication)throws AuthenticationFailedException {
        byte[] decoded= Base64.getDecoder().decode(authentication.split("Basic ")[1]);
        String decodedText=new String(decoded);
        if(decodedText.indexOf(":")==-1)
            throw new AuthenticationFailedException("ATH-003","Incorrect format of decoded customer name and password");
        String[] decodedArray=decodedText.split(":");
        Integer a = 1;
        CustomerAuthEntity userAuthTokenEntity=sup.authenticate(decodedArray[0],decodedArray[1]);


        System.out.println(userAuthTokenEntity);
        CustomerEntity user=userAuthTokenEntity.getCustomer();
        System.out.println(user);
        LoginResponse aur=new LoginResponse().id(user.getUuid())
                .emailAddress(user.getEmail()).firstName(user.getFirstname())
                .lastName(user.getLastname()).contactNumber(user.getContact_number()).message("LOGGED IN SUCCESSFULLY");
        HttpHeaders httpHeader=new HttpHeaders();
        httpHeader.add("access-token",userAuthTokenEntity.getAccessToken());
        return new ResponseEntity<LoginResponse>(aur,httpHeader,HttpStatus.OK);

    }
    @RequestMapping(value="/customer/logout",method=RequestMethod.POST)
    public ResponseEntity<LogoutResponse>logout(@RequestHeader("authorization")final String authentication)throws AuthorizationFailedException {
        String decodedText=authentication;
        CustomerAuthEntity c=sup.logout(decodedText);
        LogoutResponse lr=new LogoutResponse().id(c.getUuid()).message("LOGGED OUT SUCCESSFULLY");
        return new ResponseEntity<LogoutResponse>(lr,HttpStatus.OK);
    }
    @RequestMapping(value="/customer/password",method=RequestMethod.PUT)
    public ResponseEntity<UpdatePasswordResponse>updateCustomerPassword(UpdatePasswordRequest q,@RequestHeader("authorization")final String authentication)throws Exception {
        CustomerEntity c=sup.getCustomer(authentication);


        CustomerEntity p=sup.updateCustomerPassword(q.getOldPassword(),q.getNewPassword(),c);
        UpdatePasswordResponse lr=new UpdatePasswordResponse().id(p.getUuid()).status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdatePasswordResponse>(lr,HttpStatus.OK);
    }
}
