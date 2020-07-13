package pl.straszewski.service

import pl.straszewski.exceptions.ConflictMeetingException
import pl.straszewski.exceptions.MeetingBeyond8And18Exception
import pl.straszewski.model.AvailableTerms
import pl.straszewski.model.Meeting
import pl.straszewski.service.PlannerService
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

import static org.junit.Assert.assertEquals

class PlannerServiceTest extends Specification {


    List<Meeting> meetingList
    PlannerService plannerService

    def setup() {
        plannerService = new PlannerService(meetingList);
        meetingList = new ArrayList<>()
        meetingList.add(new Meeting(
                LocalDateTime.of(2020, 12, 12, 8, 0, 0),
                LocalDateTime.of(2020, 12, 12, 9, 0, 0),
                "Example",
                1,
                "Description"
        ))
        meetingList.add(new Meeting(
                LocalDateTime.of(2020, 12, 12, 15, 0, 0),
                LocalDateTime.of(2020, 12, 12, 17, 0, 0),
                "Example 2",
                1,
                "Description 2"
        ))
        meetingList.add(new Meeting(
                LocalDateTime.of(2020, 12, 13, 13, 0, 0),
                LocalDateTime.of(2020, 12, 13, 15, 30, 0),
                "Example 3",
                2,
                "Description 3"
        ))


    }

    def "shouldReturnTrueIfNewMeetingIsBetween8And18"() {
        given: "data with correct hours"
        LocalDateTime newMeetingStartDate = LocalDateTime.of(2020, 12, 12, 10, 0, 0)
        LocalDateTime newMeetingEndDate = LocalDateTime.of(2020, 12, 12, 11, 0, 0)
        when:
        def result = plannerService.checkIfNewMeetingIsBetween8And18(newMeetingStartDate, newMeetingEndDate)
        then:
        assertEquals(true, result)
    }
    @Unroll
    def "shouldReturnTrueIfNewMeetingIsBeyond8And18 for #meetingStart and #meetingEnd"() {
        given: "data with incorrect hours meeting"

        when:
        def result = plannerService.checkIfNewMeetingIsBetween8And18(meetingStart, meetingEnd)
        then:
        thrown(MeetingBeyond8And18Exception)

        where:
        meetingStart                             || meetingEnd
        LocalDateTime.of(2020, 12, 12, 7, 0, 0)  || LocalDateTime.of(2020, 12, 12, 9, 0, 0)
        LocalDateTime.of(2020, 12, 12, 17, 0, 0) || LocalDateTime.of(2020, 12, 12, 19, 0, 0)

    }

    def "shouldReturnNotEmptyListWhenExistOtherMeetingThisDay"() {
        given: "insert new meeting to day where other meeting exist"
        int meetingRoom = 1
        LocalDateTime newMeetingStartDate = LocalDateTime.of(2020, 12, 12, 10, 0, 0)

        when:
        def listOfMeetingsAtThisDay = plannerService.existMeetingsAtThisDay(meetingList, meetingRoom, newMeetingStartDate);
        then:
        assertEquals(2, listOfMeetingsAtThisDay.size())
    }

    def "shouldReturnEmptyListWhenNotExistOtherMeetingThisDay"() {
        given: "insert new meeting to day where other meeting exist"
        int meetingRoom = 1
        LocalDateTime newMeetingStartDate = LocalDateTime.of(2020, 12, 10, 10, 0, 0)

        when:
        def listOfMeetingsAtThisDay = plannerService.existMeetingsAtThisDay(meetingList, meetingRoom, newMeetingStartDate);
        then:
        assertEquals(true, listOfMeetingsAtThisDay.isEmpty())
    }

    def "shouldReturnFalseWhenThereIsNoConflictWithOtherMeetingsThisDay"() {
        given:
        LocalDateTime newMeetingStartDate = LocalDateTime.of(2020, 12, 13, 15, 30, 0)
        LocalDateTime newMeetingEndDate = LocalDateTime.of(2020, 12, 13, 16, 30, 0)
        int meetingRoom = 2
        when:
        def result = plannerService.checkIfNewMeetingConflictToOtherMeetingsAtThisDay(
                meetingList,meetingRoom, newMeetingStartDate, newMeetingEndDate)

        then:
        assertEquals(false, result)
    }

    def "shouldReturnExceptionWhenThereIsConflictWithOtherMeetingsThisDay"() {
        given:"given data which execute conflict"
        LocalDateTime newMeetingStartDate = LocalDateTime.of(2020, 12, 13, 15, 0, 0)
        LocalDateTime newMeetingEndDate = LocalDateTime.of(2020, 12, 13, 16, 30, 0)
        int meetingRoom = 2
        when:
        plannerService.checkIfNewMeetingConflictToOtherMeetingsAtThisDay(
                meetingList,meetingRoom, newMeetingStartDate, newMeetingEndDate)

        then:"exception 'ConflictMeetingException' should be thrown"
        thrown(ConflictMeetingException)
    }

    def "shouldCountProperMeetingDuration"()
    {
        given:
        LocalDateTime newMeetingStartDate = LocalDateTime.of(2020, 12, 13, 15, 0, 0)
        LocalDateTime newMeetingEndDate = LocalDateTime.of(2020, 12, 13, 16, 30, 0)
        when:
        def durationOfNewMeeting = plannerService.countDurationOfNewMeeting(newMeetingStartDate, newMeetingEndDate)
        then:
        assertEquals(90,durationOfNewMeeting)
    }

    def shouldReturnCorrectAvailableTermsForNewMeeting()
    {
        given:
        LocalDateTime newMeetingStartDate = LocalDateTime.of(2020, 12, 12, 8, 30, 0)
        LocalDateTime newMeetingEndDate = LocalDateTime.of(2020, 12, 12, 9, 30, 0)
        int meetingRoom = 1

        List<AvailableTerms> expectedTerms = new ArrayList<>();
        expectedTerms.add(new AvailableTerms(
                LocalDateTime.of(2020, 12, 12, 9, 0, 0),
                LocalDateTime.of(2020, 12, 12, 15, 0, 0))
        )

        expectedTerms.add(new AvailableTerms(
                LocalDateTime.of(2020, 12, 12, 17, 0, 0),
                LocalDateTime.of(2020, 12, 12, 18, 0, 0))
        )
        when:
        List<AvailableTerms> availableTermAtThisDay = plannerService.findAvailableTermAtThisDay(
                meetingList, meetingRoom, newMeetingStartDate, newMeetingEndDate)
        then:
         expectedTerms.get(0).formattedMeetingStartDate == availableTermAtThisDay.get(0).formattedMeetingStartDate
         expectedTerms.get(0).formattedMeetingEndDate == availableTermAtThisDay.get(0).formattedMeetingEndDate
        expectedTerms.get(1).formattedMeetingStartDate == availableTermAtThisDay.get(1).formattedMeetingStartDate
        expectedTerms.get(1).formattedMeetingEndDate == availableTermAtThisDay.get(1).formattedMeetingEndDate
    }


}
