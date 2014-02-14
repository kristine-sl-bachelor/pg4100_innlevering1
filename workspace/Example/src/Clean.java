import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Clean {
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("random.dat");
		Scanner scan = new Scanner(file);
		String print = "";
		long shit = 100000000000000000l; 

		scan.next();
		while (scan.hasNext()) {
			String input = scan.next(); 
			Double number;
			if(input.contains("D-")) {
				System.out.println(input);
				String[] temp = input.split("D-"); 
				double factor = Double.parseDouble(temp[1]); 
				number = Math.pow(Double.parseDouble(temp[0]), (-factor)); 
				System.out.println(number + "\n");
			} else {
				number = Double.parseDouble(input); 
			}
			
			System.out.println(number * shit);
		}
	}
}
