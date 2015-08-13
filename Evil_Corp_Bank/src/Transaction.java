import java.util.Date;


//comment

public class Transaction implements Comparable {
	int account_number;
	double transaction_amount;
	String transaction_date;
	int transaction_id;
	
	public String getTransaction_date() {
		return transaction_date;
	}

	public void setTransaction_date(String transaction_date) {
		this.transaction_date = transaction_date;
	}

	public int getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(int transaction_id) {
		this.transaction_id = transaction_id;
	}

	public int getAccount_number() {
		return account_number;
	}

	public void setAccount_number(int account_number) {
		this.account_number = account_number;
	}

	public double getTransaction_amount() {
		return transaction_amount;
	}

	public void setTransaction_amount(double transaction_amount) {
		this.transaction_amount = transaction_amount;
	}

	public String getDate() {
		return transaction_date;
	}

	public void setDate(String date) {
		this.transaction_date = date;
	}



	public static Transaction getTransaction(int acc, double am, String date, int id) {
		Transaction tr = new Transaction();
		tr.setAccount_number(acc);
		tr.setDate(date);
		tr.setTransaction_amount(am);
		tr.setTransaction_id(id);
		return tr;
	}

	/*
	 * @Override public int compareTo(Object tr) { String
	 * compare_date=((Transaction)tr).getDate(); return
	 * this.getDate().compareTo(compare_date);
	 * 
	 * }
	 */

	@Override
	public int compareTo(Object tr) {
		double amount = ((Transaction) tr).getTransaction_amount();
		return (int) (this.getTransaction_amount() - amount);

	}

}
