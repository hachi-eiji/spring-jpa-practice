package com.hachiyae.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

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
import org.springframework.transaction.annotation.Transactional;

import com.hachiyae.jpa.entity.Customer;
import com.hachiyae.jpa.helper.CustomerHelper;
import com.hachiyae.jpa.repository.CustomerRepository;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-mysql-test.xml")
@TransactionConfiguration
public class TestClientTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private TransactionAwareDataSourceProxy dataSourceProxy;


    @Transactional
    @Test
    public void test_isOverrideData() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("firstName", "あああ");
        CustomerHelper helper = new CustomerHelper();
        final Customer customer = helper.createData(data);
        final Customer save = customerRepository.save(customer);

        //        EntityManager entityManager = entityManagerFactory.createEntityManager();
        //        SessionImpl delegate = (SessionImpl)(entityManager.getDelegate());
        //        delegate.connection();
        //        Session session = entityManager.unwrap(Session.class);
        //        session.setDefaultReadOnly(true);
        //        Customer actual = customerRepository.findOne(save.getId());
        Customer actual = customerRepository.findOne(1L);
        assertNull(actual);
        // スレーブから引いてくるので0
        assertThat(customerRepository.count(), is(0L));
        //        Customer actual = (Customer)session.doReturningWork(new ReturningWork<Object>() {
        //            @Override
        //            public Object execute(Connection connection) throws SQLException {
        //                connection.setReadOnly(true);
        //                connection.setAutoCommit(false);
        //                PreparedStatement ps = connection.prepareStatement("select * from customer where id = 1");
        //                ResultSet resultSet = ps.executeQuery();
        //                while(resultSet.next()){
        //                    Customer customer1 = new Customer();
        //                    customer1.setId(resultSet.getInt(1));
        //                    customer1.setFirstName(resultSet.getString(2));
        //                    customer1.setLastName(resultSet.getString(3));
        //                    return customer1;
        //                }
        //                return null;
        //                //                return customerRepository.findOne(save.getId());
        //            }
        //        });

        //        Customer actual = customerRepository.findOne(save.getId());
        //        assertThat(actual.getFirstName(), is("あああ"));
        //        assertThat(actual.getLastName(), is("bar"));
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
