package viewmodel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import model.Model;

public class SokobanServer{

	private int port;
	private ForkJoinPool tPool;
	private boolean stop = false;
	private Model model;
	private ServerSocket server;
	private List<Socket> sockets;

	public SokobanServer(Model model, int port){
		this.port = port;
		this.model = model;
		sockets = new ArrayList<Socket>();
	}



	private void startServer() throws IOException{
		server = new ServerSocket(port);
		System.out.println("Server listening on port " + port);
		tPool = new ForkJoinPool();
		while(!stop){
			Socket aClient = server.accept();
			model.addClient(aClient.getInetAddress().toString());
			sockets.add(aClient);
			tPool.execute(()-> {
				try {
					ClientHandler handler = new SokobanHandler(model);
					handler.handle(aClient.getInputStream(), aClient.getOutputStream(),aClient.getInetAddress().toString());
				}
				catch (IOException | ClassNotFoundException e) {
					try {
						aClient.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					}finally{
					try { aClient.close(); } catch (IOException e) { e.printStackTrace(); }
					model.deleteClient(aClient.getInetAddress().toString());
					sockets.remove(aClient);
				}
			});
		}
	}

	public void start(){
		new Thread(()->{
			try {
				startServer();
			} catch (IOException e) {
				//a Client disconnected
			}
		}).start();
	}


	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void stopServer(){
		stop = true;
		try {
			if(tPool != null){
				tPool.shutdown();
				tPool.awaitTermination(5, TimeUnit.SECONDS);
				tPool.shutdownNow();
			}
			closeAllSockets();
			if(server!=null)
				server.close();
			if(model!=null)
				model.deleteAllClients();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

	private void closeAllSockets(){
		if(sockets!=null){
			sockets.forEach((s)->{
				if(!s.isClosed())
					try {
						s.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			});		
			sockets.clear();
		}
	}


}
