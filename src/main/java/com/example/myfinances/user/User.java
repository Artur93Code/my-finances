package com.example.myfinances.user;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import com.example.myfinances.finance.Finance;
import com.example.myfinances.finance.FinanceService;
import com.example.myfinances.role.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table
public class User implements UserDetails {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private LocalDate dob; //date of birth

    @Transient
    private Float saldo;

    @Transient
    private Integer age;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName="id")
    )
    private List<Role> roles; //= new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)//cascade = CascadeType.ALL, orphanRemoval = true - when user entity (parent) is delete joined finance entities (child) is delete too
    @JoinTable(
            name = "user_finance",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "finance_id", referencedColumnName="id")
    )
    private List<Finance> finances;

    //default constructor
    public User(){}

    public User(Long id, String username, String password, String email, LocalDate dob) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.dob = dob;
    }

    public User(String username, String password, String email, LocalDate dob) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.dob = dob;
    }

    public User(String username, String password, String email, LocalDate dob, boolean accountNonLocked, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.dob = dob;
        this.accountNonLocked = accountNonLocked;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Integer getAge() { return Period.between(this.dob, LocalDate.now()).getYears(); }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Float getSaldo() {
        saldo = 0F;
        for (Finance transaction : getFinances())
        {
            if(transaction.getType().equals("income"))
                saldo = saldo+transaction.getAmount();
            else
            {
                saldo=saldo-transaction.getAmount();
            }
        }
        return saldo;
    }

    public void setSaldo(Float saldo) {
        this.saldo = saldo;
    }

    public List<Finance> getFinances() {
        return finances;
    }

    public void setFinances(List<Finance> finances) {
        this.finances = finances;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : this.roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override public boolean isEnabled() {
        return true;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
    public boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                '}';
    }

    public void setAuthorities(List<Role> roles) {
        this.roles = roles;
    }
}
