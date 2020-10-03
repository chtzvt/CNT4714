/* Name: Charlton Trezevant
Course: CNT 4714 – Fall 2020
Assignment title: Project 1 – Event-driven Enterprise Simulation
Date: Sunday September 13, 2020
*/

public class Nile {

public static void main(String[] args) {
								try {
																UI store = new UI();
																store.Run();
								} catch (Exception e) {
																System.out.printf("Fatal error.%n");
																e.printStackTrace();
								}
}
}
