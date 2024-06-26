package com.hotelhub.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.hotelhub.models.Hotel;
import com.hotelhub.models.User;

public class HotelDao {

	private SessionFactory sessionFactory;

    public HotelDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Hotel hotel) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(hotel);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Hotel findById(Long hotelId) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Hotel.class, hotelId);
        } finally {
            session.close();
        }
    }

    public void update(Hotel hotel) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(hotel);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    


    public void delete(Hotel hotel) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.remove(hotel);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public List<Hotel> findAll() {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM User";
            Query<Hotel> query = session.createQuery(hql, Hotel.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    public Hotel findByName(String name) {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Hotel WHERE name = :name";
            Query<Hotel> query = session.createQuery(hql, Hotel.class);
            query.setParameter("name", name);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }
	
	
}
