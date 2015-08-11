import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

	public static boolean validate_Account_Num(String num) {
		if (num.equals("-1"))
			return true;
		String regex = "[0-9]+";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(num);
		if (matcher.matches())
			return true;
		else
			return false;
	}

	public static boolean validate_Date(String date) {
		String regex = "([0-9])+\\\\([0-9])+\\\\([0-9])+";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(date);
		if (matcher.matches())
			return true;
		else
			return false;
	}

	public static boolean validate_Amount(String num) {
		String regex = "([0-9]+(\\.[0-9]+)?)*";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(num);
		if (matcher.matches())
			return true;
		else
			return false;
	}
}
