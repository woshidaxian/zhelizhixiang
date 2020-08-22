package com.example.demo.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.regex.Pattern;

public class MyBCryptPasswordEncoder implements PasswordEncoder {

    private Pattern BCRYPT_PATTERN;
    private final Log logger;
    private final int strength;
    private final SecureRandom random;


    public MyBCryptPasswordEncoder() {this(-1);}

    public MyBCryptPasswordEncoder(int strength){this(strength,(SecureRandom) null);}

    public MyBCryptPasswordEncoder(int strength,SecureRandom random){
        this.BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
        this.logger = LogFactory.getLog(this.getClass());
        if (strength == -1 || strength >= 4 && strength <= 31){
            this.strength = strength;
            this.random = random;
        }else {
            throw new IllegalArgumentException("Bad strength");
        }
    }

    /**
     * 加密并返回
     * @param rawPassword
     * @return
     */
    @Override
    public String encode(CharSequence rawPassword) {
        String salt;
        if (this.strength > 0){
            if (this.random != null){
                salt = BCrypt.gensalt(this.strength,this.random);
            }else {
                salt = BCrypt.gensalt(this.strength);
            }
        }else {
            salt = BCrypt.gensalt();
        }
        return BCrypt.hashpw(rawPassword.toString(),salt);
    }

    /**
     * 比较两个是否相等
     * @param rawPassword
     * @param encodedPassword
     * @return
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (Objects.equals(rawPassword.toString(),encodedPassword)){
            return true;
        }
        if (encodedPassword != null && encodedPassword.length() != 0){
            if (!this.BCRYPT_PATTERN.matcher(encodedPassword).matches()){
                this.logger.warn("Encord password does not look like BCrypt");
                return false;
            }else {
                return BCrypt.checkpw(rawPassword.toString(),encodedPassword);
            }
        }else {
            this.logger.warn("Empty encoded password");
            return false;
        }
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return false;
    }
}
