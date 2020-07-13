package pl.straszewski.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@ToString
public class AvailableTerms {

    private LocalDateTime meetingStartDate;
    private LocalDateTime meetingEndDate;

    public String getFormattedMeetingStartDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return meetingStartDate.format(dateTimeFormatter);
    }

    public String getFormattedMeetingEndDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return meetingEndDate.format(dateTimeFormatter);
    }
}
