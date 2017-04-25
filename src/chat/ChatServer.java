package chat;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

	public static final int PORT = 9090;

	public static void main(String[] args) {

		ServerSocket serverSocket = null;
		List<Writer> listWriters = new ArrayList<Writer>();

		try {

			serverSocket = new ServerSocket();

			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress(hostAddress, PORT));
			log("연결 기다림" + hostAddress + ":" + PORT);

			while (true) {
				Socket socket = serverSocket.accept();
				new ChatServerThread(socket, listWriters).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();

			}
		}

	}

	public static void log(String message) {
		System.out.println("[HttpServer#" + Thread.currentThread().getId() + "] " + message);
	}

}
