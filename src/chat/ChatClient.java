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

		Socket socket = null;
		Scanner sc = new Scanner(System.in);
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));

			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);

			System.out.print("닉네임>>");

			String id = sc.nextLine();
			pw.println("join:" + id);

			// pw.flush();

			String ack = br.readLine();
			if ("registed".equals(ack)) {
				new ChatClientThread(pw, socket).start();
			}
			while (true) {
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
