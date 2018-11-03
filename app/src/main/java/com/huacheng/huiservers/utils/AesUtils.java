package com.huacheng.huiservers.utils;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class AesUtils {
	public static String aesEncrypt(String key, String text) {
        if (key==null || text==null) return null;
        byte[] encrypted = null;
        byte[] key128bits = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        try {
            System.arraycopy(key.getBytes(), 0, key128bits, 0, Math.min(key.getBytes().length, 16));
            //"AES/ECB/PKCS5Padding" ģʽ���
            SecretKeySpec skeySpec = new SecretKeySpec(key128bits, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            encrypted = cipher.doFinal(text.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        if(encrypted != null && text.length() < encrypted.length){
        	byte[] bb = new byte[text.length()];
        	for (int i = 0; i < bb.length; i++) {
				bb[i] = encrypted[i];
			}
        	return Base64.encodeToString(bb, Base64.URL_SAFE);
        }
        return encrypted == null ? null : Base64.encodeToString(encrypted, Base64.URL_SAFE);
    }

    public static String aesEncrypt(String text) {
        return aesEncrypt("biguiyuananjubao", text);
    }

    public static String aesDecrypt(String key, String text) {
        if (key==null || text==null) return null;
        byte[] decrypted = null;
        byte[] key128bits = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        try {
            System.arraycopy(key.getBytes(), 0, key128bits, 0, Math.min(key.getBytes().length, 8));
            SecretKeySpec skeySpec = new SecretKeySpec(key128bits, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            decrypted = cipher.doFinal(Base64.decode(text, Base64.URL_SAFE));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return decrypted == null ? null : new String(decrypted);
    }

    public static String aesDecrypt(String text) {
        return aesDecrypt("biguiyuananjubao", text);
    }
}
