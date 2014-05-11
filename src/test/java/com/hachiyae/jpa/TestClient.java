package com.hachiyae.jpa;

import com.hachiyae.jpa.entity.Customer;
import com.hachiyae.jpa.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-test.xml")
public class TestClient {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void test() throws Exception {
        List<Customer> all = customerRepository.findAll();
        System.out.println(all);
    }
}
