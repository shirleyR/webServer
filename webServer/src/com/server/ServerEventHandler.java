package com.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;
public class ServerEventHandler {
	private ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
	private ServletContext context;
	private Queue q;
	private int socketConnets=0;
	public  ServerEventHandler(String root,int workerThreads){
		q=new Queue();
		context=new ServletContext();
		ExecutorService pool=Executors.newFixedThreadPool(workerThreads);
		for(int i=0;i<workerThreads;i++)
			pool.execute(new HandlerNIO(q));
	}
	public void disconnectClient(SelectionKey key) throws IOException{
		key.attach(null);
		key.channel().close();
	}
	public void acceptNewRequest(Selector selector,SelectionKey key) throws IOException,ClosedChannelException{
		ServerSocketChannel server=(ServerSocketChannel)key.channel();
		SocketChannel channel=server.accept();
		channel.configureBlocking(false);
		// how to understand this problem
		SelectionKey readKey=channel.register(selector, SelectionKey.OP_READ);
		readKey.attach(new MyClient(readKey,q));
	}
	public void readDataFromSocket(SelectionKey key) throws IOException{
		int count=((SocketChannel)key.channel()).read(byteBuffer);
		if(count>0){
			byteBuffer.flip();
			byte[] data = new byte[count];
			byteBuffer.get(data, 0, count);
			((MyClient)key.attachment()).write(data);
			if(byteBuffer.hasRemaining()){
				key.interestOps(SelectionKey.OP_READ);
			}
		}else{
			key.channel().close();
		}
		byteBuffer.clear();
	}
}
