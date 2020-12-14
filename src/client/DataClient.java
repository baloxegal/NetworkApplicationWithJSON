package client;

import java.net.InetAddress;
import java.net.Socket;
import server.DataServer;
import java.util.List;
import org.json.JSONObject;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;


public class DataClient {

	private List<Integer> data;
		
	public DataClient(List<Integer> data) {
		this.data = data;
	}
	
	public void getData() throws IOException, ClassNotFoundException{
		Socket clientSocket = new Socket(InetAddress.getLoopbackAddress(), DataServer.PORT);
		ObjectOutputStream dout = new ObjectOutputStream(clientSocket.getOutputStream());
		String out = "";
		for(int i = 0; i < data.size(); i++) {
			out = out + "\"Key"+ i + "\":" + "\"" + data.get(i) + "\"";
			if(i != data.size() - 1)
				out = out + ", ";			
		}
		JSONObject jsonObject = new JSONObject(out);
		System.out.println(jsonObject);
		dout.writeObject(jsonObject);
		System.out.println("Client is transfered data");
		dout.flush();
		ObjectInputStream din = new ObjectInputStream(clientSocket.getInputStream());
		System.out.println("This is responce from server: " + din.readObject());
		clientSocket.close();
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException{		
		List<Integer> forTransmition = List.of(10, 20 , 30 , 40);
		DataClient dataClient = new DataClient(forTransmition);
		dataClient.getData();		
	}
}
