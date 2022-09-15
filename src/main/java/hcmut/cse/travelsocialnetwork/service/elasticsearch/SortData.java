package hcmut.cse.travelsocialnetwork.service.elasticsearch;

import co.elastic.clients.elasticsearch._types.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : hung.nguyen23
 * @since : 9/15/22 Thursday
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SortData {
    private String field;

    private String order;

    private Double lat;

    private Double lon;

    public SortOptions sortBuilder() {
        if (field == null || order == null) {
            return null;
        }

        switch (field) {
            case "location":
                if (lat == null || lon == null) {
                    return null;
                }
                return SortOptionsBuilders.geoDistance(distance -> distance
                        .field("location")
                        .location(location -> location.latlon(coordinates -> coordinates.lat(lat).lon(lon)))
                        .distanceType(GeoDistanceType.Plane)
                        .order(mappingSortOrder(order))
                        .unit(DistanceUnit.Meters)
                        .mode(SortMode.Min));
            case "last_update":
                return SortOptionsBuilders
                        .field(fieldSort -> fieldSort.field(field).order(mappingSortOrder(order)));
        }

        return null;
    }

    private SortOrder mappingSortOrder(String sortOrder) {
        switch (sortOrder) {
            case "ASC":
                return SortOrder.Asc;
            case "DESC":
                return SortOrder.Desc;
            default:
                return null;
        }
    }
}
