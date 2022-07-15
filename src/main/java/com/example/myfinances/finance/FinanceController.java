package com.example.myfinances.finance;

import com.example.myfinances.role.RoleRepository;
import com.example.myfinances.user.User;
import com.example.myfinances.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@ControllerAdvice //required to set global attributes
@RequestMapping("transaction")
public class FinanceController {

    @Autowired private UserRepository userRepository;
    @Autowired private FinanceRepository financeRepository;

    @Autowired private FinanceService financeService;


/*    @GetMapping(path = "add")
    public String getUsers(Authentication authentication)
    {
        return "index";
    }*/

    @PostMapping(path = "add")
    //@ResponseStatus(value = HttpStatus.OK)
    @Transactional //in this case using this annotation makes it unnecessary line 'userRepository.save(currentUser)'
    public String addNewTransaction(Authentication authentication, @RequestParam Map<String, String> body, Model model, SessionStatus status)
    {
        Finance newTransaction = new Finance(
                body.get("type"),
                Float.parseFloat(body.get("amount")),
                body.get("description"),
                LocalDate.parse(body.get("dot")));
        financeRepository.save(newTransaction);
        String username = authentication.getName();
        User currentUser = userRepository.findUserByUsername(username).get();
        List<Finance> finances = currentUser.getFinances();
        finances.add(newTransaction);
        currentUser.setFinances(finances);

        getUserFinances(authentication,model); //if this method dont call here the page won't show account saldo after last transaction
        status.setComplete();
        //userRepository.save(currentUser);
        return "index";
    }

    @PostMapping(path = "delete/{transactionId}")
    public String deleteTransaction(Authentication authentication, Model model, @PathVariable("transactionId") Long transactionId)
    {
        boolean exist = financeRepository.existsById(transactionId);
        if(exist)
        {
            String username = authentication.getName();
            User currentUser = userRepository.findUserByUsername(username).get();

            Finance financeToDelete = financeRepository.getById(transactionId);
            List<Finance> newList = currentUser.getFinances();
            if(newList.contains(financeToDelete))
            {
                newList.remove(financeToDelete);
                currentUser.setFinances(newList);
                userRepository.save(currentUser);
/*            Finance fin = financeRepository.findFinanceById(transactionId).get();
            fin.setId(99L);
            financeRepository.save(fin);*/
                //financeRepository.deleteAll();
                //financeRepository.deleteById(transactionId);
                getUserFinances(authentication, model);
            }
        }
        //financeRepository.deleteById(1L);
        return "index";
        //userService.deleteUser(userId);
    }

/*    @ModelAttribute
    public void getCurrentUser(Model model) {

        //User currentUser = userRepository.findUserByUsername("Katarzyna").get();
        model.addAttribute("name", "tak");
        model.addAttribute("email", "nie");
    }*/

    @ModelAttribute //set global attributes for view
    public void getUserFinances(Authentication authentication, Model model) {
            if(authentication!=null) {
                User currentUser = userRepository.findUserByUsername(authentication.getName()).get();
                List<Finance> transactions = currentUser.getFinances();
                Float saldo = currentUser.getSaldo();
                //List<Finance> transactions = financeService.getFinances(authentication.getName());
                model.addAttribute("transactions", transactions);
                model.addAttribute("saldo", saldo);
            }

    }
/*    @ModelAttribute("user")
    public void getCurrentUser(Model model) {

        //User currentUser = userRepository.findUserByUsername("Katarzyna").get();
        model.addAttribute("users", "Hej");
    }*/
}
