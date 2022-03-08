package utils;

import eu.dozd.mongo.annotation.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embedded
@Builder
public class Time {
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hours;
    private Integer minutes;
    private Integer second;
    private Integer week;
    private Integer current_day;
    private Integer max_day;
    private Integer max_day_of_week;
    private Integer min_day;
    private Integer min_day_of_week;
}
