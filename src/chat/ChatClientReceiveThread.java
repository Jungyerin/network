package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ChatClientReceiveThread extends Thread {

	private BufferedReader br;

	public ChatClientReceiveThread(BufferedReader br) {
		this.br = br;
	}

	@Override
	public void run() {

		try {

			while (true) {
				String message = br.readLine();
				if (message == null) {
					break;
				}

				System.out.println(message);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void log(String message) {
		System.out.println("[HttpServer#" + Thread.currentThread().getId() + "] " + message);

	}

}
