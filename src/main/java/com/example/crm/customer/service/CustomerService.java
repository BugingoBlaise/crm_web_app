package com.example.crm.customer.service;

import com.example.crm.customer.domain.CustomerEntity;
import com.example.crm.customer.domain.EEntityLifeCycle;
import com.example.crm.customer.repository.ICustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * The class CustomerService.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */

@RequiredArgsConstructor
@Service
public class CustomerService {
    /**
     * The customer repository
     **/
    private final ICustomerRepository customerRepository;


    /**
     * The Customer Query service
     **/
    private final CustomerQueryService customerQueryService;

    /**
     * Create customerEntity
     *
     * @param customerEntity the customerEntity
     */
    public void createCustomer(CustomerEntity customerEntity) {
        //TODO: IMPLEMENT sequential numbering using SequenceNumberGeneratorUtil

        int min = 5;
        int max = 100;
// Generates a random number from 5 to 15 (inclusive)
        int customerNumber = (int) (Math.random() * (max - min + 1)) + min;
        customerEntity.setCustomerNumber(
                customerNumber
        );
        customerEntity.setState(EEntityLifeCycle.ACTIVE);
        customerRepository.save(customerEntity);
    }

    /**
     * Update customerEntity
     *
     * @param customerEntity the customerEntity
     */
    public void updateCustomer(CustomerEntity customerEntity) {
        CustomerEntity found = customerQueryService.findCustomerByIdAndState(customerEntity.getId(), EEntityLifeCycle.ACTIVE).getData();
        found.setEmail(customerEntity.getEmail());
        found.setNationalIdentification(customerEntity.getNationalIdentification());
        found.setFirstName(customerEntity.getFirstName());
        found.setLastName(customerEntity.getLastName());
        found.setPhoneNumber(customerEntity.getPhoneNumber());
        found.setCompanyName(customerEntity.getCompanyName());
        found.setTinNumber(customerEntity.getTinNumber());

        customerRepository.save(found);
    }
}
