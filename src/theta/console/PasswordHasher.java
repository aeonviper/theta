package theta.console;

import java.util.Scanner;

import theta.core.Utility;

public class PasswordHasher {

	public static void main(String[] args) {
		System.out.println(Utility.hashPassword(new Scanner(System.in).nextLine()));
	}

}
