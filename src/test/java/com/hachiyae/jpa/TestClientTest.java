package com.hachiyae.jpa;

import com.hachiyae.jpa.entity.Customer;
import com.hachiyae.jpa.helper.CustomerHelper;
import com.hachiyae.jpa.repository.CustomerRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-mysql-test.xml")
@TransactionConfiguration(defaultRollback = true)
public class TestClientTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void test_isOverrideData() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("firstName", "あああ");
        CustomerHelper helper = new CustomerHelper();
        Customer customer = helper.createData(data);
        Customer save = customerRepository.save(customer);

        Customer actual = customerRepository.findOne(save.getId());
        assertThat(actual.getFirstName(), is("あああ"));
        assertThat(actual.getLastName(), is("bar"));
    }

    @Test
    public void test_isOverrideNullData() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("firstName", "あああ");
        data.put("secondName", null);
        CustomerHelper helper = new CustomerHelper();
        Customer customer = helper.createData(data);
        Customer save = customerRepository.save(customer);

        Customer actual = customerRepository.findOne(save.getId());
        assertThat(actual.getFirstName(), is("あああ"));
        assertNotNull(actual.getLastName());
    }
}
