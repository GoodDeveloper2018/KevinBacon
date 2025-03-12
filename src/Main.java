import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Instantiates a SimpleMovie that automatically loads the entire data set
        SimpleMovie bigGraph = new SimpleMovie("src/movie_data");

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Enter an actor's name (or 'quit' to exit): ");
            String name = sc.nextLine();
            if (name.equalsIgnoreCase("quit")) {
                break;
            }
            bigGraph.findMovieDistance(name);
        }
        sc.close();
    }
}
