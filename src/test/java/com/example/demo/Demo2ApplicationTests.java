package com.example.demo;

import com.example.demo.core.domain.User;
import com.example.demo.core.repo.UserRepository;
import com.example.demo.integ.domain.Product;
import com.example.demo.integ.repo.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class Demo2ApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void contextLoads() {
        User newUser = new User();
        newUser.setId(1);
        newUser.setEmail("tmb@tmb.com");
        newUser.setAge(26);
        newUser.setName("taleb");
        userRepository.save(newUser);
        List<User> all = userRepository.findAll();
        System.out.println(all);
    }

    @Test
    void addNewProduct() {
        Product p = new Product();
        p.setId(1);
        p.setName("taleb");
        p.setPrice(2.5);

        productRepository.save(p);
        List<Product> all = productRepository.findAll();
        System.out.println(all);

    }

}
