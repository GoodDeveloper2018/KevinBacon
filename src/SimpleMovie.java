import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SimpleMovie {
    private String title;
    private String actorsData;
    private ArrayList<String> actors;

    public SimpleMovie(String t, String a) {
        title = t;
        actorsData = a;
        actors = new ArrayList<String>();
        String[] tempActors = actorsData.split(":");
        for (int i = 0; i < tempActors.length; i++) {
            actors.add(tempActors[i]);
        }
    }

    class Node {
        String data;
        Node left, right;
        Node(String d) {
            data = d;
            left = null;
            right = null;
        }
    }

    public void findShortestPath() {
        List<Node> Tree = new LinkedList<Node>();
        for (int i = 0; i < actors.size(); i++) {
            Tree.add(actors.get(i));
        }
    }

    public String toString() {
        return "Title: " + title + "\n" + "Actors: " + actors + "\n";
        }
    }