package com.example.crm.customer.service;

import com.example.crm.base.IMessage;
import com.example.crm.base.Response;
import com.example.crm.customer.domain.CustomerEntity;
import com.example.crm.customer.domain.EEntityLifeCycle;
import com.example.crm.customer.repository.ICustomerRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * The class CustomerQueryService.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */

@RequiredArgsConstructor
@Service
public class CustomerQueryService {

    /** The customer repository. */
    private final ICustomerRepository customerRepository;

    /**
     * Find customer by id and state
     *
     * @param id the id
     * @return response
     */
    public Response<CustomerEntity> findCustomerByIdAndState(UUID id , EEntityLifeCycle state){
        CustomerEntity found = customerRepository.findByIdAndState(id, state)
                .orElseThrow(()-> new ObjectNotFoundException(IMessage.INFORMATION_NOT_FOUND , String.valueOf(IMessage.INFORMATION_NOT_FOUND)));
        return new Response<>(found, IMessage.INFORMATION_FOUND);
    }



    /**
     * Find all customer by state
     *
     * @param state the state
     * @return response
     */
    public Response<List<CustomerEntity>> findAllCustomerByState(EEntityLifeCycle state){
        return new Response<>(customerRepository.findAllByState(state) , IMessage.INFORMATION_FOUND);
    }
    public Page<CustomerEntity> list(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (query == null || query.isBlank()) {
            return customerRepository.findAll(pageable);
        }
        return customerRepository.search(query, pageable);
    }



}
