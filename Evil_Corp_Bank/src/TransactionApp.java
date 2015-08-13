import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
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

	static Connection conn = null;
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		transaction = new ArrayList<Transaction>();
		list = new ArrayList<Account>();
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
/*
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
				list.add(ac);
			}

		}
/*
		// Loop to perform transactions
		while (true) {
			System.out
					.println("Enter a transaction type: (Check, Card, Deposit or Withdrawl) or -1 to finish");
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
				System.out.println("Enter Date DD\\MM\\YY");
				String date = sc.nextLine();
				while (!Validation.validate_Date(date)) {
					System.out.println("Invalid date! Enter Again: DD\\MM\\YY");
					date = sc.nextLine();
				}
				if (tr.equalsIgnoreCase("c") || tr.equalsIgnoreCase("d")) // deposit
				{
					transaction.add(Transaction.getTransaction(
							Integer.parseInt(account_num), am, date));
				} else // Withdrawal
				{
					am = 0 - am;
					transaction.add(Transaction.getTransaction(
							Integer.parseInt(account_num), am, date));
				}
			}

		}

		sc.close();
		Collections.sort(transaction);
		printTransactions();
		perform_Transactions();

		printAccounts();
		// write back the modified account info
		writeFile();

	*/
		
	
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

        System.out.println("connected");
      
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
					account_object.setAcc_type(result.getString("ACC_TYPE"));
					account_object.setAcc_number(Integer.parseInt(result.getString("ACC_NUMBER")));
	            	list.add(account_object);
	            	
	        }
	        } catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		
	}
	

}