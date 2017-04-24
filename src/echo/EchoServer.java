package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {
	private static final int SERVER_PORT = 6060;

	public static void main(String args[]) {
		ServerSocket serverSocket = null;

		try {
			// 1. 서버소켓 생성 server socket created
			serverSocket = new ServerSocket();

			// 2. 바인딩 (binding)
			// ip주소 받아오기
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localHostAddress = inetAddress.getHostAddress();
			InetSocketAddress inetSocketAddress = new InetSocketAddress(localHostAddress, SERVER_PORT);
			serverSocket.bind(inetSocketAddress);
			System.out.println("[Server] binding " + localHostAddress + " : " + SERVER_PORT);

			// 3. 연결요청 기다림 (Accept)
			Socket socket = serverSocket.accept(); // blocking (listen status)

			// 4. 연결 성공
			InetSocketAddress remoteAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			int remoteHostPort = remoteAddress.getPort();
			String remoteHostAddress = remoteAddress.getAddress().getHostAddress(); // remote
																					// address이다.
			System.out.println("[server] conneted from client " + remoteHostAddress + ":" + remoteAddress.getPort());

			try { // 데이터교환용 소켓에 대한 예외처리
				// 5. socket으로부터 IOStream받기
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"),true); //true 옵션은 auto flush기능
				
				while(true){
					//개행까지 읽어드리는 프로토콜 (규칙)
					String message = br.readLine();
					if(message == null){
						//client close the socket 클라이언트가 소켓 닫음
						System.out.println("[server] disconnected by client.");
						break;
					}
					System.out.println("[server] received : "+message);
					
					//data쓰 기
					//pw.print(message+"\n");
					pw.println(message);
				}
				
			} catch (SocketException e) {
				// 클라이언트가 소켓을 정상적으로 닫지 않고 종료 됐을때
				System.out.println("[server] closed by client");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (socket != null && socket.isClosed() == false)
						socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 자원 정리
			try {
				if (serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
