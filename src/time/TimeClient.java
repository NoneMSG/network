package time;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Scanner;

public class TimeClient {
	private static final String SERVER_IP = "192.168.1.36";
	private static final int SERVER_PORT = 6065;
	
	public static void main(String[] args) {
		DatagramSocket socket = null;
		Scanner sc = new Scanner(System.in);
		try {
			socket = new DatagramSocket();
			
			while(true){
				System.out.print(">>");
				String msg = sc.nextLine();
				
				if("exit".equals(msg)==true){
					break;
				}
				byte[] sendData = msg.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,new InetSocketAddress(SERVER_IP,SERVER_PORT));
				socket.send(sendPacket);
				
				DatagramPacket receivePacket = new DatagramPacket(new byte[1024],1024);
				socket.receive(receivePacket);
				
				String showMsg = new String(receivePacket.getData(),0,receivePacket.getLength(),"utf-8");
				System.out.println("<< "+showMsg);
			}
			
		} catch(UnsupportedEncodingException e){ 
			e.printStackTrace();
		}catch (SocketException e) {
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		finally{
			if(socket!=null &&socket.isClosed()==false){
				socket.close();
			}
		}
		sc.close();
	}

}
