package com.hachiyae.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.hachiyae.jpa.entity.Customer;
import com.hachiyae.jpa.helper.CustomerHelper;
import com.hachiyae.jpa.service.CustomerService;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-mysql-test.xml")
@TransactionConfiguration
public class TestClientTest {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private TransactionAwareDataSourceProxy dataSourceProxy;


    @Test
    public void test_isOverrideData() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("firstName", "あああ");
        CustomerHelper helper = new CustomerHelper();
        final Customer customer = helper.createData(data);
        customerService.save(customer);
        Customer actual = customerService.getCustomer(1L);
        long count = customerService.count();
        assertNotNull(actual);
        assertThat(count, is(1L));
    }

    @After
    public void tearDown() throws Exception {
        JdbcTestUtils.dropTables(new JdbcTemplate(dataSource), "customer");
    }

    @Autowired
    private DataSource dataSource;

    @Test
    public void test_readScriptPath() throws Exception {
        // スクリプト実行する
        executeScript("/sql/insert-test.sql");
        // 行数を数える
        int actual = JdbcTestUtils.countRowsInTable(new JdbcTemplate(dataSource), "customer");
        assertThat("size is not match", actual, is(3));
    }

    private void executeScript(String... paths) throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        for (String path : paths) {
            EncodedResource resource = new EncodedResource(new ClassPathResource(path, getClass()));
            JdbcTestUtils.executeSqlScript(jdbcTemplate, resource, false);
        }
    }
}
