package com.myboard.board.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {
    /*
     * 알고리즘: AES
     * 암호화 모드: CBC (블록 연결?)
     * 패딩: PKCS5Padding
     */
    public static String algorithms = "AES/CBC/PKCS5Padding";

    // 키
    private final static String AESKey = "abcdefghabcdefgh"; // 16byte

    public String encrypt(String plainText) {
        String result = ""; // 암호화 결과 값을 담을 변수

        try {
            Cipher cipher = Cipher.getInstance(algorithms);

            // 비밀키 생성
            SecretKeySpec keySpec = new SecretKeySpec(AESKey.getBytes(), "AES");
            
            /* 초기화 벡터 iv 생성
             * 같은 평문이여도 iv를 추가하여 암호화 하면 다른 암호문을 만들 수 있다.
             */
            String iv = generateRandomKey();
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)); // ID 암호화(인코딩 설정)
            result = Base64.getEncoder().encodeToString(encrypted) + iv; // 암호화 인코딩 후 저장

            return result;
        }

        catch (Exception e) {
            e.printStackTrace();

            return result;
        }
    }

    public String decrypt(String encryptedText) {
        String result = "";
        System.out.println("encryptedText(userInfo): " + encryptedText);
        
        try {
            Cipher cipher = Cipher.getInstance(algorithms);
            // jMhAHd/tL3plV8asm5BrVw==
            // 비밀키 생성
            SecretKeySpec keySpec = new SecretKeySpec(AESKey.getBytes(), "AES");
            
            // 파싱
            String iv = encryptedText.substring(encryptedText.length()-16, encryptedText.length());
            encryptedText = encryptedText.substring(0, encryptedText.length()-16);
            
            /**
             * iv(초기화 벡터)로 spec 생성
             * 따라서 랜덤하게 생성된 iv는 미리 제공되어야 하고 공개되도 상관없음
             */
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            
            result = new String(decrypted, StandardCharsets.UTF_8);
            
            return result;
        }

        catch (Exception e) {
            e.printStackTrace();
            
            return result;
        }
    }
    
    /**
     * 랜덤 문자열 생성 함수
     * 
     * @return
     */
    private static String generateRandomKey() {
        int start = 48; // 0
        int end = 122; // z
        int length = 16;
        Random random = new Random();

        String generatedString = random.ints(start,end + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        
        return generatedString;
    }
}
