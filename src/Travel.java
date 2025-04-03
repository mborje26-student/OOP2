
import java.time.LocalDateTime;
public record Travel(int id, String title, String description, TravelCategory category, LocalDateTime dueDate, TravelStatus status) {
}
