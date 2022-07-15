package com.example.myfinances.authentication;

import com.example.myfinances.finance.Finance;
import com.example.myfinances.finance.FinanceController;
import com.example.myfinances.finance.FinanceService;
import com.example.myfinances.role.Role;
import com.example.myfinances.role.RoleRepository;
import com.example.myfinances.user.User;
import com.example.myfinances.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;

@Controller
public class AuthController {

    @Autowired private SecurityUserDetailsService userDetailsManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    Role role = new Role();
    private RoleRepository roleRepository;

    @Autowired
    private FinanceService financeService;

    @Autowired
    public AuthController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public AuthController() {
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpSession session) {
        session.setAttribute("error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
        return "login";
    }

/*    @PostMapping
    (value = "/register",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
            MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
    )*/

    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @PostMapping(
            value = "/register",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
            MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
    )
    public void addUser(@RequestParam Map<String, String> body) {
        User user = new User();
        user.setUsername(body.get("username"));
        user.setPassword(passwordEncoder.encode(body.get("password")));
        user.setEmail(body.get("email"));
        user.setDob(LocalDate.parse(body.get("dob")));
        user.setAccountNonLocked(true);

        List<Role> optionalRole = roleRepository.findRoleByName("User").stream().toList();
        user.setAuthorities(optionalRole);
        //user.getAuthorities().add(optionalRole);
        userDetailsManager.createUser(user);
    }

    @GetMapping("/")
    public String defaultPage() {
        return "index";
    }
/*    @GetMapping("/index")
    public String index() {
        return "index";
    }*/

    private String getErrorMessage(HttpServletRequest request, String key) {
        Exception exception = (Exception) request.getSession().getAttribute(key);
        String error = "";
        if (exception instanceof BadCredentialsException) {
            error = "Invalid username and password!";
        } else if (exception instanceof LockedException) {
            error = exception.getMessage();
        } else {
            error = "Invalid username and password!";
        }

        return error;
    }
}
