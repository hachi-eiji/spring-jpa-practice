package com.hachiyae.jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hachiyae.jpa.entity.Customer;
import com.hachiyae.jpa.repository.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CacheData cacheData;

    public CacheData getCacheData() {
        return cacheData;
    }

    public List<Customer> getCustomersFromCache() {
        List<Customer> cache = cacheData.get();
        if (cache.isEmpty()) {
            List<Customer> all = customerRepository.findAll();
            cacheData.put(all);
            return all;
        }
        return cache;
    }

    @Transactional
    public Customer getCustomer(long id) {
        return customerRepository.findOne(id);
    }

    @Transactional
    public long count(){
        return customerRepository.count();
    }

    @Transactional
    public void save(Customer data) {
        Customer save = customerRepository.save(data);
    }

    public void saveAndCache(Customer data) {
        Customer save = customerRepository.save(data);
        cacheData.put(save);
    }

    public int numberOfCustomers() {
        List<Customer> cache = cacheData.get();
        if (!cache.isEmpty()) {
            return cache.size();
        }
        return getCustomersFromCache().size();
    }
}
