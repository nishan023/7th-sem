package labQuestions.rmiAndCorba;

import java.rmi.Naming;
import java.util.Scanner;

public class CalculatorClient {
    public static void main(String[] args) {
        System.out.println("****Nishan Dhakal****");
        try {
            Calculator calc = (Calculator) Naming.lookup("rmi://localhost:1099/CalculatorService");

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter first number: ");
            double num1 = sc.nextDouble();
            
            System.out.print("Enter second number: ");
            double num2 = sc.nextDouble();
            
            System.out.println("\n--- Remote Calculation Results ---");
            System.out.println(num1 + " + " + num2 + " = " + calc.add(num1, num2));
            System.out.println(num1 + " - " + num2 + " = " + calc.subtract(num1, num2));
            System.out.println(num1 + " * " + num2 + " = " + calc.multiply(num1, num2));
            
            if (num2 != 0) {
                System.out.println(num1 + " / " + num2 + " = " + calc.divide(num1, num2));
            } else {
                System.out.println("Cannot divide by zero");
            }
            
            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
