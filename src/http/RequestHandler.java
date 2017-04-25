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
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));      //br을 사용해서 한줄씩 읽음
			OutputStream os = socket.getOutputStream();

			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = ( InetSocketAddress )socket.getRemoteSocketAddress();
			consoleLog( "connected from " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() );
			
			
			String request=null;
			while(true){
				String line=br.readLine();
				if(line==null || "".equals(line))
				{
					break;
				}
				if(request==null)
				{
					request=line;
				}
				
			}
			consoleLog(request);
			
			//요청 분석
			String[] tokens = request.split(" ");
			if("GET".equals(tokens[0])) //요청된 파일을 읽어서 작성
			{
				responseStaticResource(os, tokens[1], tokens[2]);
			}
			else{
				//post, delete, put
				//심플 웹 서버에서는 잘못된 요청(Bas Request, 400)로 처리
				response400Error(os, tokens[2]);
			}
			
			
			// 예제 응답입니다.
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
//			os.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );
//			os.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
//			os.write( "\r\n".getBytes() );
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

	
	public void response400Error(OutputStream os, String protocol) throws IOException{
		File file=new File("./webapp/error/400.html");
		byte[] body=Files.readAllBytes(file.toPath());
		
		os.write((protocol+" 400 Bad Request\r\n").getBytes("utf-8"));
		os.write("Content-Type:text.html; charset=utf-8\r\n".getBytes("utf-8"));
		os.write("\r\n".getBytes("utf-8"));
		os.write(body);
		
	}

	public void responseStaticResource(OutputStream os, String url, String protocol) throws IOException{  //서버에 있는 파일을 요청
		//HTTP/1.0 200 OK
		//Content-Type:text/html; charset=utf-8
		//.webapp => Document Root
		if("/".equals(url))
		{
			url="/index.html"; //welcome file 처리
		}
		File file = new File("./webapp"+url);
		if(file.exists()==false)
		{
			//404(file notfound) response
			response404Error(os,protocol);	
			return;
		}
		
		byte[] body=Files.readAllBytes(file.toPath()); //예외회피
		
		String mimeType=Files.probeContentType(file.toPath()); //css
		
		//header 전송
		os.write((protocol+" "+"200 OK\r\n").getBytes("utf-8"));
		os.write(("Content-Type:"+mimeType+"; charset=utf-8\r\n").getBytes("utf-8"));
		os.write("\r\n".getBytes("utf-8"));
		
		//body 전송
		os.write(body);
	}

	public void response404Error(OutputStream os, String protocol) throws IOException{
		//브라우저 디폴트 화면
		File file=new File("./webapp/error/404.html");
		byte[] body=Files.readAllBytes(file.toPath());
		
		os.write((protocol+" 404 File Not Found\r\n").getBytes("utf-8"));
		os.write("Content-Type:text.html; charset=utf-8\r\n".getBytes("utf-8"));
		os.write("\r\n".getBytes("utf-8"));
		os.write(body);
		
	}

	public void consoleLog( String message ) {
		System.out.println( "[RequestHandler#" + getId() + "] " + message );
	}
}