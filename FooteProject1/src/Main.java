//import Student.java;
/* 
 * Programmer: 	Mitchell Foote
 * Course: 	   	COSC 311, F'23
 * Project:    	1
 * Due date:   	9-19-23
 * Description: 
 * */
import java.util.*;
import java.io.*;

public class Main {
	
	//private String optionString;
	private static Scanner scanner;
	private static int option;
	
	private static Scanner fileIn;
	private static RandomAccessFile raFile;
	private static File file;
	
	private static final int studentRecordSize = 92;
	//private static Student student = new Student();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Programmer:		Mitchell Foote");
		System.out.println("Course:			COSC 311, F'23");
		System.out.println("Project:		1");
		System.out.println("Due date:		9-19-23\n");
		
		scanner = new Scanner(System.in);
		
		menu();


	}

	public static void build() throws IOException {
		// TODO Auto-generated method stub
		
		Student student = new Student();
		
		while(fileIn.hasNext()) {
			student.readFromTextFile(fileIn);
			
			student.writeToFile(raFile);
		}
		
		System.out.println("Random access file is built successfully!\n");
		
	}
	
	private static void menu() {
		
		System.out.println("   Menu");
		System.out.println("   ====");
		System.out.println("1: Make a random-access file");
		System.out.println("2: Display a random-access file");
		System.out.println("3: Retrieve a record");
		System.out.println("4: Modify a record");
		System.out.println("5: Add a record");
		System.out.println("6: Delete a record");
		System.out.println("7: Exit");
		System.out.print("\nEnter your choice: ");
		
		try {
			option = scanner.nextInt();
		}
		catch (InputMismatchException ex){
			System.out.println("Non-numeric input\n");
		}
		
		//System.out.println(option);
		
		switch (option) {
		
		case 1:
			makeRandomAccess();
			break;
		case 2:
			displayRandomAccess();
			break;
		case 3:
			int rNum;
			System.out.print("Enter a record number: ");
			rNum = scanner.nextInt();
			retrieveRecord(rNum);
			break;
		case 4:
			modifyRecord();
			break;
		case 5:
			addRecord();
			break;
		case 6:
			deleteRecord();
			break;
		case 7:
			exit();
			break;
			
		}
		
	}

	private static void exit() {
		// TODO Auto-generated method stub
		
		System.out.println("Bye!");
		System.exit(0);
		
	}

	private static void deleteRecord() {
		// TODO Auto-generated method stub
		
	}

	private static void addRecord() {
		// TODO Auto-generated method stub
		
	}

	private static void modifyRecord() {
		// TODO Auto-generated method stub
		
	}

	private static void retrieveRecord(int recNum) {
		// TODO Auto-generated method stub
		Student student = new Student();
		try {
			raFile.seek((recNum-1) * studentRecordSize);
			student.readFromFile(raFile);
			System.out.println(student);
		}
		catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		menu();
		
	}

	private static void displayRandomAccess() {
		// TODO Auto-generated method stub
		
		Student student = new Student();
		String raFileName;
		
		System.out.print("Enter the random access file name: ");
		raFileName = scanner.next();
		
		
		try {
			raFile = new RandomAccessFile(raFileName, "rw");
			raFile.seek(0);
			
			while(student.toString() != null) {
				student.readFromFile(raFile);
				System.out.println(student);
			}
		}
		catch(InputMismatchException ex) {
			System.out.println(ex.getMessage());
			scanner.nextLine();
		}
		catch(FileNotFoundException ex) {
			System.out.println(ex.getMessage());
		}
		catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		menu();
		
	}

	private static void makeRandomAccess() {
		// TODO Auto-generated method stub
		try {
			
			String inputFile, outputFile;
			
			System.out.print("Enter an input file name: ");
			inputFile = scanner.next();
			file = new File(inputFile);
			fileIn = new Scanner(file);
			
			System.out.print("Enter an output file name: ");
			outputFile = scanner.next();
			file = new File(outputFile);
			
			raFile = new RandomAccessFile(file, "rw");
			build();
			

		}
		catch(InputMismatchException ex) {
			System.out.println(ex.getMessage());
			scanner.nextLine();
		}
		catch(FileNotFoundException ex) {
			System.out.println(ex.getMessage());
		}
		catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		menu();
		
	}

}
