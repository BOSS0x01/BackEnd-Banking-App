package bankapp.controllers;

import bankapp.dtos.CustomerDTO;
import bankapp.exceptions.CustomerNotFoundException;
import bankapp.mappers.BankAccountMapperImp;
import bankapp.services.BankAccountService;
import bankapp.utils.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/customers")
public class CustomerRestController {

    private final BankAccountService bankAccountService;

    @GetMapping
    ResponseEntity<ApiResponse<List<CustomerDTO>>> getAllCustomers() {
        return bankAccountService.getAllCustomers();
    }

    @GetMapping("/{customerId}")
    ResponseEntity<ApiResponse<CustomerDTO>> getCustomerById(@PathVariable Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(customerId);
    }


    @PostMapping
    ResponseEntity<ApiResponse<CustomerDTO>> saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping
    ResponseEntity<ApiResponse<CustomerDTO>> updateCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.saveCustomer(customerDTO);
    }

    @DeleteMapping("{customerId}")
    ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long customerId) throws CustomerNotFoundException {
        return bankAccountService.deleteCustomer(customerId);
    }

}
