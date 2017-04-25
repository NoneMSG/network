package chat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	private static final int SERVER_PORT = 8000;
	public static void main(String[] args) {
		//Writer를 저장할 리스트 생성
		//여러클라이언트가 접속한 상황에서 서버에 메시지를 전달하면 다른 클라이언트들도 메시지를 전달 받기위해
		//브로드 캐스트를 해야한다. 그러기 위해서는 각 클라이언트들의 정보가 필요한데 그것을 writer객체에 저장할 수 있다.
		//또한 이 자료구조는 모든 클라이언트가 공유하고 있어야 하기때문에 서버쪽에서 생성한다.
		List<Writer> writerPool = new ArrayList<Writer>();
		//서버 소켓 변수 생성
		ServerSocket serverSocket = null;
		
		try{
			// 1. 소켓 생성
			serverSocket = new ServerSocket();
			
			// 2. 바인딩
			//서버 호스트 정보 가져오기
			InetAddress inetAddress = InetAddress.getLocalHost();
			
			//호스트 정보 객체에서 주소 받기
			String hostAddress = inetAddress.getHostAddress(); //inetAddress에 들어있는 호스트 정보에서 호스트 주소를 가져와 문자열에 넣는다.
			//InetSocketAddress 객체를 만들어서 InetSocketAdress 객체를 만들어 주소와 포트번호를 가지고 있는 객체를 만든다.
			InetSocketAddress inetSocketAddress = new InetSocketAddress(hostAddress,SERVER_PORT);
			
			//바인딩 시작
			serverSocket.bind(inetSocketAddress); //처음에 선언한 소켓객체에 bind 함수를 사용 함수값으로는 주소,포트번호를 받는다. 
			System.out.println("[서버] : 바인딩 시작, 현재 쓰레드 번호 : "+Thread.currentThread().getId()); //서버의 메인 스레드 클라이언트 접속만을 담당한다. 

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
