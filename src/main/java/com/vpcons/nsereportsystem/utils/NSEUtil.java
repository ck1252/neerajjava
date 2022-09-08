package com.vpcons.nsereportsystem.utils;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


@Component
public class NSEUtil {
    public static byte[] compress(String str) {
        String outStr = "";
        ByteArrayOutputStream obj = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(obj);

            gzip.write(str.getBytes("UTF-8"));
            gzip.close();
            return obj.toByteArray();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decompress(byte[] contentBytes){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            IOUtils.copy(new GZIPInputStream(new ByteArrayInputStream(contentBytes)), out);
        } catch(IOException e){
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }

    public String getAPIPassword(String password, String secret) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            byte[] keyByteArray = Base64.getDecoder().decode(secret.getBytes("UTF-8"));
            SecretKeySpec secretkeySpec = new SecretKeySpec(keyByteArray, "AES");
            Cipher cipher = Cipher.getInstance("aes/ecb/pkcs5padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretkeySpec);
            String encrypt =Base64.getEncoder().encodeToString(cipher.doFinal(password.getBytes()));
        return  encrypt;
    }
}
