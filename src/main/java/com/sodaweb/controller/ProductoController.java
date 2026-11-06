package com.sodaweb.controller;

import com.sodaweb.domain.Producto;
import com.sodaweb.service.CategoriaService;
import com.sodaweb.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final MessageSource messageSource;

    public ProductoController(ProductoService productoService, CategoriaService categoriaService,
            MessageSource messageSource) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var productos = productoService.getProductos(false);
        model.addAttribute("productos", productos);
        model.addAttribute("totalProductos", productos.size());
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/producto/listado";
    }

    // HU-10: buscar el producto por su nombre y categoría para modificarlo con facilidad
    @PostMapping("/buscar")
    public String buscar(@RequestParam(required = false) String descripcion,
            @RequestParam(required = false) Integer idCategoria,
            Model model) {
        var productos = productoService.consultaBusqueda(descripcion, idCategoria);
        model.addAttribute("productos", productos);
        model.addAttribute("totalProductos", productos.size());
        model.addAttribute("categorias", categoriaService.getCategorias(true));
        model.addAttribute("descripcion", descripcion);
        model.addAttribute("idCategoria", idCategoria);
        return "/producto/listado";
    }

    // HU-12: productos que llegaron a su stock mínimo
    @GetMapping("/alertas")
    public String alertas(Model model) {
        var productos = productoService.consultaAlertas();
        model.addAttribute("productos", productos);
        model.addAttribute("totalAlertas", productos.size());
        return "/producto/alertas";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Producto producto, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.actualizado";
        if (bindingResult.hasErrors() || producto.getCategoria() == null
                || producto.getCategoria().getIdCategoria() == null) {
            titulo = "error";
            mensaje = "producto.error04";
        } else {
            try {
                productoService.save(producto);
            } catch (DataIntegrityViolationException e) {
                titulo = "error";
                mensaje = "producto.error05";
            }
        }
        redirectAttributes.addFlashAttribute(titulo,
                messageSource.getMessage(mensaje, null, LocaleContextHolder.getLocale()));
        return "redirect:/producto/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idProducto, RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try {
            productoService.delete(idProducto);
        } catch (IllegalArgumentException e) {
            titulo = "error"; // Captura la excepción de argumento inválido para el mensaje de "no existe"
            detalle = "producto.error01";
        } catch (IllegalStateException e) {
            titulo = "error"; // Captura la excepción de estado ilegal para el mensaje de "datos asociados"
            detalle = "producto.error02";
        } catch (Exception e) {
            titulo = "error";  // Captura cualquier otra excepción inesperada
            detalle = "producto.error03";
        }
        redirectAttributes.addFlashAttribute(titulo,
                messageSource.getMessage(detalle, null, LocaleContextHolder.getLocale()));
        return "redirect:/producto/listado";
    }
}
