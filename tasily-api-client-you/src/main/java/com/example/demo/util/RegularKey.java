package com.example.demo.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class RegularKey {

    //编码
    public String setBase64Encryptor(String text){
        byte [] EncodeTextArray = Base64.encodeBase64(text.getBytes());
        return new String(EncodeTextArray);
    }

    //解码
    public String getBase64Decode(String code){
        byte[] DecodeTextArray = Base64.decodeBase64(code.getBytes());
        return new String(DecodeTextArray);
    }

}
