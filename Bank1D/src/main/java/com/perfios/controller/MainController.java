package com.perfios.controller;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.perfios.config.QuartzConfig;
import com.perfios.job.DateTimeJob;

import com.perfios.model.CreditCardTable;
import com.perfios.model.RepayTable;
import com.perfios.model.TransactionTable;
import com.perfios.model.User;
import com.perfios.repository.CreditCardRepository;
import com.perfios.repository.RepayRepository;
import com.perfios.repository.TransactionRepository;
import com.perfios.repository.UserRepository;
import com.perfios.service.UserService;
import com.perfios.web.dto.CreditCardTableDto;
import com.perfios.web.dto.UserRegistrationDto;
@Controller
public class MainController {
	@Autowired
	private UserRepository userRepository;
	@Autowired 
	private UserService userService;
	@Autowired
	private TransactionRepository tr;
	
	@Autowired
	private CreditCardRepository ccr;
	@Autowired
	private RepayRepository rr;
	@Autowired private QuartzConfig quartzConfig;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	@GetMapping("/repay/{id}")
    public String repay(@PathVariable(value = "id") long id, Model model) {

        // get employee from the service
        Optional<RepayTable> repayTable= rr.findById(id);
        RepayTable rt=repayTable.get();
        AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
				SecurityContextHolder.getContext().getAuthentication();
				User user=userRepository.findByEmail(auth.getName());
				CreditCardTable crt=ccr.findByEmail(auth.getName());
		if(user.getBalance()<rt.getRepayableAmount()+1000)
		{
			return"redirect:/repayMoney?lessBal";
		}
		else {
			user.setBalance(user.getBalance()-rt.getRepayableAmount());
			TransactionTable tt=new TransactionTable();
			tt.setAmount(rt.getRepayableAmount());
			crt.setAmountRepayed(crt.getAmountRepayed()+rt.getRepayableAmount());
			crt.setCreditBalance(crt.getCreditBalance()+rt.getRepayableAmount()-1500);
			crt.setRepayableAmount(crt.getRepayableAmount()-rt.getRepayableAmount());
			rt.setAmountRepayed(rt.getRepayableAmount());
			rt.setRepayableAmount(0);
			rt.setIsRepayed("yes");
			userRepository.save(user);
			if(rt.getWarningIssued()==3)
			{
				crt.setWarningIssued(0);
			}
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
			rr.save(rt);
			ccr.save(crt);
			return "redirect:/repayMoney?success";
		}
       
    }
	@GetMapping("/repayMoney")
	public String repay(Model model)
	{
		AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
				SecurityContextHolder.getContext().getAuthentication();
		User user=userRepository.findByEmail(auth.getName());
		model.addAttribute("user",user);
		 model.addAttribute("rt",userService.findByRepayed());
		 System.out.println(userService.findByRepayed());
		return "repayMoney";
	}
	@GetMapping("/cardDetails")
	public String cardDetails(Model model)
	{
		AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
				SecurityContextHolder.getContext().getAuthentication();
		CreditCardTable credit=ccr.findByEmail(auth.getName());
		System.out.println(credit);
		model.addAttribute("credit",credit);
		return "cardDetails";
	}
	@GetMapping("/creditMoney")
	public String creditMoney(Model model)
	{
		AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
				SecurityContextHolder.getContext().getAuthentication();
				User user=userRepository.findByEmail(auth.getName());
		CreditCardTable cct=new CreditCardTable();
		model.addAttribute("cct",cct);
		model.addAttribute("user",user);
		return "creditMoneyForm";
	}
	@PostMapping("/creditMoney")
	public String submitBorrowingDetails(@ModelAttribute("cct")CreditCardTableDto cctDto) {
		System.out.println(LocalDate.now().plusMonths(1));
		AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
				SecurityContextHolder.getContext().getAuthentication();
				User user=userRepository.findByEmail(auth.getName());
				CreditCardTable creditTable=ccr.findByEmail(auth.getName());
				if(cctDto.getCvv()!=creditTable.getCvv())
				{
					return "redirect:/creditMoney?wrongCvv";
				}
				if(cctDto.getCreditUsed()>creditTable.getCreditBalance())
				{
					return "redirect:/creditMoney?lessBalance";
				}
				if(creditTable.getWarningIssued()>=3)
				{
					return "redirect:/creditMoney?repay";
				}
				else {
//					Month currentMonth=LocalDate.now().getMonth();
					creditTable.setCreditUsed(creditTable.getCreditUsed()+cctDto.getCreditUsed());
					creditTable.setCreditBalance(creditTable.getCreditBalance()-cctDto.getCreditUsed());
					creditTable.setRepayableAmount(creditTable.getRepayableAmount()+cctDto.getCreditUsed()+1500);
					creditTable.setTimesCardUsed(creditTable.getTimesCardUsed()+1);
					ccr.save(creditTable);
					RepayTable rt=new RepayTable();
					rt.setAccountNumber(user.getAccountNumber());
					rt.setAmountRepayed(creditTable.getAmountRepayed());
					rt.setCardNumber(creditTable.getCardNumber());
					rt.setCreditUsed(cctDto.getCreditBalance());
					rt.setEmail(user.getEmail());
					rt.setName(user.getFirstName()+" "+user.getLastName());
					rt.setRepayableAmount(cctDto.getCreditUsed()+1500);
					rt.setTransactionDate(LocalDate.now());
					rt.setWarningIssued(0);
					rt.setRepayableDate(LocalDate.now().plusMonths(1));
					rt.setCreditCardTable(creditTable);
					rt.setIsRepayed("No");
					rr.save(rt);
					user.setBalance(user.getBalance()+cctDto.getCreditUsed());
					 userService.saveUser(user);
					 TransactionTable tt=new TransactionTable();
					 tt.setAmount(cctDto.getCreditUsed());
					 tt.setCreditAccount(String.valueOf(user.getAccountNumber()));
					 tt.setDebitAccount("none");
					 tt.setCreditOrDebit("Credit");
					 tt.setBalance(user.getBalance());
					 tt.setTransactionType("Credit Card");
					 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
					   LocalDateTime now = LocalDateTime.now();  
					 tt.setTransTime(dtf.format(now));
					 tt.setUser(user);
					 tr.save(tt);
			return"redirect:/creditMoney?success";
				}
	}
	@GetMapping("/getCreditCard")
	public String getCReditCard(Model model) {
		System.out.println("hello credit");
		AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
		SecurityContextHolder.getContext().getAuthentication();
		User user=userRepository.findByEmail(auth.getName());
		if(user.getCreditCard().equalsIgnoreCase("yes"))
		{
			return "redirect:/?haveCard";

		}
		if(user.getBalance()<10000)
		{
			System.err.println("less balance");
			return "redirect:/?credit";
		}
		else {
			//user.setcreditLimit(user.getBalance()*2);
			CreditCardTable cct=new CreditCardTable();
			model.addAttribute("cct",cct);
		return "creditCardForm";
		}
	}
	@PostMapping("/getCreditCard")
	public String submitCardDetails(@ModelAttribute("cct")CreditCardTableDto cctDto)
	{
		AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
				SecurityContextHolder.getContext().getAuthentication();
				User user=userRepository.findByEmail(auth.getName());
		CreditCardTable cct=new CreditCardTable();
		cct.setAdhaar(cctDto.getAdhaar());
		cct.setAmountRepayed(0);
		Random rnd = new Random();
		long cardNumber = 100000 + rnd.nextInt(900000);
		cct.setCardNumber(cardNumber);
		cct.setCreditBalance(user.getBalance()/2);
		cct.setCreditUsed(0);
		cct.setCvv(cctDto.getCvv());
		cct.setTimesCardUsed(0);
		cct.setName(user.getFirstName()+" "+user.getLastName());
		cct.setPan(cctDto.getPan());
		cct.setRepayableDate(null);
		cct.setRepayableAmount(0);
		cct.setAccountNumber(user.getAccountNumber());
		cct.setEmail(user.getEmail());
		user.setCreditCard("yes");
		
		userRepository.save(user);
		cct.setUser(user);
		ccr.save(cct);
		return "redirect:/?gotCard";
	}
	
	 @GetMapping("/")
	    public String home(Model model) throws IOException, SchedulerException {
		 AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
				    SecurityContextHolder.getContext().getAuthentication();
		 

		 User user=userRepository.findByEmail(auth.getName());
		 model.addAttribute(user);
//		 JobDetail jobDetail = JobBuilder.newJob(DateTimeJob.class)
//					.withIdentity("job","job").storeDurably().build();
//			String triggerJobAt = "*/5 * * * *";
//			Trigger trigger = TriggerBuilder.newTrigger()
//					.withIdentity("trigger","trigger")
//					.withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).build();
//
//			Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
			
		
			//scheduler.getListenerManager().addJobListener( myJobExecutionListener, KeyMatcher.keyEquals(jobKey));
			//scheduler.ListenerManager.AddTriggerListener(new MyJobExecutionListener(), GroupMatcher<TriggerKey>.AnyGroup());
			//scheduler.getListenerManager().addJobListener(MyJobExecutionListener, allJobs());
//			scheduler.scheduleJob(jobDetail, trigger);
//
//			scheduler.start();
	        return "index";
	    }
	 @GetMapping("/resetPin")
	 public String getResetPinForm(Model model)
	 {
		 User user=new User();
		 model.addAttribute("user",user);
		 return "resetPin";
	 }
	 @PostMapping("/resetPin")
	 public String submitRestPinForm(@ModelAttribute("user")UserRegistrationDto ur)
	 {
		 AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
				    SecurityContextHolder.getContext().getAuthentication();
		 User user=userRepository.findByEmail(auth.getName());
		 if(user.getPin()==ur.getBalance())
		 {
			 return "redirect:/resetPin?pin";
		 }
		 user.setPin(ur.getPin());
		 userRepository.save(user);

		 return "redirect:/resetPin?success";
	 }
	 @GetMapping("/deposit")
		public String hello(Model model) {
		 User user=new User();
		 model.addAttribute(user);
			return "depositForm";
		}
	 @PostMapping("/deposit")
	 public String depositAmount(@ModelAttribute("user")UserRegistrationDto registrationDto) {
		 System.out.println("hello deposited");
		 AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
				    SecurityContextHolder.getContext().getAuthentication();
		 User user=userRepository.findByEmail(auth.getName());
		 int balance=registrationDto.getBalance();
		 
		 if(balance<=0)
		 {
			 return "redirect:/deposit?zero";
		 }
		 if(registrationDto.getPin()!=user.getPin())
		 {
			 return"redirect:/deposit?pin";
		 }
		 else {
			 
			 user.setBalance(user.getBalance()+balance);
			 userService.saveUser(user);
			 String crDe="credit";
			 setTransactionTable(user,registrationDto,crDe);
			 
		 return "redirect:/deposit?success";
		 }
	 }
	 //function to set transactionTable for Credit and Withdrawal
	 public void setTransactionTable(User user, UserRegistrationDto registrationDto, String crDe)
	 {
		 TransactionTable tt=new TransactionTable();
		 tt.setAmount(registrationDto.getBalance());
		 if(crDe.equalsIgnoreCase("credit"))
		 {
			 tt.setCreditAccount(String.valueOf(user.getAccountNumber()));
			 tt.setDebitAccount("none");
			 tt.setCreditOrDebit("Credit");
		 }
		 else {
			 tt.setCreditAccount("none");
			 tt.setDebitAccount(String.valueOf(user.getAccountNumber()));
			 tt.setCreditOrDebit("Debit");
		 }
		 
		 tt.setBalance(user.getBalance());
		 
		 tt.setTransactionType("Internet Banking");
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		   LocalDateTime now = LocalDateTime.now();  
		 tt.setTransTime(dtf.format(now));
		 tt.setUser(user);
		 tr.save(tt);
	 }
	 
	 
	 //==============================================================================
	 
	 @GetMapping("/withdraw")
		public String withdrawForm(Model model) {
		 User user=new User();
		 model.addAttribute("user",user);
			return "withdrawalForm";
		}
	 @PostMapping("/withdraw")
	 public String withdrawAMountt(@ModelAttribute("user")UserRegistrationDto registrationDto) {
		 System.out.println("hello deposited");
		 AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
				    SecurityContextHolder.getContext().getAuthentication();
		 int withdrawalAmount=registrationDto.getBalance();
		 int pin=registrationDto.getPin();
		 Random rnd = new Random();
			long accountNumber = 100000 + rnd.nextInt(900000);
			
		 if(withdrawalAmount<=0)
		 {
			 return "redirect:/withdraw?zero";
		 }
		 else {
			 User user=userRepository.findByEmail(auth.getName());
			 int balance=user.getBalance();
			 if(withdrawalAmount>balance)
			 {
				 return "redirect:/withdraw?insufficient";
			 }
			 else if(withdrawalAmount+1000>balance){
				 return "redirect:/withdraw?minBal";
			 }
			 else if(pin!=user.getPin()){
				 return "redirect:/withdraw?pin";
			 }
			 else
			 {
			user.setBalance(balance-withdrawalAmount);
			 userService.saveUser(user);
			 setTransactionTable(user,registrationDto,"debit");
		 return "redirect:/withdraw?success";
			 }
		 }
	 }
	 
	 //==================================================================
	 @GetMapping("/transfer")
		public String transferForm(Model model) {
		 User user=new User();
		 model.addAttribute("user",user);
		 

			return "transferForm";
			
		}
	 @PostMapping("/transfer")
	 public String TranferAmount(@ModelAttribute("user")UserRegistrationDto registrationDto) {
		
		 AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
				    SecurityContextHolder.getContext().getAuthentication();
		 int transferAmount=registrationDto.getBalance();
		 User user=userRepository.findByEmail(auth.getName());
		 if(registrationDto.getPin()!=user.getPin())
		 {
			 return "redirect:/transfer?pin";
		 }
		 if(transferAmount<=0)
		 {
			 return "redirect:/transfer?zero";
		 }
		 else {
			 int balance=user.getBalance();
			 if(transferAmount>balance)
			 {
				 return "redirect:/transfer?insufficient";
			 }
			 else if(transferAmount+1000>balance){
				 return "redirect:/transfer?minBal";
			 }else
			 {
				 System.out.println(registrationDto.getAccountNumber());
			
				 User receiver=userRepository.findByaccountNumber(registrationDto.getAccountNumber());
				 if(receiver==null)
				 {
					 return "redirect:/transfer?noReceiver";
				 }
				 if(receiver.getAccountNumber()==user.getAccountNumber())
				 {
					 return "redirect:/transfer?same";
				 }
				 
				 user.setBalance(balance-transferAmount);
				 receiver.setBalance(receiver.getBalance()+transferAmount);
				 userService.saveUser(user);
				 userService.saveUser(receiver);
				 TransactionTable tt=new TransactionTable();
				 tt.setAmount(registrationDto.getBalance());
				 tt.setDebitAccount(String.valueOf(user.getAccountNumber()));
				 tt.setCreditAccount(String.valueOf(receiver.getAccountNumber()));
				 tt.setCreditOrDebit("Debit");
				 tt.setBalance(user.getBalance());
				 tt.setTransactionType("Internet Banking");
				 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
				   LocalDateTime now = LocalDateTime.now();  
				 tt.setTransTime(dtf.format(now));
				 tt.setUser(user);
				 tr.save(tt);
		 return "redirect:/transfer?success";
			 }
		 }
	 }
	

	 @GetMapping("/transferTable") public String showTransactionTable(Model model) {
		 model.addAttribute("AuditTable",userService.getUserTransaction());
		 
		 return "TransactionTable.html";
	 }
	 
	
}