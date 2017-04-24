package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {

	// private static final String SERVER_IP="";
	private static final int SERVER_PORT = 6060;

	public static void main(String[] args) {

		ServerSocket serverSocket = null;// finally에서 닫아주기 위해서 밖에 선언
		try {
			// 1.서버 소켓 생성
			serverSocket = new ServerSocket();

			// 2.바인딩(binding)
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhostAddress = inetAddress.getHostAddress();
			InetSocketAddress inetSocketAddress = new InetSocketAddress(localhostAddress, SERVER_PORT);
			serverSocket.bind(inetSocketAddress);

			System.out.println("[server] binding" + localhostAddress + ":" + SERVER_PORT);

			// 3. accept(연결 요청을 기다림)

			while (true) {
				Socket socket = serverSocket.accept(); // blocking
				new EchoServerReceiveThread(socket).start();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// 자원정리
				if (serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
