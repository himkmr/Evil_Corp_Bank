import static org.junit.Assert.*;

import org.junit.Test;


public class AllTests {

	@Test
	public void test_find_Account() {
		assertEquals("Pass",-1,TransactionApp.find_Account(null));
	}
	
	@Test
	public void test_find_Account2() {
		assertEquals("Pass",-1,TransactionApp.find_Account(""));
	}

}
