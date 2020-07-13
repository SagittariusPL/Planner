package pl.straszewski.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.straszewski.DAOs.MeetingDAO;
import pl.straszewski.exceptions.MeetingIsActualException;
import pl.straszewski.exceptions.MeetingIsNotActualException;
import pl.straszewski.exceptions.MeetingNotFoundException;
import pl.straszewski.model.Meeting;
import pl.straszewski.repository.MeetingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntToLongFunction;

@Service
@Transactional
public class MeetingService {

    final MeetingRepository meetingRepository;

    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;

    }

    public void addMeeting(LocalDateTime startNewMeetingDate, LocalDateTime endNewMeetingDate, String title, int roomNumber, String description) {

        List<Meeting> meetingList = meetingRepository.findAll();
        PlannerService plannerService = new PlannerService(meetingList);
        if (meetingList.isEmpty()
                || plannerService.checkIfNewMeetingIsBetween8And18(startNewMeetingDate, endNewMeetingDate) &&
                plannerService.checkIfMeetingDurationIsBetween15And120Min(startNewMeetingDate, endNewMeetingDate) &&
                !plannerService.checkIfNewMeetingConflictToOtherMeetingsAtThisDay(meetingList, roomNumber, startNewMeetingDate, endNewMeetingDate)
        ) {
            meetingRepository.save(new Meeting(startNewMeetingDate, endNewMeetingDate, title, roomNumber, description));
        }

    }

    public List<MeetingDAO> getAllMeetings() {
        List<Meeting> meetingList = meetingRepository.findAll();
        if (meetingList.isEmpty()) {
            throw new MeetingNotFoundException("No planned meetings");
        }
        List<MeetingDAO> meetingListDAO = new ArrayList<>();
        for (Meeting m : meetingList) {
            meetingListDAO.add(new MeetingDAO(m.getId(), m.getTitle(), m.getFormattedMeetingStartDate(),
                    m.getFormattedMeettingEndDate(), String.valueOf(m.isActual()), m.getRoom(), m.getDescription()));
        }
        return meetingListDAO;
    }

    public List<MeetingDAO> getAllMeetingsAtThisDay(LocalDate chosenDate) {

        List<Meeting> meetingListFromDB = meetingRepository.findAll();
        PlannerService plannerService = new PlannerService(meetingListFromDB);
        List<Meeting> meetingsAtThisDay = plannerService.showAllMeetingAtThisDay(meetingListFromDB, chosenDate);
        if (meetingsAtThisDay.isEmpty()) {
            throw new MeetingNotFoundException("No planned meetings at this day");
        }

        List<MeetingDAO> meetingListDAO = new ArrayList<>();
        for (Meeting m : meetingsAtThisDay) {
            meetingListDAO.add(new MeetingDAO(m.getId(), m.getTitle(), m.getFormattedStartTime(),
                    m.getFormattedEndTime(), String.valueOf(m.isActual()), m.getRoom(), m.getDescription()));
        }
        return meetingListDAO;
    }

    public void deleteMeeting(long meetingId) {
        Optional<Meeting> meeting = meetingRepository.findById(meetingId);
        if (meeting.isPresent()) {
            if (meeting.get().isActual() == false) {
                meetingRepository.deleteById(meetingId);

            } else {
                throw new MeetingIsActualException("This meeting is actual, can not be remove");
            }
        } else {
            throw new MeetingNotFoundException("This meeting not exist");
        }
    }

    public void deleteAllUnActualMeetings() {
        List<Meeting> meeting = meetingRepository.findAll();
        if (!meeting.isEmpty()) {
            for (Meeting m : meeting) {
                if (m.isActual() == false) {
                    meetingRepository.delete(m);
                }
            }
        }
    }

    public void cancelMeeting(long meetingId) {
        Optional<Meeting> meeting = meetingRepository.findById(meetingId);


        if (meeting.isPresent()) {

            if (meeting.get().isActual() == false) {
                throw new MeetingIsNotActualException("Can not twice cancel meeting");
            } else {
                meeting.get().setActual(false);
                meetingRepository.save(meeting.get());
            }
        } else {
            throw new MeetingNotFoundException("This meeting not exist");
        }
    }


}
