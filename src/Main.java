import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {


       //create travel manager object
        TravelManager travelManager = new TravelManager();


        // Localization
        TravelManager.printWelcomeMessage(Locale.ITALIAN); //Show ITALIAN in DEMO will display welcome message in Italian


        // Create travels
        Travel travel1 = new Travel(1, "Business Conference in Japan", "Attend a tech conference in Tokyo and network with industry leaders",
                new BusinessTravel("Tech Conference in Japan"), LocalDateTime.now().plusDays(5), TravelStatus.PENDING);

        Travel travel2 = new Travel(2, "Backpacking Through Thailand", "Solo adventure to discover cultural heritage and explore local cuisine in Bangkok and Chiang Mai",
                new SoloTravel("Solo Backpacking in Thailand"), LocalDateTime.now().plusDays(10), TravelStatus.IN_PROGRESS);

        Travel travel3 = new Travel(3, "European Cultural Tour", "Explore historic landmarks, museums, and cultural experiences in Paris, Rome, and Berlin",
                new GroupTravel("European Cultural Exploration"), LocalDateTime.now().plusDays(15), TravelStatus.COMPLETED);

        Travel travel4 = new Travel(4, "Business Trip to USA", "Attend meetings, seminars, and workshops in New York City to discuss corporate strategies",
                new BusinessTravel("Corporate Meetings in NYC"), LocalDateTime.now().plusDays(20), TravelStatus.PENDING);

        Travel travel5 = new Travel(5, "Beach Vacation in Maldives", "Relax on pristine beaches, enjoy water sports, and experience luxury resorts in Maldives",
                new GroupTravel("Maldives Resort Relaxation"), LocalDateTime.now().plusDays(25), TravelStatus.IN_PROGRESS);

        Travel travel6 = new Travel(6, "Corporate Safari in Kenya", "Network with business professionals while experiencing a safari and wildlife conservation projects in Maasai Mara",
                new GroupTravel("Corporate Safari in Kenya"), LocalDateTime.now().plusDays(30), TravelStatus.CANCELLED);

        Travel travel7 = new Travel(7, "Solo Adventure in New Zealand", "Explore New Zealand’s stunning landscapes, including hikes, lakes, and mountains",
                new SoloTravel("Solo New Zealand Hiking"), LocalDateTime.now().plusDays(35), TravelStatus.IN_PROGRESS);

        Travel travel8 = new Travel(8, "Group Cultural Trip to India", "Explore India’s rich history, culture, and architectural wonders, including the Taj Mahal, with a group",
                new GroupTravel("Group Tour in India"), LocalDateTime.now().plusDays(40), TravelStatus.COMPLETED);

        Travel travel9 = new Travel(9, "Group Hiking Expedition in Canada", "Embark on a group hiking trip through Banff National Park, with breathtaking views of lakes and mountains",
                new GroupTravel("Group Hiking in Canada"), LocalDateTime.now().plusDays(45), TravelStatus.PENDING);

        Travel travel10 = new Travel(10, "Solo Culinary Tour in Mexico", "Savor authentic Mexican cuisine, visit local markets, and enjoy street food in Mexico",
                new SoloTravel("Solo Mexican Culinary Tour"), LocalDateTime.now().plusDays(50), TravelStatus.IN_PROGRESS);
        // Add travels
        travelManager.addTravel(travel1);
        travelManager.addTravel(travel2);
        travelManager.addTravel(travel3);
        travelManager.addTravel(travel4);
        travelManager.addTravel(travel5);
        travelManager.addTravel(travel6);
        travelManager.addTravel(travel7);
        travelManager.addTravel(travel8);
        travelManager.addTravel(travel9);
        travelManager.addTravel(travel10);

        // Perform Stream Operations
        travelManager.streamOperations();
        travelManager.collectOperations();

        // Process Travel Concurrency
        travelManager.processTravels(List.of(travel1, travel2, travel3, travel4, travel5, travel6, travel7, travel8, travel9, travel10));

        // Save and Load Travels from File
        try {
            travelManager.saveTravels(List.of(travel1, travel2, travel3, travel4, travel5, travel6, travel7, travel8, travel9, travel10));
            List<String> loadedTravels = travelManager.loadTravels();
            System.out.println("Loaded Travels: " + loadedTravels);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Show Travels sorted by due Date
        List<Travel> sortedTravels = travelManager.getSortedTravel();
        System.out.println("Travels sorted by due date:");
        sortedTravels.forEach(travel ->
                System.out.println(travel.id() + ": " + travel.title() + " - Due: " + travel.dueDate()));


        // Show Travels with PENDING status
        List<Travel> pendingTravels = travelManager.filterTravel(t -> t.status() == TravelStatus.PENDING);
        System.out.println("Pending Travels: " + pendingTravels);


        // Show defafult travel group if no matching
        Supplier<Travel> defaultTravelSupplier = () -> new Travel(999, "Default Travel Destination", "If all else fails",
                new GroupTravel("Philippines"), LocalDateTime.now().plusDays(7), TravelStatus.PENDING);

        Travel defaultTravel = travelManager.getDefaultTravel(defaultTravelSupplier);
        System.out.println("Default Travel: " + defaultTravel);


        // **Consumer Example** - Print all travels
        Consumer<Travel> travelConsumer = travel -> System.out.println("Travel ID: " + travel.id() + ", Title: " + travel.title());
        System.out.println("\nPrinting all travels using Consumer:");
        travelManager.getTravels().forEach(travelConsumer);

        // **Function Example** - Extract titles from all travels and print them
        Function<Travel, String> travelTitleExtractor = travel -> travel.title();
        System.out.println("\nMapping travels to their titles using Function:");
        List<String> travelTitles = travelManager.getTravels().stream()
                .map(travelTitleExtractor)
                .collect(Collectors.toList());
        System.out.println("Travel Titles: " + travelTitles);

    }
}
