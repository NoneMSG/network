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
		this.bufferedReader = br;

	}

	@Override
	public void run() {
		System.out.print(">>");
		try {
			while (true) {
				String receivedMsg = this.bufferedReader.readLine();
				System.out.println(receivedMsg);
				if (receivedMsg == "quit") {
					stop();
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
				// e.printStackTrace();
			}
		}
	}

}
