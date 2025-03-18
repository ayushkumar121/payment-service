package com.mobieslow.paymentservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobieslow.paymentservice.models.Order;
import com.mobieslow.paymentservice.models.PaymentInstrument;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

public class EncryptionUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String ALGORITHM = "AES";

    public static String encrypt(String plainText, String secret) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SecretKeySpec keySpec = getKeySpec(secret);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String cipherText, String secret) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SecretKeySpec keySpec = getKeySpec(secret);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(decryptedBytes);
    }

    private static SecretKeySpec getKeySpec(String secret) {
        byte[] keyBytes = secret.getBytes();
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static <T> Result<T, String> decryptObject(String encryptedObject, String secret, Class<T> type) throws JsonProcessingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        try {
            return Result.ok(objectMapper.readValue(EncryptionUtils.decrypt(encryptedObject, secret), type));
        } catch (Exception ex){
            return Result.err(ex.getMessage());
        }
    }

    public static void main(String[] args) throws JsonProcessingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Order order = new Order();
        order.setId("myntra_242846");
        order.setAmount(20048.45);
        order.setCustomerId("cust_395910AC");
        order.setMerchantId("merch_CA03E22E");
        order.setRedirectUrl("https://myntra.com/mobislow/payment_callback");
        order.setPaymentInstrument(PaymentInstrument.WALLET);

        ObjectMapper objectMapper = new ObjectMapper();
        String val = objectMapper.writeValueAsString(order);

        System.out.println(EncryptionUtils.encrypt(val, "fRDLIX3KfkLDWGIEBBDqN8NMLeYByLTC"));
    }
}
