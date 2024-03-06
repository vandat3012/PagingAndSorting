package com.example.pagingandsorting.controller;

import com.example.pagingandsorting.model.Customer;
import com.example.pagingandsorting.model.Province;
import com.example.pagingandsorting.service.ICustomerService;
import com.example.pagingandsorting.service.IProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private ICustomerService iCustomerService;
    @Autowired
    private IProvinceService iProvinceService;
    @ModelAttribute("provinces")
    public Iterable<Province> listProvinces() {
        return iProvinceService.findAll();
    }
    @GetMapping("")
    public String listCustomer(Model model,@PageableDefault(size = 3) Pageable pageable) {
        Page<Customer> customerIterable = iCustomerService.findAll(pageable);
        model.addAttribute("customers",customerIterable);
        return "customer/listcustomer";
    }
    @GetMapping("/search")
    public ModelAndView search(@RequestParam("search")Optional<String> name,Pageable pageable) {
        Page<Customer> customers;
        if (name.isPresent()){
            customers = iCustomerService.findAllByFirstNameContaining(pageable,name.get());
        }else {
            customers = iCustomerService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("customer/listcustomer");
        modelAndView.addObject("customers",customers);
        return modelAndView;
    }

    @GetMapping("/create")
    public String create (Model model) {
        model.addAttribute("customers",new Customer());
        return "customer/create";
    }
    @PostMapping("/save")
    public String save (Customer customer) {
        iCustomerService.save(customer);
        return "redirect:/customers";
    }
    @GetMapping("/edit/{id}")
    public ModelAndView formEdit(@PathVariable Long id) {
        Optional<Customer> customerOptional = iCustomerService.findById(id);
        if (customerOptional.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("customer/edit");
            modelAndView.addObject("customers",customerOptional.get());
            return modelAndView;
        }else {
            return new ModelAndView("error");
        }
    }
    @PostMapping("/edit/{id}")
    public String edit (@ModelAttribute("customers")Customer customer) {
        iCustomerService.save(customer);
        return "redirect:/customers";
    }
    @GetMapping("/delete/{id}")
    public String delete (@PathVariable Long id) {
        iCustomerService.remove(id);
        return "redirect:/customers";
    }
}
