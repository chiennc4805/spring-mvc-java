package vn.hoidanit.laptopshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UploadService uploadService;

    ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    @GetMapping("/admin/product")
    public String getProduct(Model model) {
        model.addAttribute("products", this.productService.fetchProducts());
        return "admin/product/show";
    }

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String postCreateProduct(Model model, @ModelAttribute("newProduct") @Valid Product newProduct,
            BindingResult newProductBindingResult, @RequestParam("hoidanitFile") MultipartFile file) {
        // validate
        // List<FieldError> errors = newProductBindingResult.getFieldErrors();
        // for (FieldError error : errors) {
        // System.out.println(">>>>" + error.getField() + " - " +
        // error.getDefaultMessage());
        // }
        if (newProductBindingResult.hasErrors()) {
            return "/admin/product/create";
        }
        // upload img
        String img = this.uploadService.handleSaveUploadFile(file, "product");
        newProduct.setImage(img);
        this.productService.createProduct(newProduct);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/update/{id}")
    public String getUpdateProduct(@PathVariable long id, Model model) {
        Product updateProduct = this.productService.getProductById(id);
        model.addAttribute("updateProduct", updateProduct);
        return "/admin/product/update";
    }

    @PostMapping("/admin/product/update/{id}")
    public String postUpdateProduct(@PathVariable long id, @ModelAttribute("Product") @Valid Product updateProduct,
            BindingResult updateProducBindingResult, @RequestParam("hoidanitFile") MultipartFile file) {
        Product currentProduct = this.productService.getProductById(id);
        String img = this.uploadService.handleSaveUploadFile(file, "product");
        if (updateProducBindingResult.hasErrors()) {
            return "/admin/product/update";
        }
        if (currentProduct != null) {
        }
        return "/admin/product/show";
    }

}
