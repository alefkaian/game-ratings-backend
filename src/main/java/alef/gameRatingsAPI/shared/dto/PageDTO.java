package alef.gameRatingsAPI.shared.dto;

import java.util.List;

public record PageDTO<T>(
    int currentPage,
    int pageSize,
    long totalElements,
    int totalPages,
    boolean isLast,
    List<T> content
) {}
