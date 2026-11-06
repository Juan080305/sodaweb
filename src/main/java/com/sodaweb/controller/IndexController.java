package com.sodaweb.controller;

import com.sodaweb.service.CategoriaService;
import com.sodaweb.service.ProductoService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class IndexController {

    // Las últimas versiones de Spring, recomiendan utilizar final y constructor en lugar de @Autowired
    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final MessageSource messageSource;

    // (Spring inyecta automáticamente)
    public IndexController(ProductoService productoService, CategoriaService categoriaService,
            MessageSource messageSource) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.messageSource = messageSource;
    }

    // HU-01: menú digital con todas las categorías y productos activos
    @GetMapping("/")
    public String cargarPaginaInicio(Model model) {
        var productos = productoService.getMenu(null);
        model.addAttribute("productos", productos);
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/index";
    }

    // HU-01: menú filtrado por la categoría seleccionada en las "tabs"
    @GetMapping("/menu/{idCategoria}")
    public String menuCategoria(@PathVariable("idCategoria") Integer idCategoria, Model model) {
        model.addAttribute("idCategoriaActual", idCategoria);
        var productos = productoService.getMenu(idCategoria);
        model.addAttribute("productos", productos);
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/index";
    }

    // HU-02: detalle de un producto
    @GetMapping("/menu/producto/{idProducto}")
    public String detalle(@PathVariable("idProducto") Integer idProducto, Model model,
            RedirectAttributes redirectAttributes) {
        var productoOptional = productoService.getProducto(idProducto);
        if (productoOptional.isEmpty() || !productoOptional.get().isActivo()) {
            //Puede ser que el producto no exista o que ya no esté en el menú...
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("producto.error01", null, LocaleContextHolder.getLocale()));
            return "redirect:/";
        }
        model.addAttribute("producto", productoOptional.get());
        return "/menu/detalle";
    }
}
