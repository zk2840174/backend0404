package org.zerock.backend.util;


import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {


    public String generatePassword() {

        StringBuffer buffer = new StringBuffer();

        for(int i = 0 ; i < 10; i++){

            int num = (int)(Math.random() * 10); //0 ~ 9

            buffer.append(num);

        }

        return buffer.toString();

    }

}
