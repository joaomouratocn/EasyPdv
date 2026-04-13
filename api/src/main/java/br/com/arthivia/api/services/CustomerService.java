package br.com.arthivia.api.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.arthivia.api.models.dtos.CustomerDto;
import br.com.arthivia.api.models.entities.CustomerEntity;
import br.com.arthivia.api.repositories.CustomerRepository;
import br.com.arthivia.api.util.Util;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public String createCustomer(CustomerDto customerDto){
        customerRepository.findByCpf(customerDto.cpf()).ifPresent(c -> {
            throw new RuntimeException("Customer already exists");
        });

        if(!Util.isCPF(customerDto.cpf())){
            throw new RuntimeException("Cpf is not valid");
        }

        var newCustomer = new CustomerEntity(customerDto);
        customerRepository.save(newCustomer);
        return "Customer saved successfully";
    }

    public String updateCustomer(CustomerDto customerDto){
        var customer = customerRepository.findById(customerDto.id()).orElseThrow(() -> 
        new RuntimeException("Customer not Found!"));

        if(!Util.isCPF(customerDto.cpf())){
            throw new RuntimeException("Cpf is not valid");
        }

        customer.setName(customerDto.name());
        customer.setCpf(customerDto.cpf());
        customer.setPhone(customerDto.phone());
        
        customerRepository.save(customer);
        return "Customer updated succesfully";
    }

    public String disableCustomer(UUID id){
        customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));

        customerRepository.disableCustomer(id);

        return "Customer deleted Successfully";
    }

    public List<CustomerDto> getAll(){
        return customerRepository.findAllByActiveTrue()
        .stream()
        .map(ce -> new CustomerDto(ce.getId(), ce.getName(), ce.getCpf(), ce.getPhone())).toList();
    }
}
