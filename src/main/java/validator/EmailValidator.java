package main.java.validator;

import javax.inject.Inject;

import main.java.dao.interfaces.UserDao;
import main.java.model.User;

public class EmailValidator {
	@Inject
	private UserDao userDao;
	
	public User findUser(String email, String password){
		return userDao.getByEmailAndPassword(email,password);
	}
}
