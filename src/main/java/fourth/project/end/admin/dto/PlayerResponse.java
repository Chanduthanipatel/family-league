package fourth.project.end.admin.dto;

public record PlayerResponse(
        Long id,
        String code,
        String fullName,
        String shortName,
        boolean active) {
}
