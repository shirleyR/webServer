package com.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private boolean isShutDown=false;
	private ServerEventHandler handler;
	private InetSocketAddress add;
	public Server(String root,int workerThread, InetSocketAddress address){
		add=address;
		handler=new ServerEventHandler(root,workerThread);
	}
	public static void main(String args[]){
		int worker=4;
		InetSocketAddress add=new InetSocketAddress("localhost",9000);
		String root="WEB-INF/web.xml";
		Server server=new Server(root,worker,add);
		server.start();
	}
	public void start(){
		try{
			Selector selector=Selector.open();
			ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			ServerSocket ss=serverSocketChannel.socket();
			ss.bind(add);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("Server start");
			//this.receive03(selector);
			// when to load servlet
			this.receive04(selector);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void stop(){
		isShutDown=true;
	}
	public void receive04(Selector selector){
		try{
			while(!isShutDown){
				int count=selector.select();
				if(count>0){
					Iterator it = selector.selectedKeys().iterator();
					while (it.hasNext()) {

						System.out.println("key ACc");
						SelectionKey key = (SelectionKey) it.next();
						if(key.isAcceptable()){
							System.out.println("Accept");
							handler.acceptNewRequest(selector, key);
//							SocketChannel sc=((ServerSocketChannel)key.channel()).accept();
//							sc.configureBlocking(false);
//							System.out.println(" client "+sc.socket().getInetAddress().getHostAddress());
//							sc.register(selector, SelectionKey.OP_READ);
//							
						}else if(key.isReadable()){
							handler.readDataFromSocket(key);
						}
						it.remove();
					}
//					for(SelectionKey key:keys){
//						if(key.isAcceptable()){
//							System.out.println("Accept");
//							handler.acceptNewRequest(selector, key);
////							SocketChannel sc=((ServerSocketChannel)key.channel()).accept();
////							sc.configureBlocking(false);
////							System.out.println(" client "+sc.socket().getInetAddress().getHostAddress());
////							sc.register(selector, SelectionKey.OP_READ);
////							
//						}else if(key.isReadable()){
//							handler.readDataFromSocket(key);
//						}else if(key.isWritable()){
////							ByteBuffer byteBuffer=(ByteBuffer)key.attachment();
////							byteBuffer.flip();
////							SocketChannel sc=(SocketChannel)key.channel();
////							sc.write(byteBuffer);
////							if(byteBuffer.hasRemaining()){
////								key.interestOps(SelectionKey.OP_READ);
////								
////							}
////							byteBuffer.compact();
//							
//						}
//					}
				}
			}
		}catch(IOException e){
			
			e.printStackTrace();
			isShutDown=true;
		}
	}
	public void receive03(Selector selector){
		try{
			while(!isShutDown){
				int count=selector.select();
				if(count>0){
					Set<SelectionKey> keys=selector.selectedKeys();
					for(SelectionKey key:keys){
						if(key.isAcceptable()){
							SocketChannel sc=((ServerSocketChannel)key.channel()).accept();
							sc.configureBlocking(false);
							System.out.println(" client "+sc.socket().getInetAddress().getHostAddress());
							sc.register(selector, SelectionKey.OP_READ);
							
						}else if(key.isReadable()){
							ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
							SocketChannel sc=(SocketChannel)key.channel();
							
							while(true){
								int readBytes=sc.read(byteBuffer);
								if(readBytes>0){
									byteBuffer.flip();
									sc.write(byteBuffer);
									break;
								}
							}
							sc.close();
						}else if(key.isWritable()){
							ByteBuffer byteBuffer=(ByteBuffer)key.attachment();
							byteBuffer.flip();
							SocketChannel sc=(SocketChannel)key.channel();
							sc.write(byteBuffer);
							if(byteBuffer.hasRemaining()){
								key.interestOps(SelectionKey.OP_READ);
								
							}
							byteBuffer.compact();
							
						}
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
			isShutDown=true;
		}
	}
	public void receive(ServerSocket sock){
		try{
			while(!isShutDown){
				Socket client=sock.accept();
				new Thread(new Dispatch(client)).start();
			}
		}catch(IOException e){
			e.printStackTrace();
			isShutDown=true;
		}
	}
	
//	public void receive02(ServerSocket sock){
//		try{
//			Socket client=sock.accept();
//			Servlet servlet=new Servlet();
//			Request request=new Request(client.getInputStream());
//			Response response=new Response(client.getOutputStream());
//			
//			servlet.service(request, response);
//			response.pushToClient(200);
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//	}
	public void receive01(ServerSocket sock){
		try{
			Socket client=sock.accept();
			// receive message from Browser
			BufferedReader br=new BufferedReader(new InputStreamReader(client.getInputStream()));
			StringBuilder httpMessage=new StringBuilder();
			while(br.read()!=-1){
				httpMessage.append(br.readLine());
				httpMessage.append("\r\n");
			}
			System.out.println(httpMessage.toString());
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
