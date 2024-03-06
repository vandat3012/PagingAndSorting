package com.example.pagingandsorting.controller;

import com.example.pagingandsorting.model.Customer;
import com.example.pagingandsorting.model.Province;
import com.example.pagingandsorting.service.ICustomerService;
import com.example.pagingandsorting.service.IProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/provinces")
public class ProvinceController {
    @Autowired
    private IProvinceService iProvinceService;
    @Autowired
    private ICustomerService iCustomerService;

    @GetMapping("")
    public String listProvince(Model model) {
        Iterable<Province> provinces = iProvinceService.findAll();
        model.addAttribute("provinces",provinces);
        return "province/listprovince";
    }
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("provinces",new Province());
        return "province/create";
    }

    @PostMapping("/save")
    public String save (Province province) {
        iProvinceService.save(province);
        return "redirect:/provinces";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView formEdit(@PathVariable Long id) {
        Optional<Province> provinces = iProvinceService.findById(id);
        if (provinces.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("province/edit");
            modelAndView.addObject("provinces",provinces.get());
            return modelAndView;
        }else {
            return new ModelAndView("error");
        }
    }
    @PostMapping("/edit/{id}")
    public String edit (@ModelAttribute("provinces") Province province) {
        iProvinceService.save(province);
        return "redirect:/provinces";
    }
    @GetMapping("/delete/{id}")
    public String delete (@PathVariable Long id, RedirectAttributes redirectAttributes) {
        iProvinceService.remove(id);
        redirectAttributes.addFlashAttribute("message", "Update province successfully");
        return "redirect:/provinces";
    }
    @GetMapping("/view/{id}")
    public ModelAndView viewProvince(@PathVariable("id") Long id){
        Optional<Province> provinceOptional = iProvinceService.findById(id);
        if(!provinceOptional.isPresent()){
            return new ModelAndView("error");
        }

        Iterable<Customer> customers = iCustomerService.findAllByProvince(provinceOptional.get());

        ModelAndView modelAndView = new ModelAndView("customer/listcustomer");
        modelAndView.addObject("customers", customers);
        return modelAndView;
    }
}
