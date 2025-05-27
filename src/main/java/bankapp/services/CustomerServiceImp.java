package bankapp.services;

import bankapp.dtos.CustomerDTO;
import bankapp.entities.*;
import bankapp.exceptions.CustomerNotFoundException;
import bankapp.mappers.CustomerMapperImp;
import bankapp.repositories.CustomerRepository;
import bankapp.utils.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CustomerServiceImp implements CustomerService {


    private final CustomerRepository customerRepository;
    private final CustomerMapperImp bankAccountMapperImp;

    @Override
    public ResponseEntity<CustomerDTO> saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = customerRepository.save(bankAccountMapperImp.toCustomer(customerDTO));
//        return  ResponseEntity.ok(new ApiResponse<>(true,"Customer saved successfully",bankAccountMapperImp.toCustomerDTO(customer))) ;
        return  ResponseEntity.ok(bankAccountMapperImp.toCustomerDTO(customer)) ;
    }

    @Override
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        Customer existingCustomer = getCustomerById(customerDTO.getId());
        Customer customer = customerRepository.save(bankAccountMapperImp.toCustomer(customerDTO,existingCustomer));
        return  ResponseEntity.ok(bankAccountMapperImp.toCustomerDTO(customer)) ;
    }

    @Override
    public ResponseEntity<List<CustomerDTO>>  getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS =customers.stream().map(bankAccountMapperImp::toCustomerDTO).toList();
        return ResponseEntity.ok(
              customerDTOS
        );
    }


    @Override
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = getCustomerById(customerId);
        return  ResponseEntity.ok(new ApiResponse<>(true,"Customer found",bankAccountMapperImp.toCustomerDTO(customer))) ;
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = getCustomerById(customerId);
        customerRepository.delete(customer);
        return ResponseEntity.ok(new ApiResponse<>(true,"Customer deleted successfully"));
    }

    @Override
    public ResponseEntity<List<CustomerDTO>> searchCustomers(String keyword) {
        List<CustomerDTO> customerDTOS = customerRepository.findByNameContains(keyword).stream().map(bankAccountMapperImp::toCustomerDTO).toList();
        return ResponseEntity.ok(customerDTOS);
    }

    private Customer getCustomerById(Long customerId) throws CustomerNotFoundException {
        return customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

}
