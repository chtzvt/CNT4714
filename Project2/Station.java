import java.util.Random;

public class Station implements Runnable {
	private int work;
	public int ID;
	private Conveyor input, output;
	
	public Station(int work, int ID){
		this.ID = ID;
		this.work = work;
	}
	
	public void Input(Conveyor c){
		System.out.printf("Routing Station %d: input connection is set to conveyor number %d%n", this.ID, c.ID);
		this.input = c;
	}
	
	public void Output(Conveyor c){
		System.out.printf("Routing Station %d: output connection is set to conveyor number %d%n", this.ID, c.ID);
		this.output = c;
	}
	
	@Override
	public void run(){
		while(this.work > 0){
			if(input.mutex.tryLock()){
				System.out.printf("Station %d: holds lock on input conveyor %d%n", this.ID, input.ID);
			
				if(output.mutex.tryLock()){
					System.out.printf("Station %d: holds lock on output conveyor %d%n", this.ID, output.ID);
					Pack();
				} else {
					System.out.printf("Station %d: unable to lock output conveyor – releasing lock on input conveyor %d.%n", this.ID, output.ID);
					input.mutex.unlock();
				}
				
				sleepRand();
			}

			if(input.mutex.isHeldByCurrentThread()){
				System.out.printf("Station %d: unlocks input conveyor %d%n", this.ID, input.ID);
				input.mutex.unlock();
			}
			
			if(output.mutex.isHeldByCurrentThread()){
				System.out.printf("Station %d: unlocks output conveyor %d%n", this.ID, output.ID);
				output.mutex.unlock();
			}
			
			sleepRand();
		}
		
		System.out.printf("* * Station %d: Workload successfully completed. * *%n", this.ID);
	}

	private void Pack(){
				this.input.Input(this.ID);
				this.output.Output(this.ID);
				this.work--;
				System.out.printf("Station %d: has %d package groups left to move.%n", this.ID, work);
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
