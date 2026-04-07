package com.demo.user.dto;

import java.util.List;

public record PagedResponse<T>(List<T> content, int currentPage, long totalElements, int totalPages) {

}
