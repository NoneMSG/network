package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
	private static final String SERVER_IP = "192.168.1.36";
	private static final int SERVER_PORT = 8000;

	public static void main(String[] args) {
		//클라이언트가 사용할 소켓 선언
		Socket socket = null;
		//데이터를 입력하기 위한 스캐너 객체
		Scanner sc = new Scanner(System.in);
		//스트림 변수들
		BufferedReader br =null;
		PrintWriter pw =null;
		try {
			//소켓 선언
			socket = new Socket();
			//서버에 연결하기 위해 서버주소와 서버포트를 소켓에 전달하여 연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			//소켓을 통해 서버로부터 데이터 통신을 위해 스트림 선언
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true); //true옵션이 있다면 자동으로 버퍼에 있는 데이터를 flush를 해준다

			System.out.print("닉네임>>");

			String id = sc.nextLine();
			//서버에 id등록을 위한 요청 데이터를 PrintWriet객체를 통해 전달한다.
			pw.println("join:" + id);

			// pw.flush();
			
			//프로토콜 규약에 맞게 클라이언트에서 요청에 대하여 서버로 부터 응답을 받는다.
			String ack = br.readLine();
			if ("registed".equals(ack)) { //서버로부터 등록이 되었다는 응답을 받으면 클라이언트에서 읽기만을 하기위한 스레드를 만든다.
				new ChatClientThread(br, socket).start();
			}else{sc.close(); return;}
			//클라이언트 메인스레드는 데이터를 전달하는 역할만 한다.
			while (true) {
				//프로토콜 규약에 맞게 데이터를 전달한다.
				String input = sc.nextLine();
				if ("quit".equals(input)) {
					pw.println("quit:");
					break;
				} else {
					//System.out.println("나 :" + input);
					pw.println("msg:" + input);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null && socket.isClosed() == false) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		sc.close();
	}

}
