package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
	private static final String SERVER_IP = "192.168.1.36";
	private static final int SERVER_PORT = 6060;

	public static void main(String args[]) {
		// 키보드연결
		Scanner sc = new Scanner(System.in);
		Socket socket = null;

		try {
			// 1. 소켓 생성
			socket = new Socket();

			// 2. 서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));

			// 3. IOStream 받아오기
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"),true); //true 옵션은 auto flush기능
			
			// 4. 읽기/쓰기
			while(true){
				System.out.print(">>");
				String message = sc.nextLine();
				
				if("exit".equals(message)){
					System.out.println("종료");
					break;
				}
				
				//message 보내기
				pw.println(message);
				
				//echo 받기
				String echoMSG = br.readLine();
				
				if(echoMSG==null){
					System.out.println("[client] disconnceted by server");
					break;
				}
				
				//echo출력
				System.out.println("<<"+echoMSG);
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
			sc.close();
		}
	}
}
