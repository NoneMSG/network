package chat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	private static final int SERVER_PORT = 8000;
	public static void main(String[] args) {
		List<Writer> writerPool = new ArrayList<Writer>();
		ServerSocket serverSocket = null;
		
		try{
			// 1. 소켓 생성
			serverSocket = new ServerSocket();
			
			// 2. 바인딩
			//해당 호스트 정보
			InetAddress inetAddress = InetAddress.getLocalHost();
			//호스트 정보 객체에서 주소 받기
			String hostAddress = inetAddress.getHostAddress();
			InetSocketAddress inetSocketAddress = new InetSocketAddress(hostAddress,SERVER_PORT);

			//바인딩 시작
			serverSocket.bind(inetSocketAddress);
			System.out.println("[서버] : 바인딩 시작, 현재 쓰레드 번호 : "+Thread.currentThread().getId());

			//3.클라이언트 접속  대기 Accept
			while(true){
				Socket socket = serverSocket.accept();
				
				new ChatServerThread(socket, writerPool).start();
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			
				try {
					if(serverSocket!=null && serverSocket.isClosed()==false)
					serverSocket.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
