package com.inventory.librarymanagementsystem.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    public static String hashPassword(String plainText){
        return BCrypt.hashpw(plainText,BCrypt.gensalt());
    }
    public static boolean checkPassword(String plainText,String storedHash){
        return  BCrypt.checkpw(plainText,storedHash);
    }
}
