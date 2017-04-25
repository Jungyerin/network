package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.omg.CORBA.Request;

public class ChatServerThread extends Thread {

	private String nickname;
	private Socket socket;
	private List<Writer> listWriters;
	PrintWriter printwriter = null;
	BufferedReader bufferedReader = null;

	public ChatServerThread(Socket socket, List<Writer> listWriters) {
		this.socket = socket;
		this.listWriters = listWriters;
	}

	@Override
	public void run() {

		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			printwriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
					true);
			String request = null;
			while (true) {
				request = bufferedReader.readLine();
				if (request == null) {
					ChatServer.log("클라이언트로부터 연결 끊김");
					doQuit(printwriter);
					break;
				}

				String[] tokens = request.split(":");

				if ("join".equals(tokens[0])) {
					doJoin(tokens[1], printwriter);
				} else if ("message".equals(tokens[0])) {
					doMessage(tokens[1]);
				} else if ("quit".equals(tokens[0])) {
					doQuit(printwriter);
					break;
				} else {
					ChatServer.log("에러:알수 없는 요청(" + tokens[0] + ")");
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void doQuit(PrintWriter printwriter) {
		removeWriter(printwriter);
		String data = nickname + "님이 퇴장하였습니다.";
		broadcast(data);

	}

	private void removeWriter(Writer writer) {
		/* 현재 스레드의 writer를 Writer pool에서 제거 */
		synchronized (listWriters) {
			listWriters.remove(writer);
		}

	}

	private void doMessage(String string) {
		/* message:하이^^\r\n */
		printwriter.println(string + "\r\n");
		broadcast(nickname + ":" + string); // 1. 메세지 출력이 브로드캐스트!

	}

	private void doJoin(String nickname, Writer writer) {
		this.nickname = nickname;
		String data = nickname + "님이 입장하였습니다.";
		broadcast(data);
		addWriter(writer);
		printwriter.println("join:ok");
		printwriter.flush();

	}

	private void addWriter(Writer writer) {
		synchronized (listWriters) {
			listWriters.add(writer);
		}
	}

	private void broadcast(String data) {
		synchronized (listWriters) {
			for (Writer writer : listWriters) {
				PrintWriter printWriter = (PrintWriter) writer;
				printWriter.println(data);
				printWriter.flush();
			}

		}
	}

	public void log(String message) {
		System.out.println("[HttpServer#" + Thread.currentThread().getId() + "] " + message);
	}

}
