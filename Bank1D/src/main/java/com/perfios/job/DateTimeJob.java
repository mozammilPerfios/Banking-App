package com.perfios.job;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.perfios.model.CreditCardTable;
import com.perfios.model.RepayTable;
import com.perfios.model.TransactionTable;
import com.perfios.model.User;
import com.perfios.repository.CreditCardRepository;
import com.perfios.repository.RepayRepository;
import com.perfios.repository.TransactionRepository;
import com.perfios.repository.UserRepository;


@DisallowConcurrentExecution
public class DateTimeJob extends QuartzJobBean {
	@Autowired RepayRepository rr;
	@Autowired CreditCardRepository ccr;
	@Autowired UserRepository ur;
	@Autowired TransactionRepository tr;
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		List<RepayTable> rt=rr.findAll();
		for (int i = 0; i < rt.size(); i++) {
			LocalDate today=LocalDate.now();
			LocalDate due=rt.get(i).getRepayableDate();
			String isRepayed=rt.get(i).getIsRepayed();

			boolean isAfter=today.isAfter(due);
			if(isRepayed.equalsIgnoreCase("no") && isAfter)
			{
				
				AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
						SecurityContextHolder.getContext().getAuthentication();
				CreditCardTable creditTable=ccr.findByEmail(rt.get(i).getEmail());
				User user=ur.findByEmail(rt.get(i).getEmail());
				if(user.getBalance()+1000<rt.get(i).getRepayableAmount())
				{
					rt.get(i).setRepayableDate(due.plusMonths(1));
					rt.get(i).setRepayableAmount(rt.get(i).getRepayableAmount()+1500);
					rt.get(i).setWarningIssued(rt.get(i).getWarningIssued()+1);
					rr.save(rt.get(i));
					if(rt.get(i).getWarningIssued()>=3)
					{
						creditTable.setWarningIssued(rt.get(i).getWarningIssued());
						System.out.println("not repaying");
					}
				}
				else {
					System.out.println("repaying");

					user.setBalance(user.getBalance()-rt.get(i).getRepayableAmount());
					ur.save(user);
					TransactionTable tt=new TransactionTable();
					tt.setAmount(rt.get(i).getRepayableAmount());
					tt.setBalance(user.getBalance());
					tt.setCreditAccount("none");
					tt.setCreditOrDebit("Debit");
					tt.setDebitAccount(String.valueOf(user.getAccountNumber()));
					tt.setTransactionType("Repayment");
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
					   LocalDateTime now = LocalDateTime.now();  
					 tt.setTransTime(dtf.format(now));
					 tt.setUser(user);
					 tr.save(tt);
					creditTable.setAmountRepayed(creditTable.getAmountRepayed()+rt.get(i).getRepayableAmount());
					creditTable.setCreditBalance(creditTable.getCreditBalance()+rt.get(i).getRepayableAmount()-1500);
					creditTable.setRepayableAmount(creditTable.getRepayableAmount()-rt.get(i).getRepayableAmount());
					rt.get(i).setAmountRepayed(rt.get(i).getRepayableAmount());
					rt.get(i).setRepayableAmount(0);
					rt.get(i).setIsRepayed("yes");   
					rr.save(rt.get(i));

					ccr.save(creditTable);
				}
			}
		}
	}
}
