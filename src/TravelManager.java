import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TravelManager {
    private List<Travel> travels = new ArrayList<>();
    private static final Path FILE_PATH = Paths.get("travels.txt");
    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Add Travel plan
    public void addTravel(Travel travel) {
        travels.add(travel);
    }

    // Use of Supplier to create default travel plan
    public Travel getDefaultTravel(Supplier<Travel> supplier) {
        return supplier.get();
    }

    // Filter travels using Predicate (Lambda)
    public List<Travel> filterTravel(Predicate<Travel> condition) {
        return travels.stream().filter(condition).collect(Collectors.toList());
    }

    // Sort travels using Comparator.comparing()
    public List<Travel> getSortedTravel() {
        return travels.stream()
                .sorted(Comparator.comparing(Travel::dueDate))
                .collect(Collectors.toList());
    }

    // Streams Terminal Operations
    public void streamOperations() {
        System.out.println("\nMin by ID: " + travels.stream().min(Comparator.comparing(Travel::id)).orElse(null));
        System.out.println("Max by ID: " + travels.stream().max(Comparator.comparing(Travel::id)).orElse(null));
        System.out.println("Total count of travels: " + travels.stream().count());
        System.out.println("Find any: " + travels.stream().findAny().orElse(null));
        System.out.println("Find first: " + travels.stream().findFirst().orElse(null));
        System.out.println("All match PENDING: " + travels.stream().allMatch(t -> t.status() == TravelStatus.PENDING));
        System.out.println("Any match COMPLETED: " + travels.stream().anyMatch(t -> t.status() == TravelStatus.COMPLETED));
        System.out.println("None match CANCELLED: " + travels.stream().noneMatch(t -> t.status() == TravelStatus.CANCELLED));
    }

    // Collectors operations
    public void collectOperations() {
        Map<TravelStatus, List<Travel>> groupedByStatus = travels.stream()
                .collect(Collectors.groupingBy(Travel::status));

        Map<Integer, String> idToTitleMap = travels.stream()
                .collect(Collectors.toMap(Travel::id, Travel::title));

        System.out.println("\nGrouped by Status:");
        groupedByStatus.forEach((status, travelList) -> {
            System.out.println(status + ":");
            travelList.forEach(travel -> System.out.println("  " + travel.id() + ": " + travel.title()));
        });

        System.out.println("\nID to Title:");
        idToTitleMap.forEach((id, title) ->
                System.out.println("ID: " + id + ", Title: " + title)
        );
    }

    //concurrency to travel processor
    public void processTravels(List<Travel> travels) {

        Consumer<Travel> travelConsumer = travel -> {
            System.out.println("Processing Travel: " + travel.title());
        };

        List<Callable<Void>> callables = travels.stream()
                .map(travel -> (Callable<Void>) () -> {
                    travelConsumer.accept(travel);
                    return null;
                })
                .toList();

        try {
            List<Future<Void>> results = executor.invokeAll(callables);
            for (Future<Void> result : results) {
                result.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    // Write travels to file
    public static void saveTravels(List<Travel> travels) throws IOException {
        List<String> lines = travels.stream()
                .map(travel -> travel.id() + ","
                        + travel.title() + ","
                        + travel.description() + ","
                        + travel.category() + ","
                        + travel.dueDate().format(DATE_TIME_FORMATTER) + ","
                        + travel.status().toString())
                .collect(Collectors.toList());
        Files.write(FILE_PATH, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }


    // Read travels from file
    public static List<String> loadTravels() throws IOException {
        return Files.readAllLines(FILE_PATH);
    }


    //travel localization
    public static void printWelcomeMessage(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        System.out.println(bundle.getString("travel.welcome"));
    }

    public static void printExitMessage(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        System.out.println(bundle.getString("travel.end"));
    }

    public List<Travel> getTravels() {
        return travels;
    }

}
