package com.example.myfinances.finance;

import com.example.myfinances.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Repository
public interface FinanceRepository extends JpaRepository<Finance, Long> {

    Optional<Finance> findFinanceByType(String type);

    Optional<Finance> findFinanceById(Long id);
}
