package com.example.sms.repository;


import com.example.sms.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface AdminRepository extends JpaRepository<Admin,Integer> {

    @Query(nativeQuery = true,value = "Select password from admin where email=?1")
    String validate(String email);

    @Modifying
    @Transactional
    @Query(nativeQuery = true , value = "update admin set password=?2 where email=?1")
    void changePass(String email, String newPass);
}
