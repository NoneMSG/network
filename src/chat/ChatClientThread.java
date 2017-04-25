package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ChatClientThread extends Thread {
	private BufferedReader bufferedReader;

	public ChatClientThread(BufferedReader br, Socket socket) throws UnsupportedEncodingException, IOException {
		// this.bufferedReader = new BufferedReader(new
		// InputStreamReader(socket.getInputStream(),"utf-8"));
		this.bufferedReader = br; //클라이언트 소켓정보에 있는 input스트림을 받아온다

	}

	@Override
	public void run() {
		System.out.print(">>");
		try {
			while (true) {
				//읽기부분은 들어오는 데이터를 읽기만 하면 되기때문에 읽어준다
				String receivedMsg = this.bufferedReader.readLine();
				System.out.println(receivedMsg);
				if (receivedMsg == "quit") {//만약 종료메시지를 반환 받게 된다면 스레드가 끝날 수 있도록 break해준다.
		
					break;
				}
			}
		} catch (IOException e) {
			//System.out.println("종료");
			try {
				this.bufferedReader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// e.printStackTrace();
		} finally {
			try {
				this.bufferedReader.close();
			} catch (IOException e) {
				// System.out.println("종료");
				 e.printStackTrace();
			}
		}
	}

}
