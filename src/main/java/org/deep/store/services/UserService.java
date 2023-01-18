package org.deep.store.services;

import java.util.List;

import org.deep.store.dtos.UserDto;

public interface UserService {

	// add user
	UserDto addUser(UserDto userDto);

	// update
	UserDto updateUser(UserDto user, String userId);

	// get single
	UserDto getUser(String userId);

	// get All
	List<UserDto> getAll();

	// delete user
	void deletUser(String userId);

	List<UserDto> searchUser(String keywords);

}
