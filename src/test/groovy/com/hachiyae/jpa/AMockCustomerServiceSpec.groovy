package com.hachiyae.jpa

import com.hachiyae.jpa.entity.Customer
import com.hachiyae.jpa.helper.CustomerHelper
import com.hachiyae.jpa.repository.CustomerRepository
import com.hachiyae.jpa.service.CacheData
import com.hachiyae.jpa.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

class AMockCustomerServiceSpec extends Specification {
    CustomerService customerService = new CustomerService();

    def "test"() {
        setup:
        Map<String, Object> data = new HashMap<>();
        data.put("firstName", "いいい");
        CustomerHelper helper = new CustomerHelper();
        Customer customer = helper.createData(data);
        def mock = Mock(CustomerRepository){
            findAll() >> [customer]
        }
        def cache = Mock(CacheData) {
            get() >> [customer]
        }
        customerService.metaClass.setAttribute(customerService, "customerRepository", mock)
        customerService.metaClass.setAttribute(customerService, "cacheData", cache)
        when:
        def actual = customerService.getCustomers()
        then:
        actual[0].firstName == "いいい"
        cache.get().size() == 1
    }

}
