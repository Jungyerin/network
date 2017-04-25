package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import echo.EchoServerReceiveThread;

public class ChatClient {

	private static final String SERVER_IP = "192.168.1.32";
	private static final int SERVER_PORT = 9090;

	public static void main(String[] args) {
		Scanner scanner = null;
		Socket socket = null;

		try {

			scanner = new Scanner(System.in); // 2-1. 스캐너 자리
			socket = new Socket();
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));

			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);

			System.out.print("닉네임>>");
			String nickname = scanner.nextLine();
			pw.println("join:" + nickname);
			pw.flush();
			br.readLine(); // 1.br을 안읽어줬었음

			// String str = br.readLine();
			// System.out.println(str);

			Thread thread = new ChatClientReceiveThread(br);
			thread.start(); // 2.스레드를 동작 시켜줌

			while (true) {

				System.out.print(">>");
				String message = scanner.nextLine();
				if ("quit".equals(message) == true) { // 3.조건문에 true;;;
					pw.println("quit");
					pw.flush(); // 4.ack!ㄴㄴ
					break;
				} else {

					pw.println("message:" + message);
					pw.flush(); // 5.ack

				}
			}

		} catch (IOException e) {
			log("error : " + e);
		} finally {

			// 자원정리
			try {
				if (socket != null && socket.isClosed()) {
					socket.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			scanner.close();
		}

	}

	private static void log(String message) {
		System.out.println("[HttpServer#" + Thread.currentThread().getId() + "] " + message);

	}
}
