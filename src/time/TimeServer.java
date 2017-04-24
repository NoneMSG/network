package time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServer {
	private static final int PORT = 6065;
	public static void main(String[] args) {
		DatagramSocket socket = null;
		try {
			// 1. socket 생성
			socket = new DatagramSocket(PORT);

			// 2. receivePacket 생성
			DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);

			while (true) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
				String date = format.format(new Date());

				// 3. 수신대기
				socket.receive(receivePacket);
				String message = new String(receivePacket.getData(), 0, receivePacket.getLength(), "utf-8");
				System.out.println("receive msg from client : "+message);
				//if ("time".equals(message)) 
				{
					// 4. sendPacket 생성
					byte[] dateB = date.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(dateB, dateB.length,
							receivePacket.getSocketAddress());
					System.out.println("send msg to client :"+ date);
					socket.send(sendPacket);
				}

			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}
	}

}
