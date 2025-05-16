package bankapp.services;

import bankapp.dtos.CustomerDTO;
import bankapp.exceptions.CustomerNotFoundException;
import bankapp.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CustomerService {
    ResponseEntity<ApiResponse<CustomerDTO>> saveCustomer(@RequestBody CustomerDTO customerDTO);

    ResponseEntity<ApiResponse<List<CustomerDTO>>>  getAllCustomers();

    ResponseEntity<ApiResponse<CustomerDTO>> getCustomer(Long customerId) throws CustomerNotFoundException;

    ResponseEntity<ApiResponse<Void>> deleteCustomer(Long customerId) throws CustomerNotFoundException;
}
