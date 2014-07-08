package com.hachiyae.jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hachiyae.jpa.entity.Customer;
import com.hachiyae.jpa.repository.CustomerRepository;

@Service
public class OtherCustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CacheData cacheData;

    public List<Customer> getCustomers() {
        List<Customer> cache = cacheData.get();
        if(cache.isEmpty()){
            List<Customer> all = customerRepository.findAll();
            cacheData.put(all);
            return all;
        }
        return cache;
    }

    public int numberOfCustomers() {
        List<Customer> cache = cacheData.get();
        if(!cache.isEmpty()){
            return cache.size();
        }
        return getCustomers().size();
    }
}
