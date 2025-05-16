package bankapp.controllers;

import bankapp.dtos.CustomerDTO;
import bankapp.exceptions.CustomerNotFoundException;
import bankapp.mappers.BankAccountMapperImp;
import bankapp.services.BankAccountService;
import bankapp.services.CustomerService;
import bankapp.utils.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/customers")
public class CustomerRestController {

    private final CustomerService customerService;

    @GetMapping
    ResponseEntity<ApiResponse<List<CustomerDTO>>> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerId}")
    ResponseEntity<ApiResponse<CustomerDTO>> getCustomerById(@PathVariable Long customerId) throws CustomerNotFoundException {
        return customerService.getCustomer(customerId);
    }


    @PostMapping
    ResponseEntity<ApiResponse<CustomerDTO>> saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.saveCustomer(customerDTO);
    }

    @PutMapping
    ResponseEntity<ApiResponse<CustomerDTO>> updateCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.saveCustomer(customerDTO);
    }

    @DeleteMapping("{customerId}")
    ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long customerId) throws CustomerNotFoundException {
        return customerService.deleteCustomer(customerId);
    }

}
