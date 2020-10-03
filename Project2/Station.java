/*
	Name: Charlton Trezevant
	Course: CNT 4714 Fall 2020
	Assignment title: Project 2 – Multi-threaded programming in Java
*/

import java.util.Random;

public class Station implements Runnable {
	private int work;
	public int ID;
	private Conveyor input, output;
	
	public Station(int ID){
		this.ID = ID;
		this.work = 0;
	}
	
	public void Input(Conveyor c){
		System.out.printf("Routing Station %d: input connection is set to conveyor number C%d%n", this.ID, c.ID);
		this.input = c;
	}
	
	public void Output(Conveyor c){
		System.out.printf("Routing Station %d: output connection is set to conveyor number C%d%n", this.ID, c.ID);
		this.output = c;
	}
	
	public void setWorkload(int work){
		System.out.printf("Routing Station %d: Workload set. Station %d has a total of %d package groups to move.%n", this.ID, this. ID, this.work);
		this.work = work;
	}
	
	@Override
	public void run(){
		while(this.work > 0){
			if(input.mutex.tryLock()){
				System.out.printf("Routing Station %d: holds lock on input conveyor number C%d%n", this.ID, input.ID);
			
				if(output.mutex.tryLock()){
					System.out.printf("Routing Station %d: holds lock on output conveyor number C%d%n", this.ID, output.ID);
					Pack();
				} else {
					System.out.printf("Routing Station %d: unable to lock output conveyor – releasing lock on input conveyor number C%d.%n", this.ID, output.ID);
					input.mutex.unlock();
				}
				
				sleepRand();
			}

			if(input.mutex.isHeldByCurrentThread()){
				System.out.printf("Routing Station %d: unlocks input conveyor number C%d%n", this.ID, input.ID);
				input.mutex.unlock();
			}
			
			if(output.mutex.isHeldByCurrentThread()){
				System.out.printf("Routing Station %d: unlocks output conveyor number C%d%n", this.ID, output.ID);
				output.mutex.unlock();
			}
			
			sleepRand();
		}
		
		System.out.printf("* * Station %d: Workload successfully completed. * *%n%n", this.ID);
	}

	private void Pack(){
				System.out.printf("Routing Station %d: *** Now moving packages. ***%n", this.ID);
				this.input.Input(this.ID);
				this.output.Output(this.ID);
				this.work--;
				System.out.printf("Routing Station %d: has %d package groups left to move.%n%n%n", this.ID, work);
	}

	private void sleepRand(){
		try {
			Random gen = new Random();
			Thread.sleep(1000 * gen.nextInt(3));
		} catch (Exception e) {
			System.out.printf("thread sleep exception: %s%n", e.getMessage());
		}
	}
}
