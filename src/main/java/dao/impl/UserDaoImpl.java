package main.java.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import main.java.dao.interfaces.UserDao;
import main.java.model.User;
import main.java.util.HibernateUtil;

public class UserDaoImpl implements UserDao{
	
	public User getByEmailAndPassword(String email, String password){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Query query = session.createQuery("from user where email = ? and password = ?");
			query.setParameter(0, email);
			query.setParameter(1, password);
			@SuppressWarnings("unchecked")
			List<User> users = query.list();
			if(!users.isEmpty()){
				return users.get(0);
			}
		}finally{
			session.close();
		}
		return null;
		
	}

}
