package main.java.dao.impl;

import java.util.Date;
import java.util.List;

import main.java.dao.interfaces.NoteDao;
import main.java.model.Note;
import main.java.util.HibernateUtil;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class NoteDaoImpl implements NoteDao{
	public void insert(Note note){
        Session session = HibernateUtil.getSessionFactory().openSession();
        note.setCreatedTime(new Date());
        Transaction transaction = session.beginTransaction();
        try{
            session.save(note);
            transaction.commit();
        }catch(Exception exception){
            transaction.rollback();
        }finally{
            session.close();
        }
    }

    public Note getById(Long notesId){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            return (Note)session.get(Note.class,notesId);
        }finally{
            session.close();
        }
    }

    public List<Note> getAll(Long userId){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            Query query = session.createQuery("from note where userid = ?");
            query.setParameter(0,userId);
            return query.list();
        }finally{
            session.close();
        }
    }

    public void update(Note note){
        Session session = HibernateUtil.getSessionFactory().openSession();
        note.setLastUpdatedTime(new Date());
        Transaction transaction = session.beginTransaction();
        try{
            session.update(note);
            transaction.commit();
        }catch(Exception exception){
            transaction.rollback();
        }finally {
            session.close();
        }
    }
    public void delete(Note note){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try{
            session.delete(note);
            transaction.commit();
        }catch(Exception exception){
            transaction.rollback();
        }finally {
            session.close();
        }
    }

}
