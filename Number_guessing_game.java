import java.util.Random;
import java.util.Scanner;
public class Number_guessing_game {
    public static void main(String[] args) {
        Random num = new Random();
        int guessNumber = num.nextInt(100)+1;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Guess a number between 1 to 100:");
        int guess = scanner.nextInt();
        while(guess != guessNumber){
            if(guess < guessNumber) {
                System.out.println("That's too low,try again:");
            }else {
                System.out.println("That's too high,try again:");
            }
            guess = scanner.nextInt();
        }
        System.out.println("Yes,complete!!");
    }
}
