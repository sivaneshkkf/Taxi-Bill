package com.example.taxibill.Utils;

import android.util.Base64;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {


    public static String getTValue() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmss");
        formatDate.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        String val = "fgvbha1591558100fgvbha1591558100";
        return "fgvbha1591558100fgvbha1591558100";
    }


  /*  public byte[] encryptionUtil(String key, String iv, byte[] plainText) {
        Cipher cipher = Cipher.getInstance(“AES/GCM/NoPadding”);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), “AES”);
        GCMParameterSpec paramSpec = new GCMParameterSpec(256, iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
        return cipher.doFinal(plainText);
    }*/


   /* public byte[] encryptionUtil(String key, String iv, byte[] plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            GCMParameterSpec paramSpec = new GCMParameterSpec(256, iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
            return cipher.doFinal(plainText);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }*/

    public static String openssl_encrypt(String data) {
        String blabla = getTValue();
        try {
            String blablav = App.getInstance().myPrefs.getString("eniv");
            if (blablav.equalsIgnoreCase("")) {
                blablav = App.getInstance().blablaIv;
            }
            Cipher ciper = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(blabla.getBytes(), "AES");
            IvParameterSpec iv = new IvParameterSpec(blablav.getBytes(), 0, ciper.getBlockSize());
            // Encrypt
            ciper.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] encryptedCiperBytes = ciper.doFinal(data.getBytes());
            String enc = Base64.encodeToString(encryptedCiperBytes, Base64.NO_WRAP);
            decrypt(enc);
            return enc;
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }


    public static String decrypt(String strToDecrypt) {
        String blabla = getTValue();
        try {
            String blablav = App.getInstance().myPrefs.getString("eniv");
            if (blablav.equalsIgnoreCase("")) {
                blablav = App.getInstance().blablaIv;
            }
            SecretKeySpec key = new SecretKeySpec(blabla.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            IvParameterSpec iv = new IvParameterSpec(blablav.getBytes(), 0, cipher.getBlockSize());
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] bytes = cipher.doFinal(Base64.decode(strToDecrypt, Base64.NO_WRAP));
            String dec = new String(bytes);
            //CommonFunctions.writeAPIResponse(dec, MyApplication.getInstance());
            return dec;
        } catch (Exception e) {
            e.printStackTrace();
            return strToDecrypt;
        }

    }

    /*public static String encrypt(String strToEncrypt) {
        String secret = APPHelper.getTValue();
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return encoderfun(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }*/


    /*public static String encoderfun(byte[] decval) {
        return android.util.Base64.encodeToString(decval, android.util.Base64.DEFAULT);
    }

    public static byte[] decoderfun(String enval) {
        byte[] conVal = android.util.Base64.decode(enval, Base64.DEFAULT);
        return conVal;

    }


    private static Cipher encCipher = null;
    private static Cipher decCipher = null;

    public static String encryptR(Activity activity, String input) throws Exception {
        Cipher cipher = getEncCipher(activity);
        byte[] body = cipher.doFinal(input.toString().getBytes());
        return encoderfun(body);
    }

    public static String decryptR(Activity activity, String input) throws Exception {
        Cipher cipher = getDecCipher(activity);
        input = input.replaceAll("\\r\\n", "");
        byte[] bytes = decoderfun(input);
        String output = new String(cipher.doFinal(bytes));
        return output;
    }

    private static Cipher getEncCipher(Activity activity) throws CertificateException, IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, NoSuchProviderException {

        if (encCipher != null) {
            return encCipher;
        }

        RSAPublicKey pubkey = getRSAPublicKey(activity);

        // Obtain a RSA Cipher Object
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        // The source of randomness
        SecureRandom secureRandom = new SecureRandom();

        // Initialize the cipher for encryption
        cipher.init(Cipher.ENCRYPT_MODE, pubkey, secureRandom);

        encCipher = cipher;

        return encCipher;
    }

    private static Cipher getDecCipher(Activity activity) throws Exception {

        if (decCipher != null) {
            return decCipher;
        }

        // The source of randomness
        SecureRandom secureRandom = new SecureRandom();

        // Obtain a RSA Cipher Object
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");



        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(decoderfun(pvtKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey priv = (RSAPrivateKey) keyFactory.generatePrivate(privKeySpec);

        // Initialize the cipher for decryption
        cipher.init(Cipher.DECRYPT_MODE, priv, secureRandom);
        decCipher = cipher;

        return decCipher;
    }

    private static RSAPublicKey getRSAPublicKey(Activity activity) throws CertificateException, IOException, NoSuchProviderException {


        InputStream inStream = new ByteArrayInputStream(certData.getBytes());
        CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
        inStream.close();
        RSAPublicKey pubkey = (RSAPublicKey) cert.getPublicKey();
        return pubkey;
    }

    public static String decryptByAES(String secretKey, String cipherText) throws Exception {
        int INIT_VECTOR_LENGTH = 16;

        // Get raw encoded data
        byte[] encrypted = decoderfun(cipherText);

        // Slice initialization vector
        IvParameterSpec ivParameterSpec = new IvParameterSpec(encrypted, 0, INIT_VECTOR_LENGTH);
        // Set secret password
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        // Trying to get decrypted text
        String decryptedData = new String(
                cipher.doFinal(encrypted, INIT_VECTOR_LENGTH, encrypted.length - INIT_VECTOR_LENGTH));

        return decryptedData;
    }


    public static String getAssetStr(Activity activity, String file) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = activity.getAssets().open(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }*/


}
