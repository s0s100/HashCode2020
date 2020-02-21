import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
	public static int bookNum;
	public static int libraryNum;
	public static int daysForScanning;

	public static ArrayList<Library> libraries;
	public static ArrayList<Integer> pointsPerBook;

	public static final int NEXT_LINE_VARIABLE = 20;
	public static final String INPUT_FILE_NAME = "c_incunabula.txt";
	public static final String OUTPUT_FILE_NAME = "Solution.txt";

	public static void getDataFromFile() {
		Scanner in = null;
		try {
			File file = new File(INPUT_FILE_NAME);
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		readData(in);
		in.close();
	}

	public static void readData(Scanner in) {
		// reading main data
		bookNum = in.nextInt();
		libraryNum = in.nextInt();
		daysForScanning = in.nextInt();
		String output = "Number of books: " + bookNum + '\n';
		output += "Number of Libraries: " + libraryNum + '\n';
		output += "Days for scanning: " + daysForScanning + '\n';
		System.out.println(output);

		// array with all relations
		pointsPerBook = new ArrayList<Integer>();

		System.out.println("Searching for a points per book.");
		for (long i = 0; i < bookNum; i++) {
			int next = in.nextInt();
			pointsPerBook.add(next);
			output = String.format("[%d,%d]", i, next);
			if (i % NEXT_LINE_VARIABLE == 0 && i > 0) {
				output += '\n';
			}
			if (i != bookNum - 1) {
				output += ", ";
			}
			System.out.print(output);
		}
		System.out.println('\n');

		// adding all the libraries
		libraries = new ArrayList<Library>();

		System.out.println("Searching for a libraries.\n");
		for (int i = 0; i < libraryNum; i++) {
			int bookLength = in.nextInt();
			int signupTime = in.nextInt();
			int shipmentSpeed = in.nextInt();
			Library library = new Library(signupTime, shipmentSpeed);
			library.getBooks(in, bookLength);
			libraries.add(library);
			System.out.println(library.toString());
			System.out.println(library.getBookList());
			System.out.println("Sort books.\n");
			library.sortBookList();
			System.out.println(library.getBookList());
		}
	}

	public static PrintWriter createWriter() throws IOException {
		PrintWriter printWriter = null;
		try {
			File outputFile = new File(OUTPUT_FILE_NAME);
			FileWriter fileWriter = new FileWriter(outputFile);
			printWriter = new PrintWriter(fileWriter);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return printWriter;
	}

	// Working with first solution
	public static ArrayList<OutputClass> solutionOne(PrintWriter writer) {
		// FindingSolution
		boolean searching = true;
		int currentDay = 0;
		String output;
		ArrayList<OutputClass> outputList = new ArrayList<OutputClass>();
		while (searching) {
			int maxScore = -1;
			int selectedLabIndex = -1;
			Library maxSelectedLibrary = null;
			for (int i = 0; i < libraryNum; i++) {
				Library selectedLibrary = libraries.get(i);
				if (!selectedLibrary.isUsed) {
					int result = selectedLibrary.calculateScore(currentDay);
					output = "Library id: " + libraries.get(i).id + '\n';
					output += "Final score: " + result + '\n';
					output += "Current day: " + currentDay + '\n';
					output += "Is not used\n";
					System.out.println(output);
					if (maxScore < result) {
						selectedLabIndex = i;
						maxScore = result;
					}
				}
			}
			if (selectedLabIndex != -1) {
				maxSelectedLibrary = libraries.get(selectedLabIndex);
				maxSelectedLibrary.isUsed = true;
				if (maxScore != 0) {
					output = String.format("Adding library %d to output\n", maxSelectedLibrary.id);
					System.out.println(output);
					OutputClass outputClass = new OutputClass(maxSelectedLibrary.id);
					maxSelectedLibrary.addEveryBook(outputClass, currentDay);
					currentDay += maxSelectedLibrary.signupTime;
					outputList.add(outputClass);
				}
			} else {
				break;
			}
			if (currentDay >= daysForScanning) {
				System.out.println("End of solution");
			}
		}
		return outputList;
	}

	public static void saveSolution(ArrayList<OutputClass> solution, PrintWriter writer) {
		writer.printf("%d\n", solution.size());
		for (int i = 0; i < solution.size(); i++) {
			solution.get(i).outputData(writer);
		}
	}

	public static void main(String[] args) throws IOException {
		// Open file
		System.out.println("Reading main data.\n");
		getDataFromFile();

		// Solution
		System.out.println("Creating outputFile.");
		PrintWriter writer = createWriter();
		System.out.println("Finding optimal solution.\n");
		ArrayList<OutputClass> solution = solutionOne(writer);
		System.out.println("Saving solution.\n");
		saveSolution(solution, writer);
		writer.close();
	}
}
