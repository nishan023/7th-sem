package labQuestions.coreJava;

public class CustomExceptionDemo{
    public static void main(String[] args) {
        System.out.println("****Nishan Dhakal****");
        BankAccount yourAcc = new BankAccount(5000);

        try{
            yourAcc.withdraw(15000);
        }catch(InsufficientBalanceException e){
            System.out.println("Exception encountered: " + e.getMessage());
        }
    }
}
