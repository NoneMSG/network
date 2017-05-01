package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.List;

/* 프로토콜 
 * (join:) 이름등록
 * (msg:) 메시지전달
 * (quit:) 종료
 * */

public class ChatServerThread extends Thread {
	private Socket socket;
	private String id;
	private List<Writer> writerPool = null;

	public ChatServerThread(Socket socket, List<Writer> writerPool) {
		this.socket = socket;
		this.writerPool = writerPool;
	}

	@Override
	public void run(){
		//서버의 서브 스레드로 클라이언트들과의 통신을 맡은다. 
		
		//보조스트림인 BufferedReader, PrintWriter를 만든다.
		BufferedReader br=null;
		PrintWriter pw =null;
		try {
			//주스트림인 InputStreamReader, OutputStreamWriter를 만들고 소켓에다가 정보를 전달 할 수 있다록 만든다.
			 br = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "utf-8"));
			 pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "utf-8"), true);

			while (true) {
				// 요청받기
				String request = br.readLine(); //bufferedReader로부터 데이터를 한 줄 읽어온다.
				if (request == null) {
					System.out.println("클라이언트로 부터 끊김");
					break;
				}
				String[] tokens = request.split(":"); //프로토콜을 걸러주기 위해 tokens에 값을 나눠넣는다.
				// 프로토콜에 따른 처리
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
					// error 프로토콜 이외의 메시지를 전달 받았을때
					System.out.println("알수없는 요청");
					return;
				}
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		} finally {
			try {
				//자원정리
				br.close();
				pw.close();
				if (socket != null && socket.isClosed() == false)
					socket.close();
			} catch (IOException e) {
			
				e.printStackTrace();
			}
		}
	}

	private void shutDown(Writer pw) {
		String data = id + "님이 퇴장 했습니다."; 
		spreadMsg(data);//퇴장한 사실을 다른 클라이언트에 알리기 위함
		removeWriter(pw); //실제 writerlist에서 클라이언트 정보 삭제를 위함
	}

	private void removeWriter(Writer pw) {
		synchronized (writerPool) { //동기화의 이유는 데이터가 동일함을 유지하기 위함이다.
//			int count = writerPool.size(); //현재 리스트의 크기
//			for (int i = 0; i < count; ++i) {// 만큼 돌면서
//				if (writerPool.get(i) == pw) { //같은 삭제요청이 들어온 객체를 찾아
//					writerPool.remove(i);//지운다.
//				}
//			}
			writerPool.remove(pw); //객체자신 찾아서 지우기 가능
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
		addWriter(pWriter); //클라이언트 객체 등록
	}

	private void addWriter(Writer writer) {
		synchronized (writerPool) { //데이터 동기화 추가될때 다른 동작을 못하도록.
			writerPool.add(writer); //리스트에 추가
		}
	}
}