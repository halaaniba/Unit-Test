package com.example.Unit.Test.repositories;


import com.example.Unit.Test.entities.PersonalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<PersonalUser, Long> {
}
