package pl.straszewski.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.straszewski.DAOs.MeetingDAO;
import pl.straszewski.DTOs.AddMeetingDTO;
import pl.straszewski.service.MeetingService;

import java.util.List;

@RestController
public class MeetingController {

    final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }


    @GetMapping(value = "/meeting")
    public HttpEntity<List<MeetingDAO>> getAllMeetings() {
        List<MeetingDAO> allMeetings = meetingService.getAllMeetings();
        return ResponseEntity.ok(allMeetings);
    }

    @PostMapping(value = "/meeting", consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<HttpStatus> addMeeting(@RequestBody AddMeetingDTO addMeetingDTO) {
        meetingService.addMeeting(
                addMeetingDTO.getMeetingStartDate(),
                addMeetingDTO.getMeetingEndDate(),
                addMeetingDTO.getTitle(),
                Integer.parseInt(addMeetingDTO.getRoom()),
                addMeetingDTO.getDescription());

        return new ResponseEntity<>(HttpStatus.OK);
    }



    @DeleteMapping(value = "/meeting", params = "id")
    public HttpEntity<HttpStatus> deleteUnActualMeeting(@RequestParam String id) {
        meetingService.deleteMeeting(Long.parseLong(id));


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/meeting")
    public HttpEntity<HttpStatus> deleteAllUnActualMeeting() {
        meetingService.deleteAllUnActualMeetings();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
