package pl.straszewski.service;

import pl.straszewski.exceptions.ConflictMeetingException;
import pl.straszewski.exceptions.MeetingBeyond8And18Exception;
import pl.straszewski.exceptions.MeetingDurationException;
import pl.straszewski.model.AvailableTerms;
import pl.straszewski.model.Meeting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class PlannerService {


    LocalTime START_TIME_FOR_MEETING = LocalTime.of(8, 0, 0);
    LocalTime END_TIME_FOR_MEETINGS = LocalTime.of(18, 0, 0);

    List<Meeting> meetingList;
    List<Meeting> existMeetingsAtThisDay = new ArrayList<>();
    List<Meeting> meetingsAtThisDay = new ArrayList<>();
    List<AvailableTerms> availableTermsTerm = new ArrayList<>();


    public PlannerService(List<Meeting> meetingList) {
        this.meetingList = meetingList;
    }

    public boolean checkIfNewMeetingIsBetween8And18(LocalDateTime newMeetingStartDate, LocalDateTime newMeetingEndDate) {

        final LocalDateTime meetingStartDateAt8 = LocalDateTime.of(newMeetingStartDate.toLocalDate(), START_TIME_FOR_MEETING);
        final LocalDateTime meetingEndDateAt18 = LocalDateTime.of(newMeetingStartDate.toLocalDate(), END_TIME_FOR_MEETINGS);

        if (meetingStartDateAt8.isBefore(newMeetingStartDate) && meetingEndDateAt18.isAfter(newMeetingEndDate)
                || meetingStartDateAt8.isEqual(newMeetingStartDate)
                || meetingEndDateAt18.isEqual(newMeetingEndDate)) {
            return true;
        }
        throw new MeetingBeyond8And18Exception("Meeting must be between 8 and 18");
    }

    public List<Meeting> existMeetingsAtThisDay(List<Meeting> meetingList, int room, LocalDateTime newMeetingStartDate) {

        for (Meeting m : meetingList) {
            if (m.getMeetingStartDate().toLocalDate().isEqual(newMeetingStartDate.toLocalDate())
                    && m.getRoom() == room && m.isActual() == true) {
                existMeetingsAtThisDay.add(m);
            }
        }
        existMeetingsAtThisDay.sort(Comparator.comparing(Meeting::getMeetingStartDate));
        existMeetingsAtThisDay = existMeetingsAtThisDay.stream().distinct().collect(Collectors.toList());
        return existMeetingsAtThisDay;
    }
    public List<Meeting> showAllMeetingAtThisDay(List<Meeting> meetingList, LocalDate chosenDate) {

        for (Meeting m : meetingList) {
            if (m.getMeetingStartDate().toLocalDate().isEqual(chosenDate)) {
                meetingsAtThisDay.add(m);
            }
        }
        meetingsAtThisDay.sort(Comparator.comparing(Meeting::getMeetingStartDate));
        meetingsAtThisDay = meetingsAtThisDay.stream().distinct().collect(Collectors.toList());
        return meetingsAtThisDay;
    }

    public boolean checkIfNewMeetingConflictToOtherMeetingsAtThisDay(List<Meeting> meetingList, int room, LocalDateTime newMeetingStartDate, LocalDateTime newMeetingEndDate) {
        List<Meeting> meetings = existMeetingsAtThisDay(meetingList, room,newMeetingStartDate);

        for (Meeting m : meetings) {

            if (newMeetingStartDate.isAfter(m.getMeetingStartDate()) && newMeetingStartDate.isBefore(m.getMeetingEndDate())
                    || newMeetingStartDate.isEqual(m.getMeetingStartDate()) && newMeetingEndDate.isEqual(m.getMeetingEndDate())
                    || newMeetingStartDate.isEqual(m.getMeetingStartDate()) && newMeetingEndDate.isBefore(m.getMeetingEndDate())
                    || newMeetingStartDate.isBefore(m.getMeetingStartDate()) && newMeetingEndDate.isAfter(m.getMeetingEndDate())
            ) {
                List<AvailableTerms> availableTermAtThisDay = findAvailableTermAtThisDay(meetingList, room, newMeetingStartDate, newMeetingEndDate);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("This meeting is in conflict with other meetings," +
                        " please choose other date. Below are show available terms for your chosen day:");
                stringBuilder.append(System.getProperty("line.separator"));
                for (AvailableTerms am : availableTermAtThisDay) {
                    stringBuilder.append(am.getMeetingStartDate().toLocalTime() + " - " +
                            am.getMeetingEndDate().toLocalTime() +",");
                    stringBuilder.append(System.getProperty("line.separator"));
                }
                throw new ConflictMeetingException(stringBuilder.toString());
            }
        }
        return false;
    }

    public int countDurationOfNewMeeting(LocalDateTime newMeetingStartDate, LocalDateTime newMeetingEndDate) {
        int endHour = newMeetingEndDate.toLocalTime().getHour();
        int endMinute = newMeetingEndDate.toLocalTime().getMinute();
        int startHour = newMeetingStartDate.toLocalTime().getHour();
        int startMinute = newMeetingStartDate.toLocalTime().getMinute();

        int meetingDuration = (endHour * 60 + endMinute) - (startHour * 60 + startMinute);
        return meetingDuration;
    }

    public List<AvailableTerms> findAvailableTermAtThisDay(List<Meeting> meetingList, int room, LocalDateTime newMeetingStartDate, LocalDateTime newMeetingEndDate) {

        long meetingDuration = countDurationOfNewMeeting(newMeetingStartDate, newMeetingEndDate);
        List<Meeting> meetings = existMeetingsAtThisDay(meetingList, room,newMeetingStartDate);
        LocalDateTime startPoint = LocalDateTime.of(newMeetingStartDate.toLocalDate(), START_TIME_FOR_MEETING);
        LocalDateTime endPoint = LocalDateTime.of(newMeetingStartDate.toLocalDate(), START_TIME_FOR_MEETING).plusMinutes(meetingDuration);
        LocalDateTime meetingEndDateAt18 = LocalDateTime.of(newMeetingStartDate.toLocalDate(), END_TIME_FOR_MEETINGS);

        for (Meeting m : meetings) {

            if (startPoint.isAfter(m.getMeetingStartDate()) && startPoint.isBefore(m.getMeetingEndDate())
                    || startPoint.isEqual(m.getMeetingStartDate()) && endPoint.isEqual(m.getMeetingEndDate())
                    || startPoint.isBefore(m.getMeetingStartDate()) && endPoint.isAfter(m.getMeetingEndDate())
                    || startPoint.isEqual(m.getMeetingStartDate()) && endPoint.isBefore(m.getMeetingEndDate())
                    || startPoint.isBefore(m.getMeetingStartDate()) && endPoint.isAfter(m.getMeetingStartDate())

            ) {
                startPoint = m.getMeetingEndDate();
                endPoint = m.getMeetingEndDate().plusMinutes(meetingDuration);
            } else {
                availableTermsTerm.add(new AvailableTerms(startPoint, m.getMeetingStartDate()));
                startPoint = m.getMeetingEndDate();
                endPoint = m.getMeetingEndDate().plusMinutes(meetingDuration);
            }
        }
        if (checkIfTermIsAvailableAfterLastMeetingThisDay(startPoint, newMeetingStartDate, newMeetingEndDate))
            availableTermsTerm.add(new AvailableTerms(startPoint, meetingEndDateAt18));

        return availableTermsTerm;
    }

    public boolean checkIfTermIsAvailableAfterLastMeetingThisDay(LocalDateTime startPoint, LocalDateTime newMeetingStartDate, LocalDateTime newMeetingEndDate) {
        int endHour = END_TIME_FOR_MEETINGS.getHour();
        int startPointHour = startPoint.getHour();
        int startPointMinute = startPoint.getMinute();

        long result = endHour * 60 - (startPointHour * 60 + startPointMinute);
        if (result >= countDurationOfNewMeeting(newMeetingStartDate, newMeetingEndDate)) {
            return true;
        }
        return false;
    }

    public boolean checkIfMeetingDurationIsBetween15And120Min (LocalDateTime newMeetingStartDate, LocalDateTime newMeetingEndDate )
    {
        int duration = countDurationOfNewMeeting(newMeetingStartDate, newMeetingEndDate);
        if (duration <15 || duration >120)
        {
            throw new MeetingDurationException("Duration of meeting must be between 15-120 min");
        }
        return true;
    }
}
