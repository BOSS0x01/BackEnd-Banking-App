package bankapp.mappers;

import bankapp.dtos.CustomerDTO;
import bankapp.entities.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapperImp {

    public CustomerDTO toCustomerDTO (Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    public  Customer toCustomer(CustomerDTO customerDTO ) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }
    public  Customer toCustomer(CustomerDTO customerDTO , Customer existingCustomer) {
        BeanUtils.copyProperties(customerDTO, existingCustomer);
        return existingCustomer;
    }
}
