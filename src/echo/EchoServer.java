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
			while (true) {
				Socket socket = serverSocket.accept(); // blocking (listen
														// status)
				new EchoServerReceiveThread(socket).start();

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
