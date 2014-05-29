package com.hachiyae.jpa;

import com.hachiyae.jpa.entity.Customer;
import com.hachiyae.jpa.helper.CustomerHelper;
import com.hachiyae.jpa.helper.TestDataHelper;
import com.hachiyae.jpa.repository.CustomerRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.Theory;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-test.xml")
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class TestClient {
    @Autowired
    private CustomerRepository customerRepository;

    @After
    public void tearDown() throws Exception {
        customerRepository.deleteAll();
    }

    @Test
    public void test_isOverrideData() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("firstName", "あああ");
        CustomerHelper helper = new CustomerHelper();
        Customer customer = helper.create(data);
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
        Customer customer = helper.create(data);
        Customer save = customerRepository.save(customer);

        Customer actual = customerRepository.findOne(save.getId());
        assertThat(actual.getFirstName(), is("あああ"));
        assertNotNull(actual.getLastName());
    }
}
