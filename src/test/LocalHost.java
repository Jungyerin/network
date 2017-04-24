package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {

	public static void main(String[] args) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			
			String hostAddress=inetAddress.getHostAddress();   //어디서 돌릴지 모르기 떄문에 메서도를 이용해서
																//로컬호스트를 불러오는 것이 좋음
			System.out.println(hostAddress);
			
			String hostName=inetAddress.getHostName();
			
			System.out.println(hostName);
			
			byte[] addresses=inetAddress.getAddress();   //byte형으로 넣어서 -값이 나온다.범위를 벗어남(-127~127)
			for(int i=0;i<addresses.length;i++)
			{
				int address=addresses.clone()[i] & 0x000000ff;
				System.out.print(addresses[i]);
				if(i<3){
					System.out.print("."); 
				}
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
