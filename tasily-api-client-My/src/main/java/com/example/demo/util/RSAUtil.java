package com.example.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.RsaDao;
import com.example.demo.domain.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RSAUtil {

    private static final String KEY_ALGORTHM = "RSA";
    private static final String PUBLIC_KEY = "publicKey";
    private static final String PRIVATE_KEY = "privateKey";
    private static int MAX_ENCRYPT_BLOCK=117;
    private static int MAX_DECRYPT_BLOCK=128;
    private RSAKey keyPairs;

    @Autowired
    private RsaDao dao;
    @Autowired
    private RedisUtil redisUtil;


    /**
     * 初始化密钥
     * @return
     * @throws Exception
     */
    public JSONObject initKey()throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORTHM);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //公钥
        byte[] pubKey = keyPair.getPublic().getEncoded();
        //私钥
        byte[] priKey = keyPair.getPrivate().getEncoded();
        JSONObject object = new JSONObject();
        object.put(PUBLIC_KEY,pubKey);
        object.put(PRIVATE_KEY,priKey);
        return object;
    }

    /**
     * BASE64加密
     * @param key
     * @return
     * @throws Exception
     */
    public String encryptBASE64(byte[] key)throws Exception{
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * BASE64解密
     * @param key
     * @return
     * @throws Exception
     */
    public byte[] decryptBASE64(String key) throws Exception{
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * 每次服务器重启调用，每次服务器重启会更新公钥和私钥
     * @return
     * @throws Exception
     */
    @PostConstruct //init()之前，Constructor只后，调用该方法
    @Transactional
    public void getSystemRSAKeyPairs() throws Exception {
        keyPairs = dao.findRsaById(1);
        JSONObject object = this.initKey();
        byte[] publicKey = object.getBytes(PUBLIC_KEY);
        byte[] privateKey = object.getBytes(PRIVATE_KEY);
        if (keyPairs==null){//首次
            dao.saveRSAKey(1, privateKey, publicKey, 1);
        }else {
            dao.updateRsaKeyById(privateKey, publicKey, 1);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("publicKey",this.encryptBASE64(publicKey));
        map.put("privateKey",this.encryptBASE64(privateKey));
        redisUtil.PutValueForHash("RSAKey",map);
    }

    /**
     * 用公钥对数据加密加密
     * @param data   加密数据
     * @return
     * @throws Exception
     */
    public byte[] encryptByPublicKey(byte[] data, byte[] key)throws Exception{

        //取公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        /*//对数据分段加密
        int inputLen = data.length;
        List<Byte> output = new ArrayList<>();
        for (int i =0;i<inputLen;i+= MAX_ENCRYPT_BLOCK) {
            int size = inputLen - i > MAX_ENCRYPT_BLOCK ? MAX_ENCRYPT_BLOCK : inputLen - i;
            byte[] cache = new byte[size];
            System.arraycopy(data, i, cache, 0, size);
            byte[] pack = cipher.doFinal(cache);
            for (Byte b : pack) {
                output.add(b);
            }
        }
        byte [] result = new byte[output.size()];
        for (int i = 0;i<output.size();i++){
            result[i] = output.get(i);
        }*/
        return cipher.doFinal(data);
    }

    /**
     * 用私钥解密
     * @param data  加密数据
     * @return
     * @throws Exception
     */
    public byte[] decryptByPrivateKey(byte[] data, byte[] key)throws Exception{

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }



}
