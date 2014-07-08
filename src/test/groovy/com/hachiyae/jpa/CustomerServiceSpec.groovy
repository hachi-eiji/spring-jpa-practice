package com.hachiyae.jpa

import com.hachiyae.jpa.entity.Customer
import com.hachiyae.jpa.helper.CustomerHelper
import com.hachiyae.jpa.repository.CustomerRepository
import com.hachiyae.jpa.service.CacheData
import com.hachiyae.jpa.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(locations = "classpath:spring-mysql-test.xml")
class CustomerServiceSpec extends Specification {
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CacheData cacheData;

    def "test"() {
        setup:
        def list = []
        for (int i = 0; i < 1; i++) {
            list << "data " + i;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("firstName", "あああ");
        CustomerHelper helper = new CustomerHelper();
        Customer customer = helper.createData(data);
        customerService.saveAndCache(customer)
        when:
        def actual = customerService.getCustomersFromCache()
        then:
        actual.size() == 1
        cacheData.get().size() == 1
        cleanup:
        customerRepository.deleteAll()
    }

}
