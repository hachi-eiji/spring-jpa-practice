package com.hachiyae.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.hachiyae.jpa.entity.Customer;
import com.hachiyae.jpa.repository.CustomerRepository;
import com.hachiyae.jpa.service.CacheData;
import com.hachiyae.jpa.service.OtherCustomerService;
import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

import static mockit.Deencapsulation.*;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

public class ATestClientMockTest {
    @Tested
    private OtherCustomerService otherCustomerService;
    @Mocked
    private CustomerRepository customerRepository;
    @Mocked
    private CacheData cacheData;

    @Test
    public void test() throws Exception {
        new NonStrictExpectations() {{
            setField(otherCustomerService, customerRepository);
            setField(otherCustomerService, cacheData);

            Customer customer = new Customer();
            customer.setId(1);
            customer.setFirstName("あああ");
            customer.setLastName("いいい");
            Customer customer2 = new Customer();
            customer2.setId(3);
            customer2.setFirstName("taro");
            customer2.setLastName("bob");
            List<Customer> list = Arrays.asList(customer, customer2);
            customerRepository.findAll();
            result = list;

            cacheData.get();
            result = Arrays.asList(customer2, customer);
        }};
        List<Customer> customers = otherCustomerService.getCustomers();
        assertThat(customers.get(0).getFirstName(), is("taro"));
        assertThat(customers.get(1).getFirstName(), is("あああ"));
        assert cacheData.get().size() == 2;
    }


}
