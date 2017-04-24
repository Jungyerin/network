package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerReceiveThread extends Thread {

	private Socket socket;

	public EchoServerReceiveThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		InetSocketAddress remoteAddress = (InetSocketAddress) socket.getRemoteSocketAddress();

		// 상대편의 어드레스를 가져오는 것(클라이언트어드레스) invetsocketaddress가 return값보다 자식이기
		// 때문에 다운캐스팅을 해줘야함.
		int remoteHostPort = remoteAddress.getPort();
		String remoteHostAddress = remoteAddress.getAddress().getHostAddress();
		// remote어드레스가 소켓 어드레스(포트와 ip두개가 있기 떄문에 ip를 가져오기 위해서는 전체 주소에서 ip만
		// 가져옴)

		consoleLog("conneted from cilent from" + remoteHostAddress + ":" + remoteHostPort);

		try {
			// 5.ioStream 받아오기
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);

			while (true) {
				String message = br.readLine(); // blocking
				if (message == null) {
					// 클라이언트가 소켓을 닫음
					consoleLog("disconneted by client");
					break;
				}
				consoleLog("received : " + message);

				// 데이터 쓰기
				// pw.print(message+"\n");
				pw.println(message);

			}

		} catch (SocketException e) {
			// 클라이언트가 소켓을 정상적으로 닫지않고 종료한 경우
			consoleLog("closed by client");
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
		}

	}

	public void consoleLog(String message) {
		System.out.println("[server." + getId() + "]" + message);
	}

}
