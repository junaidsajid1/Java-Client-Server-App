import java.util.ArrayList;
import java.util.Scanner;

class RewardConfig {
    String customerId;
    double minimumPurchase;
    int pointRate;
    double baisaPerPoint;

    public RewardConfig(String customerId, double minimumPurchase, int pointRate, double baisaPerPoint) {
        this.customerId = customerId;
        this.minimumPurchase = minimumPurchase;
        this.pointRate = pointRate;
        this.baisaPerPoint = baisaPerPoint;
    }

    @Override
    public String toString() {
        return "Customer ID: " + customerId +
               ", Min Purchase: " + minimumPurchase +
               ", Point Rate: " + pointRate +
               ", Baisa per Point: " + baisaPerPoint;
    }
}

public class RewardManager {
    static ArrayList<RewardConfig> rewardsList = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Reward Config Menu ===");
            System.out.println("1. Add Reward Config");
            System.out.println("2. Remove Reward Config by Customer ID");
            System.out.println("3. Display all Reward Configs");
            System.out.println("4. Display Reward Config by Customer ID");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    addRewardConfig(scanner);
                    break;
                case 2:
                    removeRewardConfig(scanner);
                    break;
                case 3:
                    displayAllRewards();
                    break;
                case 4:
                    displayRewardById(scanner);
                    break;
                case 5:
                    running = false;
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }

        scanner.close();
    }

    private static void addRewardConfig(Scanner scanner) {
        System.out.print("Enter Customer ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter Minimum Purchase: ");
        double minPurchase = scanner.nextDouble();

        System.out.print("Enter Point Rate: ");
        int pointRate = scanner.nextInt();

        System.out.print("Enter Baisa per Point: ");
        double baisa = scanner.nextDouble();
        scanner.nextLine();  // Consume newline

        rewardsList.add(new RewardConfig(id, minPurchase, pointRate, baisa));
        System.out.println("Reward config added successfully.");
    }

    private static void removeRewardConfig(Scanner scanner) {
        System.out.print("Enter Customer ID to remove: ");
        String id = scanner.nextLine();

        boolean removed = rewardsList.removeIf(r -> r.customerId.equalsIgnoreCase(id));
        if (removed) {
            System.out.println("Reward config for Customer ID " + id + " removed.");
        } else {
            System.out.println("Customer ID not found.");
        }
    }

    private static void displayAllRewards() {
        if (rewardsList.isEmpty()) {
            System.out.println("No reward configurations found.");
            return;
        }
        System.out.println("\n--- All Reward Configurations ---");
        for (RewardConfig r : rewardsList) {
            System.out.println(r);
        }
    }

    private static void displayRewardById(Scanner scanner) {
        System.out.print("Enter Customer ID to display: ");
        String id = scanner.nextLine();

        for (RewardConfig r : rewardsList) {
            if (r.customerId.equalsIgnoreCase(id)) {
                System.out.println(r);
                return;
            }
        }
        System.out.println("Customer ID not found.");
    }
}
