import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.InputMismatchException;

public class QuizApplication {
    private static final int NUM_QUESTIONS = 5; // number of questions
    private static final int TIME_PER_QUESTION = 30;  // time per question in seconds

    private static String[] questions = {
            "What is the capital of France?",
            "What is the largest planet in our solar system?",
            "What is the smallest country in the world?",
            "What is the most popular programming language?",
            "What is the largest living species of lizard?"
    };

    private static String[][] options = {
            {"Paris", "London", "Berlin", "Rome"},
            {"Earth", "Saturn", "Jupiter", "Uranus"},
            {"Vatican City", "Monaco", "Nauru", "Tuvalu"},
            {"Java", "Python", "C++", "JavaScript"},
            {"Komodo dragon", "Saltwater crocodile", "Black mamba", "Green anaconda"}
    };

    private static String[] correctAnswers = {
            "Paris",
            "Jupiter",
            "Vatican City",
            "Java",
            "Komodo dragon"
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int score = 0;
        boolean[] correctAnswersArray = new boolean[NUM_QUESTIONS];

        for (int i = 0; i < NUM_QUESTIONS; i++) {
            System.out.println("Question " + (i + 1) + ": " + questions[i]);
            System.out.println("Options:");
            for (int j = 0; j < options[i].length; j++) {
                System.out.println((j + 1) + ". " + options[i][j]);
            }

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Time's up!");
                    timer.cancel();
                }
            }, TIME_PER_QUESTION * 1000);

            System.out.print("Enter your answer (1-" + options[i].length + "): ");
            int answer = 0;
            while (true) {
                try {
                    answer = scanner.nextInt();
                    if (answer < 1 || answer > options[i].length) {
                        System.out.println("Invalid input. Please enter a number between 1 and " + options[i].length);
                        continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.next(); // clear the invalid input
                }
            }

            if (options[i][answer - 1].equals(correctAnswers[i])) {
                score++;
                correctAnswersArray[i] = true;
                System.out.println("Correct!");
            } else {
                correctAnswersArray[i] = false;
                System.out.println("Incorrect. The correct answer is " + correctAnswers[i]);
            }

            timer.cancel();
        }

        System.out.println("Final Score: " + score + "/" + NUM_QUESTIONS);
        System.out.println("Summary:");
        for (int i = 0; i < NUM_QUESTIONS; i++) {
            System.out.println("Question " + (i + 1) + ": " + (correctAnswersArray[i]? "Correct" : "Incorrect"));
        }
    }
}