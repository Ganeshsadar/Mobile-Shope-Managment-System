package com.averta.inventory.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.averta.inventory.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public User findByMobileNumber(String mobileNumber) throws Exception;

	public Optional<User> findByEmail(String email) throws UsernameNotFoundException;

	public User findByUniqueNoAndOtpAndOtpVerifyStatusFalse(String uniqueNo, String otp) throws Exception;

	public User findByUniqueNo(String uniqueNo) throws Exception;

	public Page<User> findByNameContainingOrMobileNumberContainingOrEmailContaining(String name, String mobileNumber,
			String email, Pageable pageable) throws Exception;

	
}
