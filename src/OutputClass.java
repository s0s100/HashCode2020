import java.io.PrintWriter;
import java.util.ArrayList;

public class OutputClass {
	public int libraryId;
	public int scannedBooks;
	public int score;
	public ArrayList<Integer> books;

	public OutputClass(int libraryId) {
		this.libraryId = libraryId;
		books = new ArrayList<Integer>();
	}

	public void outputData(PrintWriter writer) {
		writer.printf("%d %d\n", libraryId, scannedBooks);
		for (int i = 0; i < books.size(); i++) {
			// System.out.println(books.get(i)) ;
			writer.print(books.get(i));
			if (i != (books.size() - 1)) {
				writer.print(' ');
			}
		}
		writer.print("\n");
	}
}