package pl.straszewski.service


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import pl.straszewski.DAOs.MeetingDAO
import pl.straszewski.exceptions.ConflictMeetingException
import pl.straszewski.exceptions.MeetingIsActualException
import pl.straszewski.exceptions.MeetingNotFoundException
import pl.straszewski.model.Meeting
import pl.straszewski.repository.MeetingRepository
import spock.lang.Specification

import java.time.LocalDateTime

import static org.junit.Assert.assertEquals

//@Commit
//@ActiveProfiles("test")
@DataJpaTest
class MeetingServiceTest extends Specification {

    @Autowired
    public MeetingRepository meetingRepository
    MeetingService meetingService;

    def setup() {
        meetingService = new MeetingService(meetingRepository)
        Meeting example = new Meeting(
                LocalDateTime.of(2020, 12, 12, 8, 0, 0),
                LocalDateTime.of(2020, 12, 12, 9, 0, 0),
                "Example",
                1,
                "Description"
        )
        def example2 = new Meeting(
                LocalDateTime.of(2020, 12, 12, 15, 0, 0),
                LocalDateTime.of(2020, 12, 12, 17, 0, 0),
                "Example 2",
                1,
                "Description 2"
        )
        def example3 = new Meeting(
                LocalDateTime.of(2020, 12, 13, 13, 0, 0),
                LocalDateTime.of(2020, 12, 13, 15, 30, 0),
                "Example 3",
                2,
                "Description 3"
        )
        meetingRepository.save(example)
        meetingRepository.save(example2)
        meetingRepository.save(example3)
    }

    def "shouldAddNewMeeting"() {

        given: "Create example new meeting"

        def newMeetingStartDate = LocalDateTime.of(2020, 12, 12, 10, 0, 0)
        def newMeetingEndDate = LocalDateTime.of(2020, 12, 12, 11, 0, 0)
        def title = "Example"
        def roomNumber = 1
        def description = "Description"


        when: "Invoke method which add new meeting if there is no conflict with others"
        meetingService.addMeeting(newMeetingStartDate, newMeetingEndDate, title, roomNumber, description)
        then: "Assert that new meeting was add to h2 database"
        def listOfMeetings = meetingService.meetingRepository.findAll()
        assertEquals(4, listOfMeetings.size())
    }

    def "shouldNotAddNewMeetingWhenConflictToOther"() {
        given: "Create example new meeting"
        def newMeetingStartDate = LocalDateTime.of(2020, 12, 12, 8, 30, 0)
        def newMeetingEndDate = LocalDateTime.of(2020, 12, 12, 9, 30, 0)
        def title = "Example"
        def roomNumber = 1
        def description = "Description"

        when: "Invoke method which add new meeting if there is no conflict with others"
        meetingService.addMeeting(newMeetingStartDate, newMeetingEndDate, title, roomNumber, description)

        then:
        then: "exception 'ConflictMeetingException' should be thrown"
        thrown(ConflictMeetingException)
    }

    def "shouldReturnCorrectListOfMeetings"() {
        given: "Create expected data, base on data which is send to h2 database"
        List<MeetingDAO> expectedResult = new ArrayList<>()
        expectedResult.add(new MeetingDAO(8, "Example", "2020-12-12 08:00:00", "2020-12-12 09:00:00", "true", 1, "Description"))
        expectedResult.add(new MeetingDAO(9, "Example 2", "2020-12-12 15:00:00", "2020-12-12 17:00:00", "true", 1, "Description 2"))
        expectedResult.add(new MeetingDAO(10, "Example 3", "2020-12-13 13:00:00", "2020-12-13 15:30:00", "true", 2, "Description 3"))

        when: "Invoke method which return list of meeting from h2 database"
        def allMeetings = meetingService.getAllMeetings()

        then: "Assert that two list have the same content"
        expectedResult.toString() == allMeetings.toString()
    }

    def "shouldDeleteMeetingWhenIsNotActual"() {
        given: "Given id of meeting which must be delete, and invoke method to cancel this meeting"
        long id = 11

        meetingService.cancelMeeting(id)

        when: "Invoke metod which delete meeting by id"
        meetingService.deleteMeeting(id)

        then: "Assert that meeting was delete"
        def getAllMeetings = meetingService.getAllMeetings()
        2 == getAllMeetings.size()
    }

    def "shouldNotDeleteMeetingWhenIsActual"() {
        given: "Given id of meeting which must be delete"
        long id = 14

        when: "Invoke metod which delete meeting by id"
        meetingService.deleteMeeting(id)

        then: "exception 'MeetingIsActualException' should be thrown"
        thrown(MeetingIsActualException)
    }

    def "shouldNotDeleteMeetingWhenNotExist"() {
        given: "Given id of meeting which must be delete"
        long id = 1

        when: "Invoke metod which delete meeting by id"
        meetingService.deleteMeeting(id)

        then: "exception 'MeetingNotFoundException' should be thrown"
        thrown(MeetingNotFoundException)
    }

    def "shouldDeleteAllNotActualMeetings"()
    {
        given:"Given id of meetings which must be delete"
        long meetingId = 20
        long meetingId2 = 21
        meetingService.cancelMeeting(meetingId)
        meetingService.cancelMeeting(meetingId2)

        when:"Invoke method which delete all not actual meetings"
        meetingService.deleteAllUnActualMeetings()

        then:"Assert that meeting was delete"
        1 == meetingRepository.findAll().size()
    }

    def "shouldCancelActualMeetingById" ()
    {   //23,24,25
        given:"Given id of meeting which must be cancel"
        long meetingId = 24

        when:"Invoke metod which cancel the meeting"
        meetingService.cancelMeeting(meetingId)

        then:"Assert that meeting was cancel"
        false == meetingRepository.findById(meetingId).get().isActual()
    }
}