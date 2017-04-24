package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class EchoClient {
	private static final String SERVER_IP = "192.168.1.32";
	private static final int SERVER_PORT = 6060;

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		Socket socket = null;
		try {
			// 1. 소켓생성
			socket = new Socket();

			// 2. 서버연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));

			// System.out.println("연결성공");

			// 3. IOStream 받아오기
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);

			while (true) {
				System.out.print(">>");
				String message = scanner.nextLine();
				if ("exit".equals(message)) {
					break;
				}

				// message 보내기
				pw.println(message);

				// 에코 받기
				String echoMessage = br.readLine(); // 개행앞까지만 출력
				if (echoMessage == null) // server가 close 한 경우
				{
					System.out.println("[client] disconneted by server");
					break;
				}

				// 출력
				System.out.println("<<" + echoMessage);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null && socket.isClosed() == false) {
					socket.close();

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			scanner.close();
		}
	}

	// String message = scanner.nextLine();

}
