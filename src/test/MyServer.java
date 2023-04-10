package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer {
	int port;
	ClientHandler ch;
	int numOfThreads;
	boolean stop;
	ExecutorService es;
	
	MyServer(int port,ClientHandler ch, int numOfThreads)
	{
		this.port=port;
		this.numOfThreads=numOfThreads;
		this.ch=ch;
		es = Executors.newFixedThreadPool(numOfThreads);
	}
	
	public void start()
	{
		stop=false;
		new Thread(()->startsServer()).start();

	}
	
	private void startsServer() {
		try {
			ServerSocket server = new ServerSocket(port);
			server.setSoTimeout(1000);
			while(!stop) {
				try {
					Socket client=server.accept();
					es.submit(()->{
						try {
							ch.handleClient(client.getInputStream(), client.getOutputStream());
							ch.close();
							client.close();
						}catch (Exception e) {}
					});
				}catch(SocketTimeoutException e) {}
			}
			server.close();
	}catch(IOException e) {
		e.printStackTrace();
		}
	}

	public void close()
	{
		stop=true;
	}

	
}