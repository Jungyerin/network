package Util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {

			Scanner scanner = new Scanner(System.in);

			while (true) {
				System.out.print(">>");
				String str = scanner.nextLine();

				if (str.equals("exit")) {
					break;
				} else {
					InetAddress[] inetAddress = InetAddress.getAllByName(str);
	
					for (int i = 0; i < inetAddress.length; i++) {
						System.out.println(inetAddress[i]);
					}
				}

			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

}
