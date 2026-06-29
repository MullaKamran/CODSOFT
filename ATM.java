import java.util.Scanner;
import java.util.Locale;

public class ATM {
    private BankAccount account;
    private Scanner scanner;

    private static final int MAX_WITHDRAW_LIMIT = 10000;
    private static final String CORRECT_PIN = "1234"; // hardcoded for demo

    public ATM(BankAccount account) {
        this.account = account;
        this.scanner = new Scanner(System.in);
        this.scanner.useLocale(Locale.US);
    }

    // Entry point
    public void start() {
        System.out.println("=========================================");
        System.out.println("         Welcome to JavaBank ATM         ");
        System.out.println("=========================================");

        if (!authenticateUser()) {
            System.out.println("\n  Too many failed attempts. Card blocked.");
            System.out.println("  Please contact your bank.");
            return;
        }

        showMenu();
        scanner.close();
    }

    // PIN authentication with 3 attempts
    private boolean authenticateUser() {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("\n  Enter PIN: ");
            String pin = scanner.next();
            if (pin.equals(CORRECT_PIN)) {
                System.out.println("\n  Login successful. Welcome, " + account.getAccountHolder() + "!");
                return true;
            } else {
                attempts++;
                int remaining = 3 - attempts;
                if (remaining > 0) {
                    System.out.println("  Incorrect PIN. " + remaining + " attempt(s) remaining.");
                }
            }
        }
        return false;
    }

    // Main menu loop
    private void showMenu() {
        int choice = -1;
        while (choice != 4) {
            System.out.println("\n-----------------------------------------");
            System.out.println("  Account: " + maskAccountNumber(account.getAccountNumber()));
            System.out.println("-----------------------------------------");
            System.out.println("  1. Check Balance");
            System.out.println("  2. Deposit");
            System.out.println("  3. Withdraw");
            System.out.println("  4. Exit");
            System.out.println("-----------------------------------------");
            System.out.print("  Choose an option: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                scanner.next(); // discard invalid input
                System.out.println("  Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> checkBalance();
                case 2 -> depositMenu();
                case 3 -> withdrawMenu();
                case 4 -> System.out.println("\n  Thank you for using JavaBank ATM. Goodbye!");
                default -> System.out.println("  Invalid option. Please choose 1-4.");
            }
        }
    }

    // Check balance
    private void checkBalance() {
        System.out.println("\n  ----- Account Balance -----");
        System.out.println("  Holder : " + account.getAccountHolder());
        System.out.println("  Account: " + maskAccountNumber(account.getAccountNumber()));
        System.out.printf("  Balance: $%.2f%n", account.getBalance());
        System.out.println("  ---------------------------");
    }

    // Deposit menu
    private void depositMenu() {
        System.out.print("\n  Enter amount to deposit: $");
        if (!scanner.hasNextDouble()) {
            scanner.next();
            System.out.println("  Invalid amount entered.");
            return;
        }
        double amount = scanner.nextDouble();
        if (account.deposit(amount)) {
            System.out.printf("  Success! $%.2f deposited.%n", amount);
            System.out.printf("  New Balance: $%.2f%n", account.getBalance());
        }
    }

    // Withdraw menu
    private void withdrawMenu() {
        System.out.print("\n  Enter amount to withdraw: $");
        if (!scanner.hasNextDouble()) {
            scanner.next();
            System.out.println("  Invalid amount entered.");
            return;
        }
        double amount = scanner.nextDouble();
        if (amount > MAX_WITHDRAW_LIMIT) {
            System.out.println("  Withdrawal limit exceeded. Max allowed: $" + MAX_WITHDRAW_LIMIT);
            return;
        }
        if (account.withdraw(amount)) {
            System.out.printf("  Success! $%.2f withdrawn.%n", amount);
            System.out.printf("  Remaining Balance: $%.2f%n", account.getBalance());
        }
    }

    // Mask account number: show only last 4 digits
    private String maskAccountNumber(String accNo) {
        if (accNo.length() <= 4) return accNo;
        return "*".repeat(accNo.length() - 4) + accNo.substring(accNo.length() - 4);
    }
}