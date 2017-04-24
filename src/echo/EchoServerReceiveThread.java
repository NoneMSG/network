package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class EchoServerReceiveThread extends Thread {
	private Socket socket;

	public EchoServerReceiveThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		// 4. 연결 성공
		InetSocketAddress remoteAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
		int remoteHostPort = remoteAddress.getPort();
		String remoteHostAddress = remoteAddress.getAddress().getHostAddress(); // remote
		consoleLog("connected from " +getId()+" "+ remoteHostAddress + ":" + remoteHostPort); // address이다.
		// System.out.println("[server] conneted from client " +
		// remoteHostAddress + ":" + remoteAddress.getPort());

		try { // 데이터교환용 소켓에 대한 예외처리
				// 5. socket으로부터 IOStream받기
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true); // true

			while (true) {
				// 개행까지 읽어드리는 프로토콜 (규칙)
				String message = br.readLine();
				if (message == null) {
					// client close the socket 클라이언트가 소켓 닫음
					System.out.println("[server] disconnected by client.");
					break;
				}
				consoleLog("receive:" + message);
				// System.out.println("[server] received : " + message);
				// data쓰 기
				// pw.print(message+"\n");
				pw.println(message);
			}
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
	}

	private void consoleLog(String message) {
		System.out.println("[server " + getId() + "]" + message);
	}

}
