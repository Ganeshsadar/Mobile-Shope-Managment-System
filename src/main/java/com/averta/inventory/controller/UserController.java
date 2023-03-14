package com.averta.inventory.controller;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.averta.inventory.bo.Response;
import com.averta.inventory.entity.User;
import com.averta.inventory.repository.ObjectDao;
import com.averta.inventory.repository.UserRepository;
import com.averta.inventory.service.UserService;
import com.averta.inventory.utility.ErrorConstants;
import com.averta.inventory.utility.MailUtility;
import com.averta.inventory.utility.Utils;

@RestController
@RequestMapping("/v1/user")
@CrossOrigin()
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ObjectDao objectDao;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MailUtility mailUtility;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@CrossOrigin
	@PostMapping(value = "/add")
	private ResponseEntity<Response> addUser(@Valid @RequestBody User user) throws Exception {
		userService.addUser(user);
		Response response = new Response();
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("User Added Successfully");
		return new ResponseEntity<Response>(response, HttpStatus.CREATED);
	}

	@CrossOrigin
	@PostMapping(value = "/login")
	public ResponseEntity<Response> loginUser(@RequestBody User user) throws Exception {
		Response response = new Response();
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("User login successfully");
		response.setResult(userService.loginUser(user));
		return ResponseEntity.ok(response);
	}

	@CrossOrigin
	@GetMapping(value = "/list")
	public ResponseEntity<Response> getUserList(
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "numPerPage", required = false, defaultValue = "10") Integer numPerPage,
			@RequestParam(value = "sortBy", required = false, defaultValue = "userId") String sortBy,
			@RequestParam(value = "sortOrder", required = false, defaultValue = "desc") String sortOrder,
			@RequestParam(value = "searchBy", required = false) String searchBy) throws Exception {
		return new ResponseEntity<Response>(userService.getUser(pageNum, numPerPage, sortBy, sortOrder, searchBy),
				HttpStatus.OK);
	}

	@CrossOrigin
	@PutMapping("/update/{userId}")
	private ResponseEntity<Response> updateUser(@Valid @RequestBody User user, @PathVariable Long userId)
			throws Exception {
		Response response = new Response();
		response.setResult(userService.updateUser(user, userId));
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Profile updated successfully");
		return ResponseEntity.ok(response);
	}

	@CrossOrigin
	@PostMapping("/forgot-password")
	public ResponseEntity<Response> forgotPassword(@RequestBody User user) {
		Response response = new Response();
		try {
			if (null != user.getEmail()) {
				Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
				if (!optionalUser.isPresent()) {
					response.setStatus(ErrorConstants.NOT_FOUND);
					response.setMessage("Email id doesn't exist");
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
				} else {
					User checkUser = optionalUser.get();
					String otp = Utils.generateRandomNumber(6);
					checkUser.setOtp(otp);
					checkUser.setOtpVerifyStatus(false);
					checkUser.setOtpTime(new Date());
					objectDao.updateObject(checkUser);

					mailUtility.sendSimpleMessage(user.getEmail(), "Password reset OTP",
							"Please use otp: " + otp + " to reset your account password");

					response.setStatus(ErrorConstants.SUCCESS);
					response.setMessage("Otp sent please verify");
					response.setResult(checkUser.getUniqueNo());
					return ResponseEntity.ok(response);
				}
			} else {
				response.setStatus(ErrorConstants.INVALID);
				response.setMessage("Email is required");
				response.setResult(user);
				return ResponseEntity.status(HttpStatus.MULTIPLE_CHOICES).body(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(ErrorConstants.INTERNAL_SERVER_ERROR);
			response.setMessage("Error in saving data,please try again....");
			response.setResult(null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@CrossOrigin
	@PostMapping(value = "/verify-otp")
	private ResponseEntity<Response> verifyOtp(@RequestBody User user) {
		Response response = new Response();
		try {
			User checkUser = userRepository.findByUniqueNoAndOtpAndOtpVerifyStatusFalse(user.getUniqueNo(),
					user.getOtp());
			if (null == checkUser) {
				response.setStatus("300");
				response.setMessage("Invalid OTP");
				return new ResponseEntity<Response>(response, HttpStatus.MULTIPLE_CHOICES);
			} else if (((new Date()).getTime() - checkUser.getOtpTime().getTime()) > 120000) {
				response.setStatus("300");
				response.setMessage("OTP Expired");
				return new ResponseEntity<Response>(response, HttpStatus.MULTIPLE_CHOICES);
			} else {
				checkUser.setOtpVerifyStatus(true);
				userRepository.save(checkUser);
				response.setStatus("200");
				response.setMessage("OTP verified ,Please login ");
				checkUser.setPassword(null);
				checkUser.setPwdSalt(null);
				response.setResult(checkUser.getUniqueNo());
				return new ResponseEntity<Response>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(ErrorConstants.INTERNAL_SERVER_ERROR);
			response.setMessage(ErrorConstants.SERVER_ERROR);
			response.setResult(e.getMessage());
			return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin
	@PostMapping(value = "/reset-password")
	private ResponseEntity<Response> resetPassword(@RequestBody User User) throws Exception {
		Response response = new Response();
		userService.resetPassword(User);
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Password reset successfully, Please login");
		response.setResult(User.getUniqueNo());
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@PostMapping(value = "/change-user-password")
	private ResponseEntity<Response> changePassword(@RequestBody User user) {
		Response response = new Response();
		try {
			User checkUser = userRepository.findByUniqueNo(user.getUniqueNo());
			if (null != checkUser) {
				if (passwordEncoder.matches(user.getPassword(), checkUser.getPassword())) {
					String salt = Utils.getAlphaNumString(5, 5);
					checkUser.setPwdSalt(salt);
					checkUser.setPassword(passwordEncoder.encode(user.getNewPassword()));
					userRepository.save(checkUser);
					response.setStatus(ErrorConstants.SUCCESS);
					response.setMessage("Password changed successfully");
					checkUser.setPassword(null);
					checkUser.setPwdSalt(null);
					response.setResult(checkUser);
					return ResponseEntity.ok(response);
				} else {
					response.setStatus(ErrorConstants.INVALID);
					response.setMessage("Previous password is incorrect");
					response.setResult(null);
					return new ResponseEntity<Response>(response, HttpStatus.MULTIPLE_CHOICES);
				}
			} else {
				response.setStatus(ErrorConstants.NOT_FOUND);
				response.setMessage("user not found");
				response.setResult(null);
				return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(ErrorConstants.INTERNAL_SERVER_ERROR);
			response.setMessage(ErrorConstants.SERVER_ERROR);
			response.setResult(e.getMessage());
			return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin
	@PostMapping(value = "/resend-otp")
	private ResponseEntity<Response> resendOTP(@RequestBody User user) throws Exception {
		Response response = new Response();
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("OTP sent please verify");

		response.setResult(userService.resendOTP(user));
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

}
