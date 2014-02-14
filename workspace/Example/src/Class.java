import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Class {
	public static void main(String[] args) {
		File fileDat = new File("random.dat");
		
		try(Scanner scanDat = new Scanner(fileDat)){
			
			int amount = scanDat.nextInt(); 
			Double sum = 0.0; 
			while(scanDat.hasNext()) {
				
				String input = scanDat.next(); 
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
				
				sum += number; 
			}
			
			System.out.println(sum);
			double average = sum / amount; 
			System.out.println(average);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}