package com.hotelhub.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.hotelhub.models.Booking;
import com.hotelhub.models.Hotel;
import com.hotelhub.models.Room;

public class BookingDao {

	private SessionFactory sessionFactory;

    public BookingDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

//    public List<Booking> findBookingsByRoomAndDates(Room room, Date checkInDate, Date checkOutDate) {
//        try {
//            Session session = sessionFactory.getCurrentSession();
//            Query<Booking> query = session.createQuery(
//                "FROM Booking WHERE room = :room AND ((checkInDate >= :checkInDate AND checkInDate < :checkOutDate) OR (checkOutDate > :checkInDate AND checkOutDate <= :checkOutDate))",
//                Booking.class
//            );
//            query.setParameter("room", room);
//            query.setParameter("checkInDate", checkInDate);
//            query.setParameter("checkOutDate", checkOutDate);
//            return query.getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//            // Handle any exceptions
//            return null;
//        }
//    }
    
    public void save(Booking booking) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(booking);
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

    public Booking findById(Long bookingId) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Booking.class, bookingId);
        } finally {
            session.close();
        }
    }

    public void update(Booking booking) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(booking);
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
    
    


    public void delete(Booking booking) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.remove(booking);
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

    public List<Booking> findAll() {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Booking";
            Query<Booking> query = session.createQuery(hql, Booking.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

//    public Hotel findByName(String name) {
//        Session session = sessionFactory.openSession();
//        try {
//            String hql = "FROM Booking WHERE name = :name";
//            Query<Hotel> query = session.createQuery(hql, Hotel.class);
//            query.setParameter("name", name);
//            return query.uniqueResult();
//        } finally {
//            session.close();
//        }
//    }
	
}
