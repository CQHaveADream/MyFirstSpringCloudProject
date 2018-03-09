package com.example.demo.domain;
import javax.persistence.*;

/*
* 公钥私钥
* */

@Entity
@Table(name = "rsa_key")
public class RSAKey {

    private Integer id;
    private byte[] publicKey;
    private byte[] privateKey;
    private int versionNumber; //版本号

    public RSAKey() {
    }

    public RSAKey(Integer id, byte[] publicKey, byte[] privateKey, int versionNumber) {
        this.id = id;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.versionNumber = versionNumber;
    }

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Lob
    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    @Lob
    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    @Version
    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

}
