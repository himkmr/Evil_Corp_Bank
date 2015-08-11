public class Transaction implements Comparable {
	int account_number;

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
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	double transaction_amount;
	String date;

	public static Transaction getTransaction(int acc, double am, String date) {
		Transaction tr = new Transaction();
		tr.setAccount_number(acc);
		tr.setDate(date);
		tr.setTransaction_amount(am);

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
