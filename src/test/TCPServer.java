package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class TCPServer {
	// 원래 port번호는 설정 파일을 읽어들인다. 포트는 지정해주어야 한다.
	private static final int SERVER_PORT = 5051;

	public static void main(String[] args) {
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
				// 5.  socket으로부터 IOStream받기
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
				while(true){
					//6. 데이터 읽기 data read
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer);
	
					if (readByteCount == -1) {
						// clinet가 소켓을 닫은 경우
						System.out.println("[server] : disconnected by client");
						break;
					}
					// byte를 문자열로 만들어주는 객체 생성 buffer안에 있는 0번인덱스부터 byte의 개수만큼 utf-8로
					// 인코딩
					String data = new String(buffer, 0, readByteCount, "utf-8");
					System.out.println("[server] received : " + data);
					
					//6. 데이터 쓰기 (data write)
					os.write(data.getBytes("utf-8"));
				}
			}catch(SocketException e){
				//클라이언트가 소켓을 정상적으로 닫지 않고 종료 됐을때
				System.out.println("[server] closed by client");
			}catch (IOException e) {
				e.printStackTrace();
			}finally {
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
