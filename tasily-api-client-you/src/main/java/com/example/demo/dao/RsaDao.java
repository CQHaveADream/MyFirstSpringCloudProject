package com.example.demo.dao;

import com.example.demo.domain.RSAKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

public interface RsaDao extends JpaRepository<RSAKey,Serializable> {

    @Query("select r from RSAKey r where r.id = ?1")
    RSAKey findRsaById(Integer id);

    @Transactional
    @Modifying
    @Query(value = "insert into rsa_key (id, publicKey, privateKey, versionNumber) values (?1,?2,?3,?4)",nativeQuery = true)
    void saveRSAKey(Integer id, byte[] publicKey, byte[] privateKey, int versionNumber);

    @Transactional
    @Modifying
    @Query("update RSAKey r set r.privateKey = ?1, r.publicKey = ?2 where r.id = ?3")
    void updateRsaKeyById(byte[] privateKey, byte[] publicKey, Integer id);
}
