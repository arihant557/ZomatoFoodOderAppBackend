package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.customerAuthTokenDao;
import com.upgrad.FoodOrderingApp.service.dao.customerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
//this service is for getting customer services
public class CustomerService {
    @Autowired
    private customerDao userDao;
    @Autowired
    private customerAuthTokenDao catd;
    @Autowired
    private PasswordCryptographyProvider pcp;
    //saving customer here
    public CustomerEntity saveCustomer(CustomerEntity userEntity)throws SignUpRestrictedException{
        String aa = userEntity.getContact_number();
        System.out.println(aa);

        if(userDao.checkContact(userEntity.getContact_number()))
            throw new SignUpRestrictedException("SGR-001","This contact number is already registered! Try other contact number.");
        // System.out.println("Arihant here"); for debugging used
        Integer b = 2;
        if(!colFilled(userEntity))
            throw new SignUpRestrictedException("SGR-005","Except last name all fields should be filled");
        if(!isEmailValid(userEntity.getEmail()))
            throw new SignUpRestrictedException("SGR-002","Invalid email-id format!");
        if(!isMobileCorrect(userEntity.getContact_number()))
            throw new SignUpRestrictedException("SGR-003","Invalid contact number!");
        if(!passwordValidation(userEntity.getPassword()))
            throw new SignUpRestrictedException("SGR-004","Weak password!");
        String[] encrypted = pcp.encrypt(userEntity.getPassword());
        userEntity.setPassword(encrypted[1]);
        userEntity.setSalt(encrypted[0]);
        Integer a = 1;
        return userDao.createUser(userEntity);
    }
    public boolean colFilled(CustomerEntity c){
        if(c.getUuid().length()==0||c.getPassword().length()==0||c.getFirstname().length()==0||c.getEmail().length()==0||c.getContact_number().length()==0)
            return false;
        else
            return true;
    }
    public boolean passwordValidation(String pass){
        if(pass.matches(".*[0-9]{1,}.*") && pass.matches(".*[#@$%&*!^]{1,}.*") &&pass.matches(".*[A-Z]{1,}.*")&& pass.length()>=8 )
            return true;
        else
            return false;
    }
    public boolean isMobileCorrect(String mob){
        Pattern p = Pattern.compile("[0-9]{10}");
        if(mob.length()!=10)
            return false;
        return p.matcher(mob).matches();
    }
    public boolean isEmailValid(String email){

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
    // for getting the authenticate
    public CustomerAuthEntity authenticate(String username, String password) throws AuthenticationFailedException {
        CustomerEntity user = userDao.getUserByContact(username);
        System.out.println("Pushkar authenticated");
        if (user == null) {
            throw new AuthenticationFailedException("AUTH-001", "This contact number has not been registered!");
        } else {

            String encryptedPassword = pcp.encrypt(password, user.getSalt());
            if (user.getPassword().equals(encryptedPassword)) {

                JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
                CustomerAuthEntity userAuthTokenEntity = new CustomerAuthEntity();
                userAuthTokenEntity.setUuid(UUID.randomUUID().toString());
                userAuthTokenEntity.setCustomer(user);
                ZonedDateTime now = ZonedDateTime.now();
                ZonedDateTime expiry = now.plusHours(8);
                userAuthTokenEntity.setLogin_at(now);
                userAuthTokenEntity.setExpires_at(expiry);
                String accessToken = jwtTokenProvider.generateToken(user.getUuid(), now, expiry);
                userAuthTokenEntity.setAccessToken(accessToken);
                catd.create(userAuthTokenEntity);
                userDao.updateCustomer(user);
                //return UserAuthTokenEntity so generated
                return userAuthTokenEntity;

            } else {
                throw new AuthenticationFailedException("AUTH-002", "Invalid Credentials");

            }
        }
    }
    //for log out
    public CustomerAuthEntity logout(String token) throws AuthorizationFailedException {
        CustomerAuthEntity user = catd.getAuthTokenByAccessToken(token);
        ZonedDateTime now = ZonedDateTime.now();
        user.setLogout_at(now);
        catd.updatedCustomer(user);
        long difference = user.getExpires_at().compareTo(now);
        if (user == null)
        {
            throw new AuthorizationFailedException("AUTH-001", "Customer is not Logged in.");
        }
        else if (user.getLogout_at() == null)
        {
            throw new AuthorizationFailedException("AUTH-002", "Customer is logged out. Log in again to access this endpoint.");
        }
        //System.out.println(user.getLogout_at());

        else if (difference < 0)
        {
            throw new AuthorizationFailedException("AUTH-003", "Your session is expired. Log in again to access this endpoint.");
        }



        return user;
    }
    //for getting customer
    public CustomerEntity getCustomer(String token)throws AuthorizationFailedException{
        CustomerAuthEntity c=catd.getAuthTokenByAccessToken(token);
        //Integer a  = 1;
        if(c==null)
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
        // c=logout(c.getAccessToken());
        //System.out.println("Jai mata di");
        return c.getCustomer();
    }
    public CustomerEntity updateCustomerPassword(String o,String n,CustomerEntity c)throws UpdateCustomerException {
        String encryptedPassword = pcp.encrypt(o, c.getSalt());


        String newpass=pcp.encrypt(n,c.getSalt());
        //System.out.println("HOOOOOOOOOOOOOOOOOO "+c.getPassword()+" "+o);
        if(o.length()==0||n.length()==0)
            throw new UpdateCustomerException("UCR-003","No field should be empty");
        if(!passwordValidation(n))
            throw new UpdateCustomerException("UCR-001","Weak password!");
        if(!c.getPassword().equals(encryptedPassword))
            throw new UpdateCustomerException("UCR-004","Incorrect old password!");
        c.setPassword(newpass);
        userDao.updateCustomer(c);

        return c;
    }
    public CustomerEntity getCustomerByUUid(String id){
        return userDao.getCustomerByUUid(id);
    }

}
