package com.example.sms.repository;

import com.example.sms.model.Quota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface QuotaRepository extends JpaRepository<Quota,Integer> {

    @Query(nativeQuery = true, value = "select * from quota where branch=?1")
    Quota findByBranch(String branch);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update quota set target=?2 where id=?1")
    void updateLimit(int id, long lim);
}
