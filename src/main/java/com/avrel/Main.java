package com.avrel;

import com.avrel.jpa.Student;
import com.avrel.jpa.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RestController
@RequestMapping("api/v1/customers")
public class Main {

    private final CustomerRepository customerRepository;

    public Main(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
        return args -> {
            Student aurel = (new Student(
                    "Aurel",
                    "Lungu",
                    "aurel.lungu.dev@gmail.com",
                    27
            ));

            studentRepository.save(aurel);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return this.customerRepository.findAll();
    }

    record NewCustomerRequest(
            String name,
            String email,
            Integer age
    ) {

    }

    @PostMapping
    public void addCustomer(@RequestBody NewCustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.name);
        customer.setAge(request.age);
        customer.setEmail(request.email);

        customerRepository.save(customer);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable int customerId) {
        customerRepository.deleteById(customerId);
    }

    @PutMapping("{customerId}")
    public void updateCustomer(@RequestBody NewCustomerRequest updatedCustomer, @PathVariable int customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isEmpty()) {
            return;
        }

        customer.get().setName(updatedCustomer.name);
        customer.get().setEmail(updatedCustomer.email);
        customer.get().setAge(updatedCustomer.age);

        customerRepository.save(customer.get());
    }
}
