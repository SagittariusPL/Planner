package pl.straszewski.model;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Entity
@NoArgsConstructor
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private LocalDateTime meetingStartDate;
    private LocalDateTime meetingEndDate;
    private boolean isActual;
    private int room;
    private String description;

    public Meeting(LocalDateTime meetingStartDate, LocalDateTime meetingEndDate, String title, int room, String description) {
        this.meetingStartDate = meetingStartDate;
        this.meetingEndDate = meetingEndDate;
        this.title = title;
        this.isActual =true;
        this.room=room;
        this.description =description;
    }



    public String getFormattedMeetingStartDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return meetingStartDate.format(dateTimeFormatter);
    }

    public String getFormattedMeettingEndDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return meetingEndDate.format(dateTimeFormatter);
    }

    public String getFormattedStartTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return meetingStartDate.format(dateTimeFormatter);
    }

    public String getFormattedEndTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return meetingEndDate.format(dateTimeFormatter);
    }

    public LocalDateTime getMeetingStartDate() {
        return meetingStartDate;
    }

    public LocalDateTime getMeetingEndDate() {
        return meetingEndDate;
    }

    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActual() {
        return isActual;
    }

    public int getRoom() {
        return room;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMeetingStartDate(LocalDateTime meetingStartDate) {
        this.meetingStartDate = meetingStartDate;
    }

    public void setMeetingEndDate(LocalDateTime meetingEndDate) {
        this.meetingEndDate = meetingEndDate;
    }

    public void setActual(boolean actual) {
        isActual = actual;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", meettingStartDate=" + getFormattedMeetingStartDate() +
                ", meettingEndDate=" + getFormattedMeettingEndDate() +
                ", isActual=" + isActual +
                ", room=" + room +
                '}';
    }
}
