package fourth.project.end.admin.dto;

public record TeamResponse(
    Long id,
    String code,
    String name,
    String shortName,
    String logoUrl
) {
}
