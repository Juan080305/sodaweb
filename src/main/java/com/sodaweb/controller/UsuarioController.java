package com.sodaweb.controller;

import com.sodaweb.domain.Usuario;
import com.sodaweb.service.RolService;
import com.sodaweb.service.UsuarioService;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final MessageSource messageSource;

    public UsuarioController(UsuarioService usuarioService, RolService rolService,
            MessageSource messageSource) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var usuarios = usuarioService.getUsuarios();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());
        model.addAttribute("roles", rolService.getRoles());
        return "/usuario/listado";
    }

    // HU-14: se introduce el nombre del usuario, el tipo de rol y su contraseña
    @PostMapping("/guardar")
    public String guardar(@Valid Usuario usuario, BindingResult bindingResult,
            @RequestParam Integer idRol,
            @RequestParam(required = false) String clave,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = mensaje("mensaje.actualizado");
        if (bindingResult.hasErrors()) {
            titulo = "error"; // Valores o palabras incorrectas (excepción 1 de HU-14)
            mensaje = mensaje("usuario.error04");
        } else {
            try {
                usuarioService.save(usuario, idRol, clave);
            } catch (IllegalArgumentException e) {
                titulo = "error";
                mensaje = e.getMessage();
            } catch (DataIntegrityViolationException e) {
                titulo = "error"; // No se guardó la información (excepción 2 de HU-14)
                mensaje = mensaje("usuario.error05");
            }
        }
        redirectAttributes.addFlashAttribute(titulo, mensaje);
        return "redirect:/usuario/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idUsuario, Principal principal,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = mensaje("mensaje.eliminado");
        try {
            usuarioService.delete(idUsuario, principal.getName());
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = mensaje("usuario.error01");
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = e.getMessage();
        } catch (Exception e) {
            titulo = "error";
            detalle = mensaje("usuario.error03");
        }
        redirectAttributes.addFlashAttribute(titulo, detalle);
        return "redirect:/usuario/listado";
    }

    private String mensaje(String clave) {
        return messageSource.getMessage(clave, null, LocaleContextHolder.getLocale());
    }
}
