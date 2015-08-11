import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class TransactionApp {
	static ArrayList<Account> list = null;
	static ArrayList<Transaction> transaction = null;
	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		transaction = new ArrayList<Transaction>();
		// TODO Auto-generated method stub
		list = new ArrayList<Account>();
		Scanner sc = new Scanner(System.in);
		Account ac;
		ObjectInputStream in = null;
		FileInputStream fin = null;
		//read the serialized object if exists
		try {
				fin = new FileInputStream("account_list.ser");
				 in = new ObjectInputStream(fin);
					list = (ArrayList<Account>)in.readObject();
				} catch (Exception e) {
					//list = null;  do nothing if not found, already null
					//e.printStackTrace();
				}
		System.out.println("Current Accounts:");
		for(Account elem:list)
		{
			System.out.println(elem.getName() + ": Account Number  "+ elem.getAcc_number() +" Balance: " + elem.getBalance());
		}
		
		
		while (true) {
		
			boolean flag =false;
			
			System.out.println("Enter an account number or -1 to stop entering accounts:  ");
			String account_num = sc.nextLine();
			
			while(!Validation.validate_Account_Num(account_num))
			{
					
					System.out.println("Enter an account number or -1 to stop entering accounts:  ");
					account_num = sc.nextLine();
					
			}
			
			if (Integer.parseInt(account_num) < 0)
				break;
			else {
			
				for(Account elem:list)
				{
					if(Integer.parseInt(account_num) ==	elem.getAcc_number())
					{
						System.out.println("Account present, balance is: " + elem.getBalance());
						System.out.println("Enter d to delete account, ENTER to continue ");
						String response = sc.nextLine();
						if(response.equalsIgnoreCase("d"))
						{
							list.remove(elem);
							System.out.println("Account removed");
							System.out.println("Current Accounts:");
							for(Account elem2:list)
							{
								System.out.println(elem2.getName() + ": Account Number  "+ elem2.getAcc_number() +" Balance: " + elem2.getBalance());
							}
							flag = true;
							break;
						}
						else
							continue;
				
					}
				
				}
				if(flag)
					continue;
				ac = new Account();
				ac.setAcc_number(Integer.parseInt(account_num));
				System.out.println("Enter the name for the account# "
						+ account_num);
				ac.setName(sc.nextLine());
				System.out.println("Enter the balance for account number# "
						+ account_num);
				ac.setBalance(Double.parseDouble(sc.nextLine()));
				list.add(ac);
			}

		}


		
		// transactions
		while (true) {
			System.out.println("Enter a transaction type: (Check, Card, Deposit or Withdrawl) or -1 to finish");
			String tr = sc.nextLine();
			
			if((!tr.equalsIgnoreCase("-1")) && (!tr.equalsIgnoreCase("w")) && (!tr.equalsIgnoreCase("d")) && (!tr.equalsIgnoreCase("c")))
				continue;
			if(tr.equalsIgnoreCase("-1"))
				break;
				System.out.println("Enter the account# ");
				String account_num = sc.nextLine();
				int acc_index = TransactionApp.find_Account(account_num);
				
				if(acc_index == -1)
				{System.out.println("Invalid Account #, try again"); continue;}
				else
				{
					System.out.println("Balance : " +list.get(acc_index).getBalance());
					System.out.println("Enter Amount: ");
					String db = sc.nextLine();
					while(!Validation.validate_Amount(db))
					{
						System.out.println("Enter Amount: ");
						db = sc.nextLine();
					}
					double am = Double.parseDouble(db);;
				//	sc.nextLine();
					System.out.println("Enter Date DD\\MM\\YY");
					String date = sc.nextLine();
					while(!Validation.validate_Date(date))
					{
						System.out.println("Invalid date! Enter Again: DD\\MM\\YY");
						date = sc.nextLine();
					}
					if(tr.equalsIgnoreCase("c") || tr.equalsIgnoreCase("d"))	//deposit
					{
						transaction.add(Transaction.getTransaction(Integer.parseInt(account_num), am, date));
					}
					else		//Withdrawal
					{
						am = 0 - am;
						transaction.add(Transaction.getTransaction(Integer.parseInt(account_num), am, date));
					}	
				}
				
			} 

		sc.close();
		Collections.sort(transaction);
		for(Transaction elem:transaction)
		{
				System.out.println(elem.getDate() +"  Account # " + elem.getAccount_number() + "   transaction amount:  " +elem.getTransaction_amount());
		}
		perform_Transactions();
		
		for(Account elem:list)
		{
				System.out.println(elem.getName()+"  Account # " + elem.getAcc_number() + "   Balance:  " +elem.getBalance());
		}
		
	
		//write back the modified account info
		ObjectOutputStream out = null;
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream("account_list.ser");
				 out = new ObjectOutputStream(fileOut);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					out.writeObject(list);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		
		
		
	}

	
	public static void perform_Transactions()
	{
		for(Transaction elem: transaction)
		{

			for(Account acc_elem: list)
			{
				if(acc_elem.getAcc_number() == elem.getAccount_number())
				{
					if(elem.getTransaction_amount() < 0)
						acc_elem.withdraw(elem.getTransaction_amount());
					else
						acc_elem.deposit(elem.getTransaction_amount());
					
				}
			}
			
			
		}
		
		
	}
	
	
	public static int find_Account(String acc_num)
	{
		if(acc_num == null || acc_num.equals(""))
			return -1;
		for(Account elem: TransactionApp.list)
		{
			int acc = Integer.parseInt(acc_num);
			if(acc == elem.getAcc_number())
				return list.indexOf(elem);
		}
		
		return -1;	
	}

}
