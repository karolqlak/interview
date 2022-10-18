package org.ing.interview.integration.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PagedResponse {

    private long totalPages;
    private List<RealEstateApiData> data;

    @Getter
    @Setter
    public static class RealEstateApiData {
        private UUID uuid;
        private String type;
        private String price;
        private String description;
        private String area;
        private int rooms;
    }
}

