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
@CrossOrigin("*")
public class CustomerRestController {

    private final CustomerService customerService;

    @GetMapping
    ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("search")
    ResponseEntity<List<CustomerDTO>> search(@RequestParam(name="keyword",defaultValue = "") String keyword) {
        return customerService.searchCustomers(keyword);
    }

    @GetMapping("/{customerId}")
    ResponseEntity<ApiResponse<CustomerDTO>> getCustomerById(@PathVariable Long customerId) throws CustomerNotFoundException {
        return customerService.getCustomer(customerId);
    }


    @PostMapping
    ResponseEntity<CustomerDTO> saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.saveCustomer(customerDTO);
    }

    @PutMapping
    ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        System.out.println(customerDTO.toString());
        return customerService.updateCustomer(customerDTO);
    }

    @DeleteMapping("{customerId}")
    ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long customerId) throws CustomerNotFoundException {
        return customerService.deleteCustomer(customerId);
    }

}
