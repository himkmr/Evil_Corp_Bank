import java.io.Serializable;

public class Account implements Serializable {
	private int acc_number;
	private double balance;
	private String name;
	private String acc_type;

	
	// test comment for git paired
	// change account
	
	public String getAcc_type() {
		return acc_type;
	}

	public void setAcc_type(String acc_type) {
		this.acc_type = acc_type;
	}

	public int getAcc_number() {
		return acc_number;
	}

	public void setAcc_number(int acc_number) {
		this.acc_number = acc_number;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void withdraw(double amount) {
		balance = balance + amount;
		if (balance < 0)
			balance = balance - 35;

	}

	public void deposit(double amount) {
		balance = balance + amount;
	}

}
