package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.List;

public class ChatServerThread extends Thread {
	private Socket socket;
	private String id;
	private List<Writer> writerPool = null;

	public ChatServerThread(Socket socket, List<Writer> writerPool) {
		this.socket = socket;
		this.writerPool = writerPool;
	}

	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "utf-8"), true);

			while (true) {
				// 요청받기
				String request = br.readLine();
				if (request == null) {
					System.out.println("클라이언트로 부터 끊김");
					break;
				}
				String[] tokens = request.split(":");
				// 프로토콜
				if ("join".equals(tokens[0])) {
					// join 처리 닉네임등록
					System.out.println(tokens[1] + "닉네임등록 진입");
					signUp(tokens[1], pw);
					pw.println("registed");
					System.out.println(tokens[1] + "등록 완료 전송");
					spreadMsg("님 입장했습니다.");
					// break;
				} else if ("msg".equals(tokens[0])) {
					// message처리 브로드캐스트
					System.out.println(id + "메시지수신");
					spreadMsg(tokens[1]);
					System.out.println(id + "메시지완료");
					// break;
				} else if ("quit".equals(tokens[0])) {
					// quit처리 종료
					System.out.println(id + "종료처리");
					shutDown(pw);
					pw.println("quit");
					System.out.println(id + "종료완료");
					break;
				} else {
					// error
					System.out.println("알수없는 요청");
					return;
				}
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

	private void shutDown(Writer pw) {
		String data = id + "님이 퇴장 했습니다.";
		spreadMsg(data);
		removeWriter(pw);
	}

	private void removeWriter(Writer pw) {
		synchronized (writerPool) {
			int count = writerPool.size();
			for (int i = 0; i < count; ++i) {
				if (writerPool.get(i) == pw) {
					writerPool.remove(i);
				}
			}
		}
	}

	private void spreadMsg(String data) {
		synchronized (writerPool) {
			int count = writerPool.size();
			for (int i = 0; i < count; ++i) {
				PrintWriter pwr = (PrintWriter) writerPool.get(i);
				pwr.println("NewMsg's from " + id + " : " + data);
				pwr.flush();
			}
		}
	}

	private void signUp(String id, Writer pWriter) {
		this.id = id;
		addWriter(pWriter);
	}

	private void addWriter(Writer writer) {
		synchronized (writerPool) {
			writerPool.add(writer);
		}
	}
}