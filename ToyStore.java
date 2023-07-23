import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Toy {
    private int id;
    private String name;
    private int quantity;
    private double weight;

    public Toy(int id, String name, int quantity, double weight) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

public class ToyStore {
    private List<Toy> toys = new ArrayList<>();

    public void addToy(Toy toy) {
        toys.add(toy);
    }

    public void updateToyWeight(int toyId, double newWeight) {
        for (Toy toy : toys) {
            if (toy.getId() == toyId) {
                toy.setWeight(newWeight);
                break;
            }
        }
    }

    public Toy getRandomToy() {
        double totalWeight = toys.stream().mapToDouble(Toy::getWeight).sum();
        double randomWeight = Math.random() * totalWeight;

        for (Toy toy : toys) {
            randomWeight -= toy.getWeight();
            if (randomWeight <= 0) {
                if (toy.getQuantity() > 0) {
                    toy.setQuantity(toy.getQuantity() - 1);
                    return toy;
                } else {
                    // If the selected toy is out of stock, try again
                    return getRandomToy();
                }
            }
        }

        return null; // Should not reach this point, but just in case
    }

    public void saveResultToFile(Toy toy) {
        try {
            FileWriter fileWriter = new FileWriter("winners.txt", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("ID: " + toy.getId() + ", Name: " + toy.getName());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ToyStore toyStore = new ToyStore();

        Toy toy1 = new Toy(1, "Toy1", 5, 20);
        Toy toy2 = new Toy(2, "Toy2", 3, 30);
        Toy toy3 = new Toy(3, "Toy3", 7, 50);

        toyStore.addToy(toy1);
        toyStore.addToy(toy2);
        toyStore.addToy(toy3);

        // Update the weight of a toy (for example)
        toyStore.updateToyWeight(1, 15);

        // Perform the draw
        Toy winner = toyStore.getRandomToy();
        if (winner != null) {
            System.out.println("Congratulations! You won " + winner.getName());
            toyStore.saveResultToFile(winner);
        } else {
            System.out.println("Sorry, no toys available for the draw.");
        }
    }
}
