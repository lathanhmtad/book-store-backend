package com.app.repository;

import com.app.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {
    @Query("select r from Role r where r.name in (:names)")
    List<Role> findAllByName(@Param("names") List<String> names);
}
