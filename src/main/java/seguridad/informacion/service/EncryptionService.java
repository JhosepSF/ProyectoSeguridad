package seguridad.informacion.service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionService 
{
    private static final String ALGORITHM = "AES";
    private final SecretKeySpec secretKey;

    public EncryptionService(String secret) 
    {
        this.secretKey = getKey(secret);
    }
    
    private SecretKeySpec getKey (String secret)
    {
        String fixedKey = secret.substring(0, Math.min(secret.length(), 16));
        while (fixedKey.length() < 16) {
            fixedKey += "0";
        }
        
        return new SecretKeySpec(fixedKey.getBytes(), ALGORITHM);
    }

    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        return new String(cipher.doFinal(decodedData));
    }
}