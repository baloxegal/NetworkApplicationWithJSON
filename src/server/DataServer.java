package server;

import java.net.ServerSocket;
import java.net.Socket;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import client.DataClient;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class DataServer {

	public static final Integer PORT = 8888;
	
	public Socket startServer() throws IOException {
		ServerSocket serverSocket = new ServerSocket(PORT);
		Socket clientSocket = serverSocket.accept();
		serverSocket.close();
		return clientSocket;
	}
	
	public static void main(String[] args) throws IOException {
		
		Socket clientSocket = new DataServer().startServer();
		
		InputStream din = new ObjectInputStream(clientSocket.getInputStream());
		
		ObjectMapper objectMapper = JsonMapper.builder().disable(StreamWriteFeature.AUTO_CLOSE_TARGET)
														.disable(StreamReadFeature.AUTO_CLOSE_SOURCE)
														.enable(StreamWriteFeature.FLUSH_PASSED_TO_STREAM)
														.build();
		
		DataClient dataClient = objectMapper.readValue(din, DataClient.class);
		System.out.println("Server has read transfered data: " + dataClient.toString());
		
		Integer sum = 0;
		Float average = null;
		for(var i : dataClient.getData()) {
			if((dataClient.getOption().equals("positive") && i < 0) || (dataClient.getOption().equals("negative") && i > 0))
				i *= -1;
			sum = sum + i;
		}
		average = (float)sum / dataClient.getData().size();
		
		OutputStream dout = new ObjectOutputStream(clientSocket.getOutputStream());
		objectMapper.writeValue(dout, average.toString());
		System.out.println("Server has been returned data: " + average);
				
		DataClient dataClient_1 = objectMapper.readValue(din, DataClient.class);
		System.out.println("Server has read transfered data: " + dataClient_1.toString());
		
		Integer sum_1 = 0;
		Float average_1 = null;
		for(var i : dataClient_1.getData()) {
			if((dataClient_1.getOption().equals("positive") && i < 0) || (dataClient_1.getOption().equals("negative") && i > 0))
				i *= -1;
			sum_1 = sum_1 + i;
		}	
		average_1 = (float)sum_1 / dataClient_1.getData().size();
		
		objectMapper.writeValue(dout, average_1.toString());
		System.out.println("Server has been returned data: " + average_1);		
	}		
}

