package com.sodaweb.controller;

import com.sodaweb.domain.Proveedor;
import com.sodaweb.service.ProveedorService;
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
@RequestMapping("/proveedor")
public class ProveedorController {

    private final ProveedorService proveedorService;
    private final MessageSource messageSource;

    public ProveedorController(ProveedorService proveedorService, MessageSource messageSource) {
        this.proveedorService = proveedorService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var proveedores = proveedorService.getProveedores();
        model.addAttribute("proveedores", proveedores);
        model.addAttribute("totalProveedores", proveedores.size());
        return "/proveedor/listado";
    }

    // HU-13: buscar al proveedor por medio de su nombre
    @PostMapping("/buscar")
    public String buscar(@RequestParam(required = false) String nombre, Model model) {
        var proveedores = proveedorService.buscar(nombre);
        model.addAttribute("proveedores", proveedores);
        model.addAttribute("totalProveedores", proveedores.size());
        model.addAttribute("nombre", nombre);
        return "/proveedor/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Proveedor proveedor, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.actualizado";
        if (bindingResult.hasErrors()) {
            titulo = "error"; // Valores o palabras incorrectas (excepción 1 de HU-13)
            mensaje = "proveedor.error04";
        } else {
            try {
                proveedorService.save(proveedor);
            } catch (DataIntegrityViolationException e) {
                titulo = "error"; // No se guardó la información (excepción 2 de HU-13)
                mensaje = "proveedor.error05";
            }
        }
        redirectAttributes.addFlashAttribute(titulo,
                messageSource.getMessage(mensaje, null, LocaleContextHolder.getLocale()));
        return "redirect:/proveedor/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idProveedor, RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try {
            proveedorService.delete(idProveedor);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "proveedor.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "proveedor.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "proveedor.error03";
        }
        redirectAttributes.addFlashAttribute(titulo,
                messageSource.getMessage(detalle, null, LocaleContextHolder.getLocale()));
        return "redirect:/proveedor/listado";
    }
}
