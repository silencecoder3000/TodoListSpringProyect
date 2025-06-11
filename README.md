# Aplicación inicial ToDoList

Aplicación ToDoList de la asignatura Metologias Agiles 2025-A EPN usando Spring Boot y plantillas Thymeleaf.

## Requisitos

Necesitas tener instalado en tu sistema:

- Java 8

## Ejecución

Puedes ejecutar la aplicación usando el _goal_ `run` del _plugin_ Maven
de Spring Boot:

```
$ ./mvnw spring-boot:run
```

También puedes generar un `jar` y ejecutarlo:

```
$ ./mvnw package
$ java -jar target/mads-todolist-inicial-0.0.1-SNAPSHOT.jar
```

Una vez lanzada la aplicación puedes abrir un navegador y probar la página de inicio:

- [http://localhost:8080/login](http://localhost:8080/login)

# TodoListSpringBoot

### Enlance del Trello

https://trello.com/b/9ljlaahH/todolist-epn

### Enlace del Git hub 

# ToDoList - Proyecto Web con Spring Boot

Descripción General

ToDoList es una aplicación web desarrollada con Spring Boot que permite a los usuarios gestionar tareas pendientes. Este documento detalla las historias de usuario implementadas, incluyendo descripciones funcionales, criterios de aceptación, tareas de desarrollo, código fuente y pruebas.

Historia de Usuario: Barra de Menú (HU-002)

Descripción

Como usuario,
necesito ver una barra de menú,
para poder acceder a mis datos y salir de la sesión.

Criterios de Aceptación

Criterio 1

Dado que soy un usuario registrado,cuando accedo a cualquier página (excepto login y registro),entonces debo ver una barra de menú en la parte superior con los siguientes elementos:

Un enlace con el texto "ToDoList" que redirige a la página Acerca de.

Un enlace con el texto "Tareas" que redirige a la página con la lista de tareas pendientes.

Un menú desplegable en el lado derecho con el nombre de usuario que incluye:

Cuenta: enlace a la página de gestión de cuenta.

**Cerrar sesión **: redirige al login.

Criterio 2

Dado que soy un usuario no registrado,cuando accedo a la página Acerca de,entonces debo ver los enlaces "Login" y "Registro" en lugar del menú de usuario.

Criterio 3

Dado que estoy logueado,cuando accedo a la página Acerca de,entonces debo ver la barra común con ToDoList, Tareas y menú desplegable de usuario.

Tareas de Implementación

Modificar pom.xml para versión 1.0.1.

Crear archivo about.html con la estructura básica y barra de menú.

Agregar enlace en formLogin.html hacia Acerca de.

Implementar lógica condicional para mostrar elementos de menú según autenticación.

Probar visibilidad dinámica del menú.

Código HTML de la Barra de Menú
Fragmento que construye la barra de navegación con elementos visibles según el estado de autenticación del usuario.
```
<!-- Fragmento HTML con barra de navegación condicional según el estado de autenticación -->
<div th:fragment="menu">
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" th:href="@{/about}">ToDoList</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown"
                aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavDropdown">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item" th:if="${idUsuarioLogeado != null}">
                    <a class="nav-link" th:href="@{/usuarios/{id}/tareas(id=${idUsuarioLogeado})}">Tareas</a>
                </li>
                <li class="nav-item" th:if="${idUsuarioLogeado != null}">
                    <a class="nav-link" th:href="@{/registrados}">Usuarios Registrados</a>
                </li>
            </ul>
            <ul class="navbar-nav ml-auto">
                <li class="nav-item dropdown" th:if="${idUsuarioLogeado != null}">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button"
                       data-toggle="dropdown"  aria-expanded="false">
                        <span th:text="${nombreUsuario}">Usuario</span>
                    </a>
                    <div class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdownMenuLink">
                        <ul>
                            <li style="list-style: none"><a class="dropdown-item" th:href="@{/account}">Cuenta</a></li>
                            <li style="list-style: none"><a class="dropdown-item" th:href="@{/login}">Cerrar sesión</a></li>
                        </ul>
                    </div>
                </li>
                <li class="nav-item" th:if="${idUsuarioLogeado == null}">
                    <a class="nav-link" th:href="@{/login}">Login</a>
                </li>
                <li class="nav-item" th:if="${idUsuarioLogeado == null}">
                    <a class="nav-link" th:href="@{/registro}">Registro</a>
                </li>
            </ul>
        </div>
    </nav>
</div>
```

Controlador
Método que controla la ruta /about, obtiene información del usuario autenticado y la pasa a la vista para mostrar el nombre y controlar la visibilidad de elementos del menú.
```
// Controlador que carga la vista "about" e inyecta datos del usuario si está autenticado
@GetMapping("/about")
public String about(Model model) {
Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
if (idUsuarioLogeado != null) {
UsuarioData usuario = usuarioService.findById(idUsuarioLogeado);
model.addAttribute("nombreUsuario", usuario.getNombre());
}
model.addAttribute("idUsuarioLogeado", idUsuarioLogeado);
return "about";
}
```
Prueba Automática
Prueba que valida si un usuario no autenticado ve los elementos correctos en el menú ("Login" y "Registro") y no ve "Cerrar sesión".
```
// Prueba que verifica si un usuario no autenticado ve correctamente el menú de navegación
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testMenuForNonLoggedInUser() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(containsString("Registro")))
                .andExpect(content().string(not(containsString("Cerrar sesión"))));
    }
}
```
Historia de Usuario: Listado de Usuarios Registrados (HU-003)

Descripción

Como usuario autenticado,
quiero acceder a una lista de usuarios registrados,
para poder visualizar la información pública de otros usuarios.

Criterios de Aceptación

Criterio 1

Dado que estoy autenticado,cuando accedo a la ruta /registrados,entonces debo ver una tabla con los nombres y correos de los usuarios registrados.

Criterio 2

Dado que no estoy autenticado,cuando intento acceder a la ruta /registrados,entonces debo ser redirigido a la página de login.

Tareas de Implementación

Crear el controlador @GetMapping("/registrados").

Implementar el método findAll() en UsuarioService.

Crear vista registrados.html con tabla de usuarios.

Aplicar restricción de seguridad con Spring Security.

Crear prueba automática para la protección de la ruta.

Vista HTML
Fragmento HTML de la vista registrados.html que muestra una tabla con los nombres, correos y un enlace para ver los detalles de cada usuario.
```
<!-- Tabla que muestra el listado de usuarios registrados -->
<table class="table">
    <thead>
        <tr>
            <th>Nombre</th>
            <th>Correo</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <tbody>
        <tr th:each="usuario : ${usuarios}">
            <td th:text="${usuario.nombre}">Nombre</td>
            <td th:text="${usuario.correo}">Correo</td>
            <td><a th:href="@{'/registrados/' + ${usuario.id}}">Datos</a></td>
        </tr>
    </tbody>
</table>
```
Controlador
Método que atiende la ruta /registrados y carga en el modelo todos los usuarios registrados mediante usuarioService.findAll().
```
// Controlador que muestra todos los usuarios registrados
@GetMapping("/registrados")
public String registrados(Model model) {
List<UsuarioData> usuarios = usuarioService.findAll();
model.addAttribute("usuarios", usuarios);
return "registrados";
}
```
Seguridad
Configuración de seguridad que restringe el acceso a la ruta /registrados sólo a usuarios autenticados.
```
// Configuración de seguridad para permitir acceso sólo a usuarios autenticados
@Override
protected void configure(HttpSecurity http) throws Exception {
http.authorizeRequests()
.antMatchers("/registrados").authenticated()
.anyRequest().permitAll();
}
```
Prueba Automática
Prueba que valida que la ruta /registrados está protegida; redirige a /login si el usuario no está autenticado.
```
// Prueba que garantiza que la ruta /registrados está protegida si el usuario no está autenticado
@Test
public void accesoProtegido() throws Exception {
mockMvc.perform(get("/registrados"))
.andExpect(status().is3xxRedirection())
.andExpect(redirectedUrlPattern("**/login"));
}
```
Historia de Usuario: Descripción de Usuario (HU-004)

Descripción

Como usuario autenticado,
quiero ver una descripción detallada de un usuario registrado,
para poder conocer su información pública.

Criterios de Aceptación

Criterio 1

Dado que estoy autenticado,cuando accedo a la ruta /registrados/{id} con un ID válido,entonces debo ver los datos públicos del usuario correspondiente.

Criterio 2

Dado que no estoy autenticado,cuando intento acceder a /registrados/{id},entonces debo ser redirigido a login.

Tareas de Implementación

Crear la ruta /registrados/{id}.

Implementar búsqueda por ID en UsuarioService.

Crear vista descripcionUsuario.html.

Verificar autenticación para proteger acceso.

Probar acceso correcto e incorrecto.

Vista HTML
Vista que muestra los datos públicos (nombre y correo) de un usuario registrado, identificado por su ID.
```
<!-- Vista de la descripción de un usuario específico -->
<h2>Descripción del Usuario</h2>
<p><strong>Nombre:</strong> <span th:text="${usuario.nombre}">Nombre</span></p>
<p><strong>Correo:</strong> <span th:text="${usuario.correo}">Correo</span></p>
```
Controlador
Método que maneja la ruta /registrados/{id}, recupera el usuario correspondiente por ID y lo pasa al modelo para su despliegue en la vista.
```
// Controlador que muestra los datos públicos de un usuario por su ID
@GetMapping("/registrados/{id}")
public String descripcionUsuario(@PathVariable Long id, Model model) {
UsuarioData usuario = usuarioService.findById(id);
model.addAttribute("usuario", usuario);
return "descripcionUsuario";
}
```

Prueba Automática
Prueba que verifica que un usuario no autenticado es redirigido correctamente al intentar acceder al detalle de otro usuario.
```
// Prueba que verifica que un usuario no autenticado es redirigido al acceder al detalle de usuario
@Test
public void redireccionSiNoLogeado() throws Exception {
mockMvc.perform(get("/registrados/1"))
.andExpect(status().is3xxRedirection())
.andExpect(redirectedUrlPattern("**/login"));
}
```