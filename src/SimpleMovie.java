import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * A combined class that can:
 *   (1) represent a single movie (title + actors),
 *   (2) also build a bipartite graph of "movie titles" and "actor names" from a file,
 *   (3) run a BFS-like search to find the 'movie distance' from any actor to Kevin Bacon.
 */
public class SimpleMovie
{
    // Fields for representing a single movie
    private String title;
    private String actorsData;
    private ArrayList<String> actors;

    /**
     * Constructor for making a single SimpleMovie object (title + actor string).
     * Example: new SimpleMovie("Braveheart", "Mel Gibson:Sophie Marceau:...")
     */
    public SimpleMovie(String t, String a) {
        title = t;
        actorsData = a;
        actors = new ArrayList<>();
        String[] tempActors = actorsData.split(":");
        for (String raw : tempActors) {
            actors.add(raw);
        }
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\n" + "Actors: " + actors + "\n";
    }

    // Fields for the Kevin Bacon "bipartite" graph
    private Map<String, List<String>> adjacency;

    /**
     * Default constructor. If you use this, you'll have an empty adjacency
     * until you call a method to load data from file.
     */
    public SimpleMovie() {
        adjacency = new HashMap<>();
    }

    /**
     * Constructor that automatically builds the bipartite graph from a file.
     * You do NOT need to call any extra method to populate the graph.
     * Example usage: new SimpleMovie("src/movie_data")
     */
    public SimpleMovie(String fileName) {
        adjacency = new HashMap<>();
        loadDataAndBuildGraph(fileName);
    }

    /**
     * Internal helper for reading the file and building adjacency.
     */
    private void loadDataAndBuildGraph(String fileName) {
        try (Scanner scan = new Scanner(new File(fileName))) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] parts = line.split("---");
                if (parts.length < 2) continue;
                String movieTitle = parts[0];
                String actorData  = parts[1];
                adjacency.putIfAbsent(movieTitle, new ArrayList<>());
                String[] actorNames = actorData.split(":");
                for (String rawActor : actorNames) {
                    adjacency.putIfAbsent(rawActor, new ArrayList<>());
                    adjacency.get(movieTitle).add(rawActor);
                    adjacency.get(rawActor).add(movieTitle);
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        }
    }

    /**
     * Finds the chain from startActor to "Kevin Bacon," then prints:
     * (A) The path (Actor -> Movie -> Actor -> Movie -> ... -> Kevin Bacon)
     * (B) The "movie distance" = (# of edges in path)/2
     * Returns the movie distance, or -1 if no path.
     */
    public long findMovieDistance(String startActor) {
        String target = "Kevin Bacon";
        if (!adjacency.containsKey(startActor) || !adjacency.containsKey(target)) {
            System.out.println("Either \"" + startActor + "\" or \"Kevin Bacon\" is not in the data.");
            return -1;
        }
        Queue<String> queue = new LinkedList<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();
        queue.add(startActor);
        visited.add(startActor);
        parent.put(startActor, null);
        boolean found = false;
        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(target)) {
                found = true;
                break;
            }
            for (String neighbor : adjacency.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        if (!found) {
            System.out.println("No connection found from \"" + startActor + "\" to Kevin Bacon.");
            return -1;
        }
        List<String> chain = new ArrayList<>();
        String node = target;
        while (node != null) {
            chain.add(node);
            node = parent.get(node);
        }
        Collections.reverse(chain);
        System.out.println("\nPath from " + startActor + " to Kevin Bacon:");
        for (int i = 0; i < chain.size(); i++) {
            if (i > 0) System.out.print(" -> ");
            System.out.print(chain.get(i));
        }
        System.out.println();
        int edges = chain.size() - 1;
        long distance = edges / 2;
        System.out.println("Movie Distance = " + distance);
        return distance;
    }
}
