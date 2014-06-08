package com.hachiyae.jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hachiyae.jpa.entity.Customer;
import com.hachiyae.jpa.repository.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CacheData cacheData;

    public CacheData getCacheData(){
        return cacheData;
    }

    public List<Customer> getCustomers() {
        List<Customer> cache = cacheData.get();
        if(cache.isEmpty()){
            List<Customer> all = customerRepository.findAll();
            cacheData.put(all);
            return all;
        }
        return cache;
    }

    public void save(Customer data){
        Customer save = customerRepository.save(data);
        cacheData.put(save);
    }

    public int numberOfCustomers() {
        List<Customer> cache = cacheData.get();
        if(!cache.isEmpty()){
            return cache.size();
        }
        return getCustomers().size();
    }
}
