/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unpam.model;
import java.security.MessageDigest;

/**
 *
 * @author luthfimubarok71
 */
public class Enkripsi {
    public String hashMD5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte byteData[] = md.digest();
        StringBuilder hexString = new StringBuilder();
        for(int i=0;i<byteData.length;i++) {
        String hex=Integer.toHexString(0xff & byteData[i]); if 
      (hex.length()==1) { 
        hexString.append('0'); 
        } 
        hexString.append(hex); 
        } 
        return hexString.toString(); 
    }
}