/*
	Name: Charlton Trezevant
	Course: CNT 4714 Fall 2020
	Assignment title: Project 2 – Multi-threaded programming in Java
*/

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Conveyor {
	public int ID;
	public ReentrantLock mutex = new ReentrantLock();
	
	public Conveyor(int ID){
		this.ID = ID;
	}
	
	public void Input(int stationID){
		System.out.printf("Station %d: successfully moves packages into station on input conveyor %d%n", stationID, this.ID);
	}
	
	public void Output(int stationID){
		System.out.printf("Station %d: successfully moves packages out of station on output conveyor %d%n", stationID, this.ID);
	}
}
