package com.perfios.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.perfios.model.RepayTable;
import com.perfios.model.Role;
import com.perfios.model.TransactionTable;
import com.perfios.model.User;
import com.perfios.repository.RepayRepository;
import com.perfios.repository.TransactionRepository;
import com.perfios.repository.UserRepository;
import com.perfios.web.dto.UserRegistrationDto;
@Service
public class UserServiceImpl implements UserService {
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private TransactionRepository tr;
	@Autowired private RepayRepository rr;
	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public User save(UserRegistrationDto registrationDto) {
		// TODO Auto-generated method stub
		Random rnd = new Random();
		long accountNumber = 100000 + rnd.nextInt(900000);
		System.out.println(accountNumber);
		registrationDto.setAccountNumber(accountNumber);
		User user = new User("No",registrationDto.getCity(),registrationDto.getFirstName(),
				registrationDto.getLastName(),
				registrationDto.getAccountNumber(),
				registrationDto.getPin(),
				registrationDto.getAge(),
				registrationDto.getGender(),
				registrationDto.getContact(),
				registrationDto.getBalance(),
				registrationDto.getState(),
				registrationDto.getCountry(),
				registrationDto.getCity(),
				registrationDto.getEmail(),
				passwordEncoder.encode(registrationDto.getPassword()),			
	 Arrays.asList(new Role("ROLE_USER")));
		
		return userRepository.save(user);
	
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub

		User user = userRepository.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));		
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public User getUserById(long id) {
		// TODO Auto-generated method stub
		 Optional < User > optional = userRepository.findById(id);
	        User user = null;
	        if (optional.isPresent()) {
	            user = optional.get();
	        } else {
	            throw new RuntimeException(" User not found for id :: " + id);
	        }
	        return user;	}

	@Override
	public void saveUser(User user) {
		// TODO Auto-generated method stub
        this.userRepository.save(user);

	}

	@Override
	public List<TransactionTable> getAllTransaction() {
		// TODO Auto-generated method stub
		return tr.findAll();
	}

	@Override
	public List<TransactionTable> getUserTransaction() {
		 AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
				    SecurityContextHolder.getContext().getAuthentication();
		 User user=userRepository.findByEmail(auth.getName());
		return tr.findByAccount(user.getAccountNumber());
	}

	@Override
	public List<RepayTable> findByRepayed() {
		// TODO Auto-generated method stub
		AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
			    SecurityContextHolder.getContext().getAuthentication();
	 User user=userRepository.findByEmail(auth.getName());
	return rr.findByRepaid(auth.getName());
	}
	}

