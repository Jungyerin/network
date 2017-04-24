package time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServer {
	
	private static final int PORT = 7070; 

	public static void main(String[] args) {
		DatagramSocket datagramSocket = null;
		try {
			// 1.소켓 생성
			datagramSocket = new DatagramSocket(PORT);

			while (true) {

				// 2.수신 패킷 생성
				DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);

				// 3.데이터 수신 대기
				datagramSocket.receive(receivePacket); // blocking

				// 4.수신
				String message = new String(receivePacket.getData(), 0, receivePacket.getLength(), "utf-8");
				// 0부터 패킷의 사이즈 만큼 데이터를 메세지에 저장

				if(message.equals(""))
				{
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
					message = format.format(new Date());
					System.out.println("[UDP Echo Server] received : "+ message );
				}
				else
				{
					System.out.println("[UDP Echo Server] received : " + message);
				}

				// 5.에코잉
				byte[] sendData = message.getBytes("utf-8");
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
						receivePacket.getSocketAddress());

				datagramSocket.send(sendPacket);
			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (datagramSocket != null && datagramSocket.isClosed() == false)
				datagramSocket.close();
		}

	}

}
