package com.server;

import java.util.LinkedList;


public class Queue 
{
	private int waitingThreads = 0;
	private LinkedList list;
	public Queue(){
		list=new LinkedList();
	}
	public synchronized void insert(Object obj)
	{
		System.out.println("E M P");
		list.addLast(obj);
		notify();
	}

	public synchronized Object remove()
	{
		if ( isEmpty() ) {
			System.out.println("wait");
			try	{ waitingThreads++; wait();} 
			catch (InterruptedException e)	{Thread.interrupted();}
			waitingThreads--;
			System.out.println("unlocks");
		}
		return list.removeFirst();
	}

	public boolean isEmpty() {
		return 	(list.size() - waitingThreads <= 0);
	}
}