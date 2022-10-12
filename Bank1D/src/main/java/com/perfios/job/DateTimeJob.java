package com.perfios.job;


import java.time.LocalDate;
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
import com.perfios.model.User;
import com.perfios.repository.CreditCardRepository;
import com.perfios.repository.RepayRepository;
import com.perfios.repository.UserRepository;


@DisallowConcurrentExecution
public class DateTimeJob extends QuartzJobBean {
	@Autowired RepayRepository rr;
	@Autowired CreditCardRepository ccr;
	@Autowired UserRepository ur;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    	List<RepayTable> rt=rr.findAll();
    	for (int i = 0; i < rt.size(); i++) {
    	    LocalDate today=LocalDate.now();
    	    LocalDate due=rt.get(i).getRepayableDate();
    	    boolean isBefore=today.isBefore(due);
    	    boolean isAfter=today.isAfter(due);
    	    System.out.println(isAfter);
    	    if(isAfter && rt.get(i).getIsRepayed().equalsIgnoreCase("no"));
    	    {
    	    	System.out.println("true");
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
