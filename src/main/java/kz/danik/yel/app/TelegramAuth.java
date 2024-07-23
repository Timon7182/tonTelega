package kz.danik.yel.app;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("yel_TelegramAuth")
public class TelegramAuth {


    private static final Logger log = LoggerFactory.getLogger(TelegramAuth.class);

    public void authenticate(String user,
                             String chat_instance,
                             String chat_type,
                             String auth_date,
                             String hash) throws Exception {

        Map<String, String> params = new TreeMap<>();
        params.put("user", user);
        params.put("chat_instance", chat_instance);
        params.put("chat_type", chat_type);
        params.put("auth_date", auth_date);

        StringBuilder dataCheckString = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            dataCheckString.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }
        // Remove the trailing newline character
        dataCheckString.setLength(dataCheckString.length() - 1);

        // Step 2: Generate the secret key using HMAC-SHA-256 with "WebAppData" as the key
        String secretKey = hmacSha256("7062923943:AAFwL-88vIo3Us_HK-64MGsMYmQl--Nbgr8", "WebAppData");

        // Step 3: Generate the hash from the data-check-string and secret key
        String generatedHash = hmacSha256(dataCheckString.toString(), secretKey);

        log.error("secretKey: " + secretKey);
        log.error("generatedHash: " + generatedHash);
        log.error("hash " + hash);
        // Step 4: Compare the generated hash with the received hash
        if (!generatedHash.equals(hash))
            throw new Exception("Invalid data");
    }

    private String hmacSha256(String data, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hmacData = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hmacData);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}