public class Main {
    public static void main(String[] args) {
        // Create a bank account with holder name, account number, initial balance
        BankAccount account = new BankAccount("Kamran", "9876543210", 5000.00);

        // Connect account to ATM and start
        ATM atm = new ATM(account);
        atm.start();
    }
}