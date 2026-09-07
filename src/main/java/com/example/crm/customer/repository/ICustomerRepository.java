package com.example.crm.customer.repository;

import com.example.crm.customer.domain.CustomerEntity;
import com.example.crm.customer.domain.EEntityLifeCycle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface ICustomerRepository.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */

@Repository
public interface ICustomerRepository extends JpaRepository<CustomerEntity, UUID> , JpaSpecificationExecutor<CustomerEntity> {

    /**
     * Find by id and state
     *
     * @param id the id
     * @param state the state
     * @return optional
     */
    Optional<CustomerEntity> findByIdAndState(UUID id , EEntityLifeCycle state);

    /**
     * Find all by state
     *
     * @param state the state
     * @return response
     */
    List<CustomerEntity> findAllByState(EEntityLifeCycle state);

    @Query("select c from CustomerEntity c where lower(c.firstName) like lower(concat('%', :q, '%')) or lower(c.lastName) like lower(concat('%', :q, '%')) or lower(c.companyName) like lower(concat('%', :q, '%'))")
    Page<CustomerEntity> search(@Param("q") String q, Pageable pageable);




}
