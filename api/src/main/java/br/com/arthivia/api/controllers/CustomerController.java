package br.com.arthivia.api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.arthivia.api.models.dtos.CustomerDto;
import br.com.arthivia.api.services.CustomerService;
import br.com.arthivia.api.util.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<SuccessResponse> createCustomer(@RequestBody @Valid CustomerDto customerDto){
        var result = customerService.createCustomer(customerDto);
        return ResponseEntity.ok(new SuccessResponse(result));
        
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SuccessResponse> updateCustomer(@PathVariable @Valid UUID id, @RequestBody @Valid CustomerDto customerDto){
        var result = customerService.updateCustomer(customerDto);
        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SuccessResponse> deleteCustomer(@PathVariable @Valid UUID id){
        var result = customerService.disableCustomer(id);
        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerDto>> getAllCustomers(){
        var result = customerService.getAll();
        return ResponseEntity.ok(result);
    }
}
