package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.Socket;

public class ChatClientThread extends Thread{
	private BufferedReader bufferedReader;
	
	public ChatClientThread(Writer pw, Socket socket) throws UnsupportedEncodingException, IOException {
		this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
	
	}

	@Override
	public void run() {
		System.out.print(">>");
		try {
			while(true){
			String receivedMsg = this.bufferedReader.readLine();
				System.out.println(receivedMsg);
				if(receivedMsg=="quit"){
					stop();
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("종료");
			//e.printStackTrace();
		}finally{
			try {
				this.bufferedReader.close();
			} catch (IOException e) {
				//System.out.println("종료");
				//e.printStackTrace();
			}
		}
	}

	
	
}
