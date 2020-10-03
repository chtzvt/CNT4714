/*
	Name: Charlton Trezevant
	Course: CNT 4714 Fall 2020
	Assignment title: Project 2 – Multi-threaded programming in Java
*/

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Simulation {
	
	private static String CONFIG_FILE = "config.txt";
	
	static int n_stations;
	static Station[] stations;
	static int[] station_workload;
	static Conveyor[] conveyors;
	
	public static void main(String[] args) {
		
		File infile;
		Scanner inf_scanner;
		try {
			infile = new File(CONFIG_FILE);
			inf_scanner = new Scanner(infile);
		} catch (Exception e) {
			System.out.printf("Unable to open input file '%s', exiting.%n", CONFIG_FILE);
			return;
		}
		
		n_stations = inf_scanner.nextInt();
		station_workload = new int[n_stations];
		conveyors = new Conveyor[n_stations];
		stations = new Station[n_stations];
		
		for(int i = 0; i < n_stations; i++){
			conveyors[i] = new Conveyor(i);
		}
		
		System.out.printf("* * * SIMULATION BEGINS * * *%n%n");
		
		for(int i = 0; i < n_stations; i++){
			station_workload[i] = inf_scanner.nextInt();
			System.out.printf("Routing Station %d has a total workload of %d%n", i, station_workload[i]);
			stations[i] = new Station(i);
		}
		
		System.out.printf("%n%n");
		
		for(int i = 0; i < n_stations; i++){
			stations[i].Input((i == 0) ? conveyors[0] : conveyors[i - 1]);
			stations[i].Output((i == 0) ? conveyors[n_stations - 1] : conveyors[i]);
			stations[i].setWorkload(station_workload[i]);
		}
		
		System.out.printf("%n%n");
		
		ExecutorService simThreadPool = Executors.newFixedThreadPool(n_stations);
		for(int i = 0; i < n_stations; i++){
			try {
				simThreadPool.execute(stations[i]);
			} catch (Exception e) {
				System.out.printf("Error processing station %d ('%s')%n", i, e.getMessage());
			}
		}
	
		simThreadPool.shutdown();
	}
}
