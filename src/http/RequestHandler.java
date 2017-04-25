package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RequestHandler extends Thread {
	private Socket socket;
	
	public RequestHandler( Socket socket ) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			// get IOStream
			//데이터를 byte데이터를 라인단위로 읽어드리기 위해 선언
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8")); //소켓에 들어있는 인풋정보들을 utf-8형식으로 읽어드린다. 그리고 버퍼에 넣은다.
			OutputStream os = socket.getOutputStream();
			
			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = ( InetSocketAddress )socket.getRemoteSocketAddress();
			consoleLog( "connected from " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() );
			
			
			while(true){
				String line = br.readLine(); //보조스트림 BufferedReader에 있는 byte단위 데이터를 한줄씩 String으로 넣는다.
				if(line==null || "".equals(line)){
					break;
				}
				consoleLog(line); //로그를 보면 요청이 2번 들어온걸 알 수 있다.
			}
			// 예제 응답입니다.
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
			os.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) ); //header
			os.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) ); //header
			os.write( "\r\n".getBytes() ); //body
			os.write( "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할 준비가 된 것입니다.</h1>".getBytes( "UTF-8" ) );

		} catch( Exception ex ) {
			consoleLog( "error:" + ex );
		} finally {
			// clean-up
			try{
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
			} catch( IOException ex ) {
				consoleLog( "error:" + ex );
			}
		}			
	}

	
	public void consoleLog( String message ) {
		System.out.println( "[RequestHandler#" + getId() + "] " + message );
	}
}