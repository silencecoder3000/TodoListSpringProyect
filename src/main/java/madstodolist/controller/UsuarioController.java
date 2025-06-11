package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}