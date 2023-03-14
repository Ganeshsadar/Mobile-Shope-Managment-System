package com.averta.inventory.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.averta.inventory.bo.Response;
import com.averta.inventory.entity.User;
import com.averta.inventory.exception.InventoryException;
import com.averta.inventory.exception.ResourceNotFoundException;
import com.averta.inventory.repository.UserRepository;
import com.averta.inventory.repository.UserRoleRepository;
import com.averta.inventory.security.JwtTokenHelper;
import com.averta.inventory.service.UserService;
import com.averta.inventory.utility.ErrorConstants;
import com.averta.inventory.utility.MailUtility;
import com.averta.inventory.utility.Utils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private MailUtility mailUtility;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Override
	public void addUser(User user) throws Exception {
		User checkMobile = userRepository.findByMobileNumber(user.getMobileNumber());
		if (checkMobile != null) {
			throw new InventoryException(ErrorConstants.INVALID,
					"Mobile No. " + user.getMobileNumber() + " already exists");
		}
		Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
		User checkEmail = optionalUser.isPresent() ? optionalUser.get() : null;
		if (checkEmail != null) {
			throw new InventoryException(ErrorConstants.INVALID, "Email " + user.getEmail() + " already exists");
		}
		user.setUniqueNo(user.getUserRole() + Utils.generateRandomNumber(10));
		String password = Utils.generateRandomNumber(6);
		System.out.println("Password =>" + password);
		user.setPassword(passwordEncoder.encode(password));
		user.setUserRole(userRoleRepository.getById(user.getRoleId()));

//		mailUtility.sendSimpleMessage(user.getEmail(), "Password", "Your Password is = " + password);

		String otp = Utils.generateRandomNumber(6);
		user.setOtp(otp);
		user.setOtpVerifyStatus(false);
		user.setOtpTime(new Date());
		userRepository.save(user);
		user.setPassword(null);
		user.setPwdSalt(null);
	}

	@Override
	public User loginUser(User user) throws Exception {
		User checkLogin = userRepository.findByMobileNumber(user.getEmail());
		if (checkLogin == null) {
			Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
			checkLogin = optionalUser.isPresent() ? optionalUser.get() : null;
			if (checkLogin == null) {
				throw new ResourceNotFoundException(ErrorConstants.NOT_FOUND,
						"Please enter valid username and password");
			}
		}
		if (!passwordEncoder.matches(user.getPassword(), checkLogin.getPassword())) {
			throw new InventoryException(ErrorConstants.INVALID, "Username or Password incorrect");
		}

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				user.getEmail(), user.getPassword());

		authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
		String generateToken = jwtTokenHelper.generateToken(userDetails);
		checkLogin.setSessionToken(generateToken);

		checkLogin.setDeviceId(user.getDeviceId());
		userRepository.save(checkLogin);
		checkLogin.setPassword(null);
		checkLogin.setPwdSalt(null);
		return checkLogin;
	}

	@Override
	public User updateUser(User user, Long userId) throws Exception {
		User checkUser = getUserById(userId);
		if (!user.getMobileNumber().toString().equalsIgnoreCase(checkUser.getMobileNumber().toString())) {
			User checkMobile = userRepository.findByMobileNumber(user.getMobileNumber());
			if (checkMobile != null) {
				throw new InventoryException(ErrorConstants.INVALID,
						"Mobile No. " + user.getMobileNumber() + " already exists");
			}
		}

		if (!user.getEmail().equalsIgnoreCase(checkUser.getEmail())) {
			Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
			User checkEmail = optionalUser.isPresent() ? optionalUser.get() : null;
			if (checkEmail != null) {
				throw new InventoryException(ErrorConstants.INVALID, "Email " + user.getEmail() + " already exists");
			}
		}
		checkUser.setUserRole(userRoleRepository.getById(user.getRoleId()));
		checkUser.setMobileNumber(user.getMobileNumber());
		checkUser.setEmail(user.getEmail());
		checkUser.setName(user.getName());
		userRepository.save(checkUser);
		return checkUser;
	}

	@Override
	public void resetPassword(User user) throws Exception {
		if (null != user.getUniqueNo() && !user.getUniqueNo().isEmpty()) {
			User checkUser = userRepository.findByUniqueNoAndOtpAndOtpVerifyStatusFalse(user.getUniqueNo(),
					user.getOtp());
			if (null != checkUser) {
				checkUser.setOtpVerifyStatus(true);
				String salt = Utils.getAlphaNumString(5, 5);
				checkUser.setPwdSalt(salt);
				checkUser.setPassword(passwordEncoder.encode(user.getPassword()));
				userRepository.save(checkUser);
			} else {
				throw new InventoryException(ErrorConstants.INVALID, "Invalid OTP");
			}
		} else {
			throw new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "User Not Found");
		}
	}

	@Override
	public String resendOTP(User user) throws Exception {
		Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
		User checkUser = optionalUser.isPresent() ? optionalUser.get() : null;
		if (null == checkUser) {
			throw new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "User Not Found");
		} else {
			String otp = Utils.generateRandomNumber(6);
			checkUser.setOtp(otp);
			checkUser.setOtpVerifyStatus(false);
			checkUser.setOtpTime(new Date());
			userRepository.save(checkUser);
			mailUtility.sendSimpleMessage(user.getEmail(), "Password reset OTP",
					"Please use otp: " + otp + " to reset your account password");
			checkUser.setPassword(null);
			checkUser.setPwdSalt(null);
			return checkUser.getUniqueNo();
		}
	}

	@Override
	public String forgotPassword(User user) throws Exception {
		try {
			if (null != user.getEmail() && !user.getEmail().isEmpty()) {
				Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
				User checkUser = optionalUser.isPresent() ? optionalUser.get() : null;
				if (null == checkUser) {
					throw new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "mail id doesn't exist");
				} else {
					String otp = Utils.generateRandomNumber(6);
					checkUser.setOtp(otp);
					checkUser.setOtpVerifyStatus(false);
					checkUser.setOtpTime(new Date());
					userRepository.save(checkUser);
					return checkUser.getUniqueNo();
				}
			} else {
				throw new InventoryException(ErrorConstants.INVALID, "emailId is required");
			}
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public User getUserById(Long userId) throws Exception {
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "User not found"));
	}

	@Override
	public Response getUser(Integer pageNum, Integer numPerPage, String sortBy, String sortOrder, String searchBy)
			throws Exception {
		Response response = new Response();
		Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNum - 1, numPerPage, sort);

		Page<User> page = null;
		if (searchBy != null && !searchBy.isEmpty()) {
			page = userRepository.findByNameContainingOrMobileNumberContainingOrEmailContaining(searchBy, searchBy,
					searchBy, pageable);
		} else {
			page = userRepository.findAll(pageable);
		}
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("User List is ");
		response.setResult(page.getContent());
		response.setListCount(page.getTotalElements());
		return response;
	}

}