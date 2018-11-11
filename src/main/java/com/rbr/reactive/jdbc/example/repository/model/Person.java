package com.rbr.reactive.jdbc.example.repository.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Entity
@Table(name = "person")
public class Person {

    @Id
    @Column
    private final UUID id;

    @Column(name = "first_name")
    private final String firstName;

    @Column(name = "date_of_birth")
    private final LocalDateTime dateOfBirth;



    @Column(name = "last_name")
    private final String lastName;

    @Column
    private final String gender;

    private Person() {
        this.id = null;
        this.firstName = null;
        this.dateOfBirth = null;
        this.lastName = null;
        this.gender = null;
    }
}
