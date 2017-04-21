package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
			while(true){
				System.out.print(">>");
				String ipAD = sc.next();
				
				if("exit".equals(ipAD)){
					System.out.println("eixt");
					//System.exit(0);
					break;
				}else{
					try {
						InetAddress[] inetAddress = InetAddress.getAllByName(ipAD);
						String stringAdd = null;
						//byte[] addresses=null;
						for(int i = 0 ; i < inetAddress.length; ++i){
							//addresses = inetAddress[i].getAddress();
							stringAdd=inetAddress[i].getHostAddress();
						}
						System.out.print(ipAD+" : ");
						System.out.println(stringAdd);
						
//						System.out.print(ipAD+" : ");
//						for(int i = 0 ; i < addresses.length ; ++i){
//							int ads = addresses[i] & 0x000000ff;
//							System.out.print(ads);
//							if(i<3){
//								System.out.print(".");	
//							}
//						}
						//System.out.println();
						
					} catch (UnknownHostException e) {
						System.out.println("a");
					}
				}
			}
			sc.close();

	}

}
