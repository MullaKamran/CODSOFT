import java.util.Scanner;

// Class to represent the user's bank account
class BankAccount {
    private double balance;

    // Constructor to initialize balance
    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    // Withdraw method
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        } else {
            return false;
        }
    }

    // Deposit method
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    // Method to get current balance
    public double getBalance() {
        return balance;
    }
}

// Class to represent the ATM machine
class ATM {
    private BankAccount account;
    private Scanner scanner;

    // Constructor
    public ATM(BankAccount account) {
        this.account = account;
        this.scanner = new Scanner(System.in);
    }

    // Start method to show the menu
    public void start() {
        while (true) {
            System.out.println("\n===== ATM MENU =====");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Exit");
            System.out.print("Choose an option (1-4): ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Check balance method
    private void checkBalance() {
        System.out.println("Your current balance is: ₹" + account.getBalance());
    }

    // Deposit method
    private void deposit() {
        System.out.print("Enter amount to deposit: ₹");
        double amount = scanner.nextDouble();
        if (amount <= 0) {
            System.out.println("Invalid deposit amount.");
        } else {
            account.deposit(amount);
            System.out.println("Successfully deposited ₹" + amount);
        }
    }

    // Withdraw method
    private void withdraw() {
        System.out.print("Enter amount to withdraw: ₹");
        double amount = scanner.nextDouble();
        if (account.withdraw(amount)) {
            System.out.println("Successfully withdrew ₹" + amount);
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }
}

// Main class with main method
public class ATMInterface {
    public static void main(String[] args) {
        BankAccount userAccount = new BankAccount(1000.0); // starting with ₹1000
        ATM atm = new ATM(userAccount);
        atm.start();
    }
}
