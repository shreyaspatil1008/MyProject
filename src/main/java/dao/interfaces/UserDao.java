package main.java.dao.interfaces;

import main.java.model.User;

public interface UserDao {
	User getByEmailAndPassword(String email, String password);

}
