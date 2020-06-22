package org.poc.cpfap.application.repository;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.poc.cpfap.application.model.TransactionDetail;
import org.poc.cpfap.application.request.Transaction;
import org.poc.cpfap.application.request.TransactionJSONPostRequest;

@Transactional
public class TransactionDetailRepository  {
	private Random rand = new Random();
	
	public List<TransactionDetail> findByAccountNumberOrderByIdDesc(long accountNumber, EntityManager em) {
		List<TransactionDetail> tdList = new ArrayList<>();
		try {
			tdList = em.createQuery("select e from TransactionDetail e where e.accountNumber = :acct", TransactionDetail.class).setParameter("acct", Long.valueOf(accountNumber)).getResultList();
		} catch(Exception e) {
			em.clear();
			e.printStackTrace();
		} finally {
		}
		return tdList;
	}

	TransactionDetail findByTransactionIdOrderByIdDesc(String transactionId) {
		TransactionDetail td = new TransactionDetail();
		td.setAccountNumber(78234923702335l);
		td.setAmount(2345l);
		td.setAvailableBalance(86365l);
		td.setCategory("Current");
		td.setStatus("Complete");
		td.setTransactionType("Debit");
		td.setTransactionDateTime(new Date());
		return td;
	}
	
	public Boolean insertTransactionDetails(TransactionJSONPostRequest postRequest, EntityManager em) {
		List<Transaction> transactionList = postRequest.getTransaction();
		for (Transaction tr:transactionList) {
			TransactionDetail td = new TransactionDetail();
			int number = rand.nextInt(999999);
			String tid = String.format("%06d", number);
			td.setTransactionId(tid);
			td.setAccountNumber(postRequest.getAccountNumber());
			td.setAmount(tr.getAmount());
			td.setAvailableBalance(Long.valueOf(tr.getAccountBalance()));
			td.setCategory("Savings");
			td.setStatus("Complete");
			td.setTransactionType(tr.getTransactionType());
			td.setTransactionDateTime(new Date());
			td.setDescription(tr.getTransactionDesc());
			try {
				em.persist(td);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
//		td.setTransactionId("9553");
//		td.setAccountNumber(accountNumber);
//		td.setAmount(6532l);
//		td.setAvailableBalance(8943756l);
//		td.setCategory("Savings");
//		td.setStatus("Complete");
//		td.setTransactionType("Debit");
//		td.setTransactionDateTime(new Date());
//		td.setDescription("Money debited by Cheque");
	}
}
