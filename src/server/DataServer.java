package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataServer {
	
	public static final Integer PORT = 8888;
	
	public static Socket start() throws IOException {
		ServerSocket serverSocket = new ServerSocket(PORT);
		Socket clientSocket = serverSocket.accept();
		serverSocket.close();
		return clientSocket;
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Socket clientSocket = DataServer.start();
		ObjectInputStream din = new ObjectInputStream(clientSocket.getInputStream());
		JSONObject clientData = (JSONObject) din.readObject();
		System.out.println("Server is reading transfered data: " + clientData);
		List<Integer> clientDataFormated = new ArrayList<>();
		String str = "";
//		for(var s : clientData.toCharArray()) {
//			if(s != ',' && s != ' ')
//				str = str + s;
//			else if(s == ','){
//				clientDataFormated.add(Integer.parseInt(str));
//				str = "";
//			}
//		}
		Integer average = null;
		for(int i = 0, sum = 0;  i < clientDataFormated.size(); i++) {
			sum = sum + clientDataFormated.get(i);
			if(i == clientDataFormated.size() - 1)
				average = sum / i;
		}
		ObjectOutputStream dout = new ObjectOutputStream(clientSocket.getOutputStream());
		dout.writeUTF(average.toString());
		dout.flush();
	}
}
