package hcmut.cse.travelsocialnetwork.model;

/**
 * @author : hung.nguyen23
 * @since : 12/6/22 Tuesday
 **/

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * A page information
 */
public class Paginated<T> implements Serializable {
    @JsonProperty("items")
    private List<T> items;

    @JsonProperty("pageNumber")
    private long pageNumber;

    @JsonProperty("pageSize")
    private long pageSize;

    @JsonProperty("totalItems")
    private long totalItems;

    @JsonProperty("totalPages")
    private long totalPages;

    @JsonProperty("hasNext")
    private boolean hasNext = false;

    @JsonProperty("nextPage")
    private long nextPage = 1;

    @JsonProperty("hasPrevious")
    private boolean hasPrevious = false;

    @JsonProperty("previousPage")
    private long previousPage = 1;

    Paginated() {}

    public Paginated(List<T> items, long pageNumber, long pageSize, long total) {
        this.items = items;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalItems = total;
        this.totalPages = (int) Math.ceil((double) total / this.pageSize);

        this.hasNext = this.pageNumber < this.totalPages;
        this.hasPrevious = this.pageNumber > 1;

        if (this.hasNext) {
            this.nextPage = this.pageNumber + 1;
        }

        if (this.hasPrevious) {
            this.previousPage = this.pageNumber - 1;
        }
    }
}
