import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Library {
	public int id;
	public long signupTime;
	public long shipmentSpeed;
	public boolean isUsed;

	public class BookPoint {
		public int id;
		public int value;

		public BookPoint(int id, int value) {
			this.id = id;
			this.value = value;
		}
	}

	public ArrayList<BookPoint> bookList;

	public static int idGenerator = 0;

	public Library(long signupTime, long ShipmentSpeed) {
		id = idGenerator;
		idGenerator++;

		isUsed = false;
		this.signupTime = signupTime;
		this.shipmentSpeed = ShipmentSpeed;

		bookList = new ArrayList<BookPoint>();
	}

	public void getBooks(Scanner in, long bookLength) {
		for (long i = 0; i < bookLength; i++) {
			int newBookId = in.nextInt();
			BookPoint bookPoint = new BookPoint(newBookId, Main.pointsPerBook.get(newBookId));
			bookList.add(bookPoint);
		}
	}

	public String toString() {
		String output = "Library " + id + ":\n";
		output += "Book number: " + bookList.size() + '\n';
		output += "Signup takes: " + signupTime + '\n';
		output += "Ship per day: " + shipmentSpeed + '\n';
		return output;
	}

	public String getBookList() {
		String output = "Showing books:\n";
		for (int i = 0; i < bookList.size(); i++) {
			String addition = String.format("[%d,%d]", bookList.get(i).id, bookList.get(i).value);
			output += addition;
			if (i % Main.NEXT_LINE_VARIABLE == 0 && i > 0) {
				output += '\n';
			}
		}
		output += '\n';
		return output;
	}

	public void sortBookList() {
		Collections.sort(bookList, new Comparator<BookPoint>() {
			@Override
			public int compare(BookPoint book1, BookPoint book2) {
				if (book1.value < book2.value) {
					return 1;
				} else if (book1.value == book2.value) {
					return 0;
				}
				return -1;
			}

		});
	}

	public void addEveryBook(OutputClass outputClass, int currentDay) {
		int daysLeft = (int) (Main.daysForScanning - currentDay - signupTime);
		if (daysLeft > 0) {
			int currentJ = 0;
			for (int i = 0; i < daysLeft; i++) {
				for (int j = currentJ; j < currentJ + shipmentSpeed && j < bookList.size(); j++) {
					BookPoint book = bookList.get(j);
					outputClass.books.add(book.id);
					outputClass.scannedBooks++;
				}
				currentJ += shipmentSpeed;
				if (currentJ > bookList.size()) {
					break;
				}
			}
		}
	}

	public int calculateScore(int currentDay) {

		int daysLeft = (int) (Main.daysForScanning - currentDay - signupTime);
		if (daysLeft > 0) {
			int result = 0;
			int currentJ = 0;
			for (int i = 0; i < daysLeft; i++) {
				for (int j = currentJ; j < currentJ + shipmentSpeed && j < bookList.size(); j++) {
					BookPoint book = bookList.get(j);
					result += book.value;
				}
				currentJ += shipmentSpeed;
				if (currentJ > bookList.size()) {
					break;
				}
			}
			return result;
		}
		return 0;
	}
}
