package vn.hoidanit.laptopshop.controller.admin;

import java.util.Optional;

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
            return "admin/product/create";
        }
        // upload img
        String img = this.uploadService.handleSaveUploadFile(file, "product");
        newProduct.setImage(img);
        this.productService.createProduct(newProduct);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/update/{id}")
    public String getUpdateProduct(@PathVariable long id, Model model) {
        Optional<Product> updateProduct = this.productService.fetchProductById(id);
        model.addAttribute("newProduct", updateProduct.get());
        return "admin/product/update";
    }

    @PostMapping("/admin/product/update")
    public String postUpdateProduct(@ModelAttribute("newProduct") @Valid Product pr,
            BindingResult newProductBindingResult, @RequestParam("hoidanitFile") MultipartFile file) {
        // valide
        if (newProductBindingResult.hasErrors()) {
            return "admin/product/update";
        }
        Product currentProduct = this.productService.fetchProductById(pr.getId()).get();
        if (currentProduct != null) {
            // update new image
            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveUploadFile(file, "product");
                currentProduct.setImage(img);
            }
            currentProduct.setName(pr.getName());
            currentProduct.setPrice(pr.getPrice());
            currentProduct.setQuantity(pr.getQuantity());
            currentProduct.setDetailDesc(pr.getDetailDesc());
            currentProduct.setShortDesc(pr.getShortDesc());
            currentProduct.setFactory(pr.getFactory());
            currentProduct.setTarget(pr.getTarget());

            this.productService.createProduct(currentProduct);
        }
        return "admin/product/show";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("product", new Product());
        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete")
    public String postDeleteProduct(Model model, @ModelAttribute("product") Product eric) {
        this.productService.deleteAProduct(eric.getId());
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        model.addAttribute("product", this.productService.fetchProductById(id).get());
        return "admin/product/detail";
    }

}
