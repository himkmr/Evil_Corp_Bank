import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;



public class TransactionApp {
	static ArrayList<Account> list = null;
	static ArrayList<Transaction> transaction = null;
	static ObjectInputStream in = null;
	static FileInputStream fin = null;
	static ObjectOutputStream out = null;
	static FileOutputStream fileOut = null;
	static ArrayList<Account> new_list = null;
	static ArrayList<Account> del_list = null;
	static Connection conn = null;
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		int tr_id=0;
		transaction = new ArrayList<Transaction>();
		list = new ArrayList<Account>();
		new_list = new ArrayList<Account>();	//list for new accounts
		del_list = new ArrayList<Account>();	//accounts to remove
		Scanner sc = new Scanner(System.in);
		Account ac = null;

		try {
			connectSQL();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Read accounts to arrayList
		getAccounts();
		//print accounts
		System.out.printf("%20s%15s%15s\n", "Name", "Account #", "Balance");
		for(Account elem: list)
		{
			System.out.printf("%20s%15s%15s\n", elem.getName(), elem.getAcc_number(), elem.getBalance());
		}

		// Loops to add/remove accounts

		while (true) {

			boolean flag = false;
			System.out
					.println("Enter  account number or -1 to stop entering accounts:  ");
			String account_num = sc.nextLine();

			while (!Validation.validate_Account_Num(account_num)) {
				System.out
						.println("Invalid! Enter an account number or -1 to stop entering accounts:  ");
				account_num = sc.nextLine();

			}

			if (Integer.parseInt(account_num) < 0) // EXIT
				break;
			else {

				for (Account elem : list) {
					if (Integer.parseInt(account_num) == elem.getAcc_number()) {
						flag = true;
						System.out.println("Account present, balance is: "
								+ elem.getBalance());
						System.out
								.println("Enter d to delete account, ENTER to continue ");
						String response = sc.nextLine();
						if (response.equalsIgnoreCase("d")) {
							if(elem.getBalance() != 0)
							{
								System.out.println("Cannot close account: Non Zero Balance");
								flag = true;
								break;
							}
							list.remove(elem);
							del_list.add(elem);
							System.out.println("Account removed");
							System.out.println("Current Accounts:");
							for (Account elem2 : list) {
								System.out.println(elem2.getName()
										+ ": Account Number  "
										+ elem2.getAcc_number() + " Balance: "
										+ elem2.getBalance());
							}
							//flag = true;
							break;
						} 

					}

				}
				if (flag)
					continue;

				ac = new Account();
				ac.setAcc_number(Integer.parseInt(account_num));
				System.out.println("Enter the name for the account# "
						+ account_num);
				ac.setName(sc.nextLine());
				System.out.println("Enter the balance for account number# "
						+ account_num);
				ac.setBalance(Double.parseDouble(sc.nextLine()));
				new_list.add(ac);
			}

		}

		// Loop to perform transactions
		while (true) {
			System.out
					.println("Enter a transaction type: (c:Check, dc:Debit Card, d:Deposit or w:Withdrawl) or -1 to finish");
			String tr = sc.nextLine();

			if ((!tr.equalsIgnoreCase("-1")) && (!tr.equalsIgnoreCase("w"))
					&& (!tr.equalsIgnoreCase("d"))
					&& (!tr.equalsIgnoreCase("c")))
				continue;
			if (tr.equalsIgnoreCase("-1"))
				break;
			System.out.println("Enter the account# ");
			String account_num = sc.nextLine();
			
			while (!Validation.validate_Account_Num(account_num)) {
				System.out.println("Invalid! Enter an account number:  ");
						account_num = sc.nextLine();
			}
			
			

			int acc_index = TransactionApp.find_Account(account_num);

			if (acc_index == -1) {
				System.out.println("Invalid Account #, try again");
				continue;
			} else {
				System.out.println("Balance : "
						+ list.get(acc_index).getBalance());
				System.out.println("Enter Amount: ");
				String db = sc.nextLine();
				while (!Validation.validate_Amount(db)) {
					System.out.println("Enter Amount: ");
					db = sc.nextLine();
				}
				double am = Double.parseDouble(db);
				;
				// sc.nextLine();
				System.out.println("Enter Date MM/DD/YY");
				String date = sc.nextLine();
				while (!Validation.validate_Date(date)) {
					System.out.println("Invalid date! Enter Again: MM/DD/YY");
					date = sc.nextLine();
				}

				if(tr.equalsIgnoreCase("d"))
					tr_id =1;
				else if(tr.equalsIgnoreCase("dc"))
					tr_id =4;
				else if(tr.equalsIgnoreCase("c"))
					tr_id =2;
				else if(tr.equalsIgnoreCase("w"))
						tr_id=3;
					

				if (tr.equalsIgnoreCase("c") || tr.equalsIgnoreCase("d")) // deposit
				{
					transaction.add(Transaction.getTransaction(
							Integer.parseInt(account_num), am, date, tr_id));
				} else // Withdrawal
				{
					am = 0 - am;
					transaction.add(Transaction.getTransaction(
							Integer.parseInt(account_num), am, date, tr_id));
				}
			}

		}

		sc.close();
		Collections.sort(transaction);
		printTransactions();
		perform_Transactions();

		printAccounts();
		// write back the modified account info


		removeAccounts();
		addAccounts();
		upDateDB();
		
	}

	
	public static void addAccounts(){
        try {
		for(Account elem:new_list)
		{
			String new_accounts ="insert into BANK_ACCT (ACC_NUMBER, NAME, BALANCE)values("+elem.getAcc_number()+ " ,'"+elem.getName()+"',"+elem.getBalance()+")";
			System.out.println(new_accounts);
			PreparedStatement preStatement = conn.prepareStatement(new_accounts);
			ResultSet result = preStatement.executeQuery();
		}
		
        } catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
      
    }
	
	public static void removeAccounts(){
        try {
		for(Account elem:del_list)
		{
			String new_accounts ="delete from BANK_ACCT where ACC_NUMBER = "+elem.getAcc_number();
		//	System.out.println(new_accounts);
			PreparedStatement preStatement = conn.prepareStatement(new_accounts);
			ResultSet result = preStatement.executeQuery();
		}
		
        } catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
      
    }
	
	
	public static void upDateDB(){
        try {
		for(Account elem:list)
		{
			String set_accounts ="update BANK_ACCT SET BALANCE ="+ elem.getBalance()+ " where ACC_NUMBER = "+elem.getAcc_number();
		//	System.out.println(set_accounts);
			PreparedStatement preStatement = conn.prepareStatement(set_accounts);
			ResultSet result = preStatement.executeQuery();
		}
		

	
		
		for(Transaction elem:transaction)
		{

			String set_transactions = "insert into TRANSACTION (account_number, transaction_amount,transaction_id,transaction_date) values  ("+elem.account_number+","+ elem.transaction_amount+","+ elem.transaction_id+","+ "TO_DATE('"+elem.getDate()+"','mm/dd/yyyy'))";
			// insert the data
			System.out.println(set_transactions);
			PreparedStatement preStatement = conn.prepareStatement(set_transactions);
			ResultSet result = preStatement.executeQuery();
		}
		
		
		
        } catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
      
    }
	
	
	public static void connectSQL() throws SQLException {
		//URL of Oracle database server
        String url = "jdbc:oracle:thin:testuser/password@localhost"; 
        
        //properties for creating connection to Oracle database
        Properties props = new Properties();
        props.setProperty("user", "testdb");
        props.setProperty("password", "password");
      
        //creating connection to Oracle database using JDBC
        conn = DriverManager.getConnection(url,props);

        //System.out.println("connected");
      
    }
	
	public static void getAccounts()
	{
		 String get_accounts ="select * from BANK_ACCT";

	        //creating PreparedStatement object to execute query
	        try {
				PreparedStatement preStatement = conn.prepareStatement(get_accounts);
				ResultSet result = preStatement.executeQuery();
			  
				while(result.next()){
					Account account_object =new Account();
					account_object.setName(result.getString("NAME"));
					account_object.setBalance(Double.parseDouble(result.getString("BALANCE")));
					account_object.setAcc_number(Integer.parseInt(result.getString("ACC_NUMBER")));
	            	list.add(account_object);
	            	
	        }
	        } catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		
	}
	public static void perform_Transactions() {
		for (Transaction elem : transaction) {

			for (Account acc_elem : list) {
				if (acc_elem.getAcc_number() == elem.getAccount_number()) {
					if (elem.getTransaction_amount() < 0)
						acc_elem.withdraw(elem.getTransaction_amount());
					else
						acc_elem.deposit(elem.getTransaction_amount());

				}
			}

		}

	}

	public static int find_Account(String acc_num) {
		if (acc_num == null || acc_num.equals(""))
			return -1;
		for (Account elem : TransactionApp.list) {
			int acc = Integer.parseInt(acc_num);
			if (acc == elem.getAcc_number())
				return list.indexOf(elem);
		}

		return -1;
	}
	
	
	public static void printAccounts() {
		System.out.println("\n---------------ACCOUNTS------------");
		System.out.format("%15s%15s%15s\n", "Name", "Account #", "Balance");
		System.out.format("%15s%15s%15s\n", "----", "--------", "-------");
		for (Account elem : list) {
			System.out.format("%15s%15s%15s\n", elem.getName() , elem.getAcc_number(), elem.getBalance());
		}

	}

	public static void printTransactions() {
		System.out.println("\n---------------TRANSACTIONS--------");
		System.out.format("%25s%25s%25s\n", "Date", "Account #", "$$$$$$$");
		System.out.format("%25s%25s%25s\n", "----", "--------", "-------");
		for (Transaction elem : transaction) {
			System.out.format("%15s%15s%15s\n", elem.getDate(),+elem.account_number,+elem.getTransaction_amount());
		}
	}
	

}