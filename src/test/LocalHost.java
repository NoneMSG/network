package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {

	public static void main(String[] args) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			//local host address가 장치마다 다르기 때문에 함수로 구해야한다.
			String hostAddress = inetAddress.getHostAddress();
			System.out.println(hostAddress);
			
			String hostName  = inetAddress.getHostName();
			System.out.println(hostName);
			
			byte[] addresses = inetAddress.getAddress();
			for(int i = 0 ; i <addresses.length; ++i){
				//byte를 2의보수를 이용해서 캐스팅 됨으로 빈 비트공간을 마스킹 해주면
				//값을 찾을 수 있다.
				int address = addresses[i] & 0x000000ff; 
				System.out.print(address);
				if(i<3){
					System.out.print(".");
				}
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
