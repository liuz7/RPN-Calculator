import com.test.impl.RPNCalculator;
import java.util.Scanner;

public class App {

  public static void main(String[] args) {
    RPNCalculator rpnCalculator = new RPNCalculator();
    Scanner scanner = new Scanner(System.in);
    System.out.println("Please input RPN calculation string below:");
    while (true) {
      String input = scanner.nextLine();
      if (input.equalsIgnoreCase("quit")) {
        System.out.println("Exit.");
        break;
      }
      String result;
      try {
        result = rpnCalculator.calculate(input);
      } catch (Exception e) {
        result = e.getMessage();
      }
      System.out.println("Result : " + result);
    }
  }
}
