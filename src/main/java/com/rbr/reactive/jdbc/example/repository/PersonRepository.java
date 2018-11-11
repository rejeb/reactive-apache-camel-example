package com.rbr.reactive.jdbc.example.repository;

import com.rbr.reactive.jdbc.example.repository.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

}
