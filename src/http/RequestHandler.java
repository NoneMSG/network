package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;

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
			
			String request = null;
			while(true){
				String line = br.readLine(); //보조스트림 BufferedReader에 있는 byte단위 데이터를 한줄씩 String으로 넣는다.
				if(line==null || "".equals(line)){
					break;
				}
				if(request==null){ //첫줄을 읽으면 null이 아니게 된다.
					request=line; //1줄만 읽는다.
				}				
			}
			consoleLog(request); 
			
			//요청 분석
			String[] tokens = request.split(" "); //데이터는 공백으로 분류가능하다.
			if("GET".equals(tokens[0])){
				responseStaticResource(os, tokens[1], tokens[2]); //프로토콜과, url
			}else{
				//POST, DELETE, PUT
				//심플 웹 서버에서는 잘못된 요청(Bad Request, 400)로 처리
				response400Error(os,tokens[2]);
			}
			
//			// 예제 응답입니다.
//			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
//			os.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) ); //header
//			os.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) ); //header
//			os.write( "\r\n".getBytes() ); //body
//			os.write( "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할 준비가 된 것입니다.</h1>".getBytes( "UTF-8" ) );

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

	public void responseStaticResource(OutputStream os, String url, String protocol)throws IOException{ //외부로 예외를 던지면 호출한곳에서 처리해야함
			//이런 형식으로 가공이 된다.
			// HTTP/1.0 200 O (\n개행)
			// Content-Type:text/html; charset=utf-8 (\n개행)
			//
			// "contents-body"
		// ./webapp => DocumentRoot
		if("/".equals(url)){
			url="/index.html"; //Welcome file 처리
		}
		File file = new File("./webapp"+url);
		if(file.exists()==false){
			//404 File not found response
			response404Error(os,protocol);
			return ;
		}
		byte[] body = Files.readAllBytes(file.toPath()); //서버내부의 파일들의 경로설정
		String mimeType = Files.probeContentType(file.toPath()); //콘텐츠 타입을 지정해주는 작업
		//header 전송
		os.write( (protocol+" 200 OK\r\n").getBytes("utf-8") );
		os.write(("Content-Type:"+mimeType+"; charset=utf-8\r\n").getBytes("utf-8"));
		os.write("\r\n".getBytes("utf-8"));
		//body 전송
		os.write(body);
	}
	public void response404Error(OutputStream os, String protocol) throws IOException{
		File file = new File("./webapp/error/404.html");
		byte[] body  = Files.readAllBytes(file.toPath());
		
		os.write( (protocol+" 404 File Not Found\r\n").getBytes("utf-8"));
		os.write("Content-Type:text/html; charset=utf-8\r\n".getBytes("utf-8"));
		os.write("\r\n".getBytes("utf-8"));
		os.write(body);
	}
	public void response400Error(OutputStream os, String protocol)throws IOException{
		File file = new File("./webapp/error/400.html");
		byte[] body = Files.readAllBytes(file.toPath());
		
		os.write( (protocol+" 400 Command Error\r\n").getBytes("utf-8") );
		os.write("Content-Type:text/html; charset=utf-8\r\n".getBytes("utf-8"));
		os.write("\r\n".getBytes("utf-8"));
		os.write(body);
	}
	public void consoleLog( String message ) {
		System.out.println( "[RequestHandler#" + getId() + "] " + message );
	}
}