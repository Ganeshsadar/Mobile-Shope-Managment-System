package com.averta.inventory.service;

import com.averta.inventory.bo.Response;
import com.averta.inventory.entity.User;

public interface UserService {

	public User loginUser(User user) throws Exception;

	public void addUser(User user) throws Exception;

	public User updateUser(User user, Long userId) throws Exception;

	public void resetPassword(User user) throws Exception;

	public String forgotPassword(User user) throws Exception;

	public String resendOTP(User user) throws Exception;

	public Response getUser(Integer pageNum, Integer numPerPage, String sortBy, String sortOrder, String searchBy)
			throws Exception;

	User getUserById(Long userId) throws Exception;

}