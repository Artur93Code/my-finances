package com.example.myfinances.finance;


import com.example.myfinances.user.User;
import com.example.myfinances.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Service
public class FinanceService {

    @Autowired
    private UserRepository userRepository;

/*    public List<Finance> getFinances(String username)
    {
        User currentUser = userRepository.findUserByUsername(username).get();
        return currentUser.getFinances();
    }*/
/*    @ModelAttribute("transactions")
    public List<Finance> finances(String username) {

        User currentUser = userRepository.findUserByUsername(username).get();
        List<Finance> finances = currentUser.getFinances();
        return finances;
    }*/
}
