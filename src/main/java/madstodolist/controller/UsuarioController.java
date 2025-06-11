package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import madstodolist.service.UsuarioServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import madstodolist.dto.UsuarioData;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ManagerUserSession managerUserSession;

    @GetMapping("/registrados")
    public String listaUsuarios(Model model) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        List<Usuario> usuarios = usuarioService.findAllUsers();
        model.addAttribute("idUsuarioLogeado", idUsuarioLogeado);
        model.addAttribute("usuarios", usuarios);
        return "registrados";
    }
    @GetMapping("/registrados/{id}")
    public String descripcionUsuario(@PathVariable Long id, Model model) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        UsuarioData usuario = usuarioService.findById(id);
        if (usuario == null) {
            throw new UsuarioServiceException("Usuario no encontrado");
        }
        model.addAttribute("idUsuarioLogeado", idUsuarioLogeado);
        model.addAttribute("usuario", usuario);
        return "descripcionUsuario";
    }
}