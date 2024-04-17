package com.hotelhub.config;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.hotelhub.dao.BookingDao;
import com.hotelhub.models.Booking;
import com.hotelhub.models.User;

public class BookingTree {
    private BookingTreeNode root;

    public BookingTree(SessionFactory sessionFactory) {
    	BookingDao bookingDao = new BookingDao(sessionFactory);
        List<Booking> bookings = bookingDao.findAll();
        this.root = buildBookingTree(bookings);
    }

    private BookingTreeNode buildBookingTree(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            return null;
        }
        System.out.println("bookings --> " + bookings);
        Booking rootBooking = bookings.get(0);
        BookingTreeNode root = new BookingTreeNode(rootBooking);

        for (int i = 1; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            insertBooking(root, booking);
        }

        return root;
    }

    private void insertBooking(BookingTreeNode node, Booking booking) {
        if (booking.getCheckInDate().compareTo(node.getBooking().getCheckInDate()) < 0) {
            if (node.getLeft() == null) {
                node.setLeft(new BookingTreeNode(booking));
            } else {
                insertBooking(node.getLeft(), booking);
            }
        } else {
            if (node.getRight() == null) {
                node.setRight(new BookingTreeNode(booking));
            } else {
                insertBooking(node.getRight(), booking);
            }
        }
    }

    public BookingTreeNode getRoot() {
        return root;
    }

    public List<Booking> searchBookingsByUser(User user) {
        List<Booking> bookings = new ArrayList<>();
        searchBookingsByUserHelper(root, user, bookings);
        System.out.println("inside searchBOOKINGS ---> " + bookings);
        return bookings;
    }

    private void searchBookingsByUserHelper(BookingTreeNode node, User user, List<Booking> bookings) {
        if (node == null) {
            return;
        }
        System.out.println("user details --> " + user.getFirstName());
        if (node.getBooking().getUser().getFirstName().equals(user.getFirstName())) {
            bookings.add(node.getBooking());
        }

        searchBookingsByUserHelper(node.getLeft(), user, bookings);
        searchBookingsByUserHelper(node.getRight(), user, bookings);
    }
}
