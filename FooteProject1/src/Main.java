
/* 
 * Programmer: 	Mitchell Foote
 * Course: 	   	COSC 311, F'23
 * Project:    	1
 * Due date:   	9-19-23
 * Project Description: A menu driven terminal program that can read in text files and create random access files to serve as a
 * database for student records using a fixed byte length file size. Users can create, read, edit and delete student records.
 * Class Description: The following main class handles all functionality other than the data structures and organization handled 
 * by the provided Student class.
 * */
import java.util.*;
import java.io.*;

public class Main {
	
	//shared variable declarations.
	private static Scanner scanner;
	private static int option;
	
	private static Scanner fileIn;
	private static RandomAccessFile raFile;
	private static File file;
	
	private static final int studentRecordSize = 92;
	

	public static void main(String[] args) {
		
		
		System.out.println("Programmer:		Mitchell Foote");
		System.out.println("Course:			COSC 311, F'23");
		System.out.println("Project:		1");
		System.out.println("Due date:		9-19-23\n");
		
		//Open Scanner object to take keyboard input.
		scanner = new Scanner(System.in);
		
		menu();


	}
	
	//Builds random access file from provided text file.
	public static void build() throws IOException {
		
		
		Student student = new Student();
		
		while(fileIn.hasNext()) {
			student.readFromTextFile(fileIn);
			
			student.writeToFile(raFile);
		}
		
		System.out.println("Random access file is built successfully!\n");
		
	}
	
	//Main menu provides interface to the user. Handles user choices
	private static void menu() {
		
		menuHelper();

		//check for valid input.
		try {
			option = scanner.nextInt();
		}
		catch (InputMismatchException ex){
			System.out.println("Non-numeric input\n");
			scanner.nextLine();
		}
		
		//Menu control switch.
		switch (option) {
		
		case 1:
			makeRandomAccess();
			break;
		case 2:
			displayRandomAccess();
			break;
		case 3:
			retrieveRecord();
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
		default:
			System.out.println("Invalid Option\n");
			menu();
			break;
		}
		
	}

	//Breaks Menu loop, closes scanner and exits terminal application.
	private static void exit() {
		
		System.out.println("Bye!");
		scanner.close();
		System.exit(0);
		
	}

	//Marks a record as deleted using a "lazy" deletion method.
	private static void deleteRecord() {
		
		//Check for random access file.
	    if (!checkRAF()) {
	        menu();
	        return;
	    }
		
		
		int recNumb;
		long pos;
		
		try {
			
	        // Get user input and validate.
	        System.out.print("Enter the record number to delete: ");
	        while (!scanner.hasNextInt()) {
	            System.out.println("Input must be integer: ");
	            scanner.next(); 
	        }
	        recNumb = scanner.nextInt();
	        scanner.nextLine();
	        pos = (recNumb - 1) * studentRecordSize;

	        raFile.seek(pos);

	        raFile.writeChars("DELETED"); 
	        
	    } catch (IOException ex) {
	        System.out.println("Error while deleting the record: " + ex.getMessage());
	    }
		
		menu();
		
	}

	//Adds a record to the random access file.
	private static void addRecord() {
		
		//Check for random access file.
	    if (!checkRAF()) {
	        menu();
	        return;
	    }
		
		
		try {
			scanner.nextLine();
	        // Prompt the user to enter data for a new record.
	        System.out.print("Enter first name: ");
	        String firstName = scanner.nextLine();
	        System.out.print("Enter last name: ");
	        String lastName = scanner.nextLine();
	        
	        System.out.print("Enter ID: ");
	        while (!scanner.hasNextInt()) {
	            System.out.println("Input must be integer: ");
	            scanner.next(); 
	        }
	        int studentID = scanner.nextInt();
	        
	        System.out.print("Enter GPA: ");
	        while (!scanner.hasNextDouble()) {
	            System.out.println("Input must be in decimal format: ");
	            scanner.next();
	        }
	        double GPA = scanner.nextDouble();

	        // Create new Student object and populate.
	        Student student = new Student();
	        student.setData(firstName, lastName, studentID, GPA);
	        raFile.seek(raFile.length());
	        student.writeToFile(raFile);

	    } 
		catch (IOException ex) {
	        System.out.println(ex.getMessage());
	    }
		
		menu();
		
	}

	//Edit a record in the random access file.
	private static void modifyRecord() {
		
		//Check for random access file.
	    if (!checkRAF()) {
	        menu();
	        return;
	    }
		
		
		int recNumb;
		long pos;
		
	    try {
	    	
	    	//Validate input.
	    	while (true) {
	    	    System.out.print("Enter a record number: ");
	    	    while (!scanner.hasNextInt()) {
	    	        System.out.println("Invalid input. Enter a valid integer: ");
	    	        scanner.next(); 
	    	    }
	    	    recNumb = scanner.nextInt();
	    	    scanner.nextLine(); 

	    	    if (recNumb < 1 || recNumb > raFile.length() / studentRecordSize) {
	    	        System.out.println("Invalid record number. Enter a valid record number: ");
	    	    } else {
	    	        break; 
	    	    }
	    	}
	    	
	        pos = (recNumb - 1) * studentRecordSize;

	        raFile.seek(pos);
	        Student student = new Student();
	        student.readFromFile(raFile);

	        //Check for deleted records.
	        if (isRecordDeleted(student)) {
	            System.out.println("Can't modify a deleted record\n");
	            menu();
	            return;
	        }
	        
	        int choice;
	        
	        //Editing menu loop.
	        do {
	        	System.out.println(student);
	        	
		        System.out.println("\n1- Change the first name");
		        System.out.println("2- Change the last name");
		        System.out.println("3- Change ID");
		        System.out.println("4- Change GPA");
		        System.out.println("5- Done");
	            System.out.print("Enter your choice: ");
	            
	            choice = scanner.nextInt();
	            scanner.nextLine();

	            //Menu control switch.
	            switch (choice) {
	                case 1:
	                    System.out.print("Enter First Name: ");
	                    String newFirstName = scanner.nextLine();
	                    if (!newFirstName.isEmpty()) {
	                        student.setFirst(newFirstName);
	                    }
	                    break;
	                case 2:
	                    System.out.print("Enter Last Name: ");
	                    String newLastName = scanner.nextLine();
	                    if (!newLastName.isEmpty()) {
	                        student.setLast(newLastName);
	                    }
	                    break;
	                case 3:
	                    System.out.print("Enter Student ID: ");
	                    String newStudentIDStr = scanner.nextLine();
	                    if (!newStudentIDStr.isEmpty()) {
	                        int newStudentID = Integer.parseInt(newStudentIDStr);
	                        student.setID(newStudentID);
	                    }
	                    break;
	                case 4:
	                    System.out.print("Enter GPA: ");
	                    String newGPAstr = scanner.nextLine();
	                    if (!newGPAstr.isEmpty()) {
	                        double newGPA = Double.parseDouble(newGPAstr);
	                        student.setGPA(newGPA);
	                    }
	                    break;
	                case 5:
	                    break;
	                default:
	                    System.out.println("Invalid choice.");
	            }
	        } while (choice != 5);

	        // Seek back to the position and overwrite the original record with the modified record.
	        raFile.seek(pos);
	        student.writeToFile(raFile);
	    }
	    catch (IOException ex) {
	        System.out.println(ex.getMessage());
	    }
		
		menu();
		
	}

	//Search for a record and read out to terminal.
	private static void retrieveRecord() {
		
		//Check for random access file.
	    if (!checkRAF()) {
	        menu();
	        return;
	    }
		
		Student student = new Student();
		int recNumb;
		
		try {
			
			//validate input.
			while (true) {
			    System.out.print("Enter a record number: ");
			    while (!scanner.hasNextInt()) {
			        System.out.println("Invalid input. Enter a valid integer: ");
			        scanner.next(); 
			    }
			    recNumb = scanner.nextInt();
			    scanner.nextLine(); 

			    if (recNumb < 1 || recNumb > raFile.length() / studentRecordSize) {
			        System.out.println("Invalid record number. Enter a valid record number.");
			    } else {
			        break; 
			    }
			}
			
			raFile.seek((recNumb-1) * studentRecordSize);
			student.readFromFile(raFile);
			
			//check for deleted records
	        if (isRecordDeleted(student)) {
	            System.out.println("Can't read deleted record.\n");
	            menu();
	            return;
	        }
			
			System.out.println(student);
		}
		catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		menu();
		
	}

	//Displays selected random access file to terminal starting with 5 records and prompting the user for continuation.
	private static void displayRandomAccess() {
		
		Student student = new Student();
		String raFileName;
		
		System.out.print("Enter the random access file name: ");
		raFileName = scanner.next();
		File raFileCheck = new File(raFileName);
		
		if (!raFileCheck.exists()) {
		    System.out.println("File not found.");
		    menu();
		    return; 
		}
		
		try {
		    raFile = new RandomAccessFile(raFileName, "rw");
		    raFile.seek(0);

		    int count = 0;
		    boolean displayAll = false;

		    while (true) {
		        // Read the record 
		        student.readFromFile(raFile);
		        String record = student.toString();
		        
		        //Check for deleted records.
		        if (record.contains("DELETED")) {
		            continue; 
		        }

		        System.out.println(record);
		        count++;

		        // Check if we've displayed 5 records, and prompt for the next action
		        if (count % 5 == 0) {
		        	
		            System.out.print("Enter N (for next 5 records), A (for all remaining records), M(for main menu): ");
		            char choice = scanner.next().charAt(0); 

		            if (choice == 'N' || choice == 'n') {
		                continue; // Display the next set of records
		            } else if (choice == 'A' || choice == 'a') {
		                displayAll = true; // Display all remaining records
		            } else if (choice == 'M' || choice == 'm') {
		                break; // Return to menu
		            }
		            
		        }
		    }

		    // If not displaying all, skip to the end of the file
		    if (!displayAll) {
		        while (student.toString() != null) {
		            student.readFromFile(raFile);
		        }
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

	//Takes input that provides an input text file and creates a random access file with the desired name.
	private static void makeRandomAccess() {
		
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

	
	//Prints the menu options to terminal.
	private static void menuHelper() {
		
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
		
	}
	
	//Checks for deleted records.
	private static boolean isRecordDeleted(Student student) {
		return student.getFirst().contains("DELETED");
	}
	
	//Checks for random access file
	private static boolean checkRAF() {
		
	    if (raFile == null) {
	        System.out.println("No random access file selected, use option 1 or 2 to begin.");
	        return false;
	    }
	    
	    return true;
		
	}

}