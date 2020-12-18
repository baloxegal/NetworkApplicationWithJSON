package client;

import java.net.InetAddress;
import java.net.Socket;
import server.DataServer;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class DataClient{
	
	private List<Integer> data;
	private String option;
	
	public DataClient() {		
	}
		
	public DataClient(List<Integer> data, String option) {
		this.data = data;
		if(option.equals("positive") || option.contentEquals("negative"))
			this.option  = option;
	}	
	
	public List<Integer> getData() {
		return data;
	}

	public void setData(List<Integer> data) {
		this.data = data;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	@Override
	public String toString() {
		return "DataClient [data=" + data + ", option=" + option + "]";
	}
	
	public void getDataForTransfer() throws IOException {
		
		Socket clientSocket = new Socket(InetAddress.getLoopbackAddress(), DataServer.PORT);
		
		OutputStream dout = new ObjectOutputStream(clientSocket.getOutputStream());
		
		ObjectMapper objectMapper = JsonMapper.builder().disable(StreamWriteFeature.AUTO_CLOSE_TARGET)
					  									.disable(StreamReadFeature.AUTO_CLOSE_SOURCE)
					  									.enable(StreamWriteFeature.FLUSH_PASSED_TO_STREAM)
					  									.build();
		
		objectMapper.writeValue(dout, this);
		System.out.println("Client is transfering data: " + this.toString());
		
		InputStream din = new ObjectInputStream(clientSocket.getInputStream());
		String average = objectMapper.readValue(din, String.class);
		System.out.println("Сlient received data from the server: " + average);
		
		DataClient d = new DataClient(List.of(10, 20, -30, -40), "negative");
		
		objectMapper.writeValue(dout, d);
		System.out.println("Client is transfering data: " + d.toString());
		
		String average_1 = objectMapper.readValue(din, String.class);
		System.out.println("Сlient received data from the server: " + average_1);
		
		clientSocket.close();
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException{		
		new DataClient(List.of(10, 20, 30, 41), "positive").getDataForTransfer();
	}
}
