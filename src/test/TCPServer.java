package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {

	// private static final String SERVER_IP="";
	private static final int SERVER_PORT = 5050;

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
			Socket socket = serverSocket.accept(); // blocking

			// 4. 연결 성공
			InetSocketAddress remoteAddress = (InetSocketAddress) socket.getRemoteSocketAddress();

			// 상대편의 어드레스를  가져오는  것(클라이언트어드레스) invetsocketaddress가  return값보다  자식이기  때문에  다운캐스팅을  해줘야함.
			int remoteHostPort = remoteAddress.getPort();
			String remoteHostAddress = remoteAddress.getAddress().getHostAddress(); 
			// remote어드레스가  소켓  어드레스(포트와  ip두개가  있기 떄문에 ip를 가져오기 위해서는 전체 주소에서 ip만 가져옴)

			System.out.println("[server] conneted from cilent from" + remoteHostAddress + ":" + remoteHostPort);

			try {
				// 5.ioStream 받아오기
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
				while(true){
					// 6. 데이터 읽기
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer); // blokcking

					if (readByteCount <= -1) {
						// 클라이언트가 소켓을 닫은 경우
						System.out.println("[server] disconnected by client");
						break;
						//return;
					}

					String data = new String(buffer, 0, readByteCount, "utf-8");
					System.out.println("[server] received : " + data);

					// 7. 데이터 쓰기
					os.write(data.getBytes("utf-8"));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {

				try {
					if (socket != null && socket.isClosed()) {
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}catch(SocketException e){
			//클라이언트가 소켓을 정상적으로 닫지않고 종료한 경우
			System.out.println("[server] closed by client");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
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
