package com.example.crm.customer.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * The Class Customer Entity
 * @author Blaise Mugisha
 * @version 1.0
 *
 */
@Getter
@Setter
@Entity
@Table(name = "customer_entity")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The customer number.
     */
    @Column(name = "customer_number", nullable = false, unique = true)
    private double customerNumber;

    /**
     * The national identification.
     */
    @Size(max = 100)
    @Column(name = "national_identification", nullable = true, unique = true)
    private String nationalIdentification;

    /**
     * The email.
     */
    @Column(name = "email", nullable = true, unique = true)
    private String email;

    /**
     * The phone number.
     */
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;
    /**
     * The company name
     */
    @Column(name = "company_name", nullable = true, unique = true)
    private String companyName;
    /**
     * The tin number
     */
    @Column(name = "tin_number", nullable = true, unique = true)
    private String tinNumber;

    /**
     * The first name.
     */
    @Column(name = "first_name", nullable = true)
    private String firstName;

    /**
     * The last name.
     */
    @Column(name = "last_name", nullable = true)
    private String lastName;

    /**
     * The state
     */
    @Column(name = "customer_state", nullable = true)
    @Enumerated(EnumType.STRING)
    private  EEntityLifeCycle state=EEntityLifeCycle.ACTIVE;





}
