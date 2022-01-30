import java.util.Iterator;

public class deneme {

	public static String answer = "";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		answer = "123abc";
		String x = "456";

		System.out.println(answer);
		answer = answer.substring(0,answer.length() - x.length());
		for (int i = 0; i < x.length(); i++) {
			answer = answer + x.charAt(i);
		}
		System.out.println(answer);
	}

}
