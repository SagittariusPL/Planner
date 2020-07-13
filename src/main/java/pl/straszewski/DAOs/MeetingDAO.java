package pl.straszewski.DAOs;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MeetingDAO {

    private long id;
    private String title;
    private String meetingStartDate;
    private String meetingEndDate;
    private String isActual;
    private int room;
    private String description;
}
