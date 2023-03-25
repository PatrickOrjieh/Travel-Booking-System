package Application.JunitTests;

import Application.DAOs.MySqlPaymentDao;
import Application.DAOs.PaymentDaoInterface;
import Application.DTOs.Payment;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MySqlPaymentDaoTest {
    @Test
    void testFindAllPayments() {
        PaymentDaoInterface paymentDao = new MySqlPaymentDao();

        try{
            List<Payment> payments = paymentDao.findAllPayments();
            //this is a test to see if the list is empty
            assertNotNull(payments);
            //if the list is not empty, then the size of the list should be greater than 0 which is true
            assertTrue(payments.size() > 0);
            for (Payment p : payments) {
                System.out.println(p);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //this is a test to see if the payment is found by id
    @Test
    void testFindPaymentById(){
        PaymentDaoInterface paymentDao = new MySqlPaymentDao();
        int paymentId = 1;
        try{
            Payment payment = paymentDao.findPaymentById(paymentId);
            //this is a test to see if the payment is found by id
            //if the payment is found by id, then the payment should not be null
            assertNotNull(payment);
            System.out.println(payment);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //this is a test to see if the payment is deleted by id
    @Test
    void testDeletePaymentById(){
        PaymentDaoInterface paymentDao = new MySqlPaymentDao();
        int paymentId = 15;
        try{
            boolean deleted = paymentDao.deletePaymentById(paymentId);
            //this is a test to see if the payment is deleted by id
            //if the payment is deleted by id, then the payment should be true
            assertTrue(deleted);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //this is a test to see if the payment is inserted
    @Test
    void testInsertPayment() {
        PaymentDaoInterface paymentDao = new MySqlPaymentDao();
        Payment payment = new Payment();
        payment.setBooking_id(2);
        payment.setPayment_date("2020-12-12");
        payment.setAmount_paid(100.00);
        payment.setMethod("cash");

        try {
            Payment insertedPayment = paymentDao.insertPayment(payment);
            //this is a test to see if the payment is inserted
            //if the payment is inserted, then the payment should not be null
            assertNotNull(insertedPayment);
            System.out.println(insertedPayment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}