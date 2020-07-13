package pl.straszewski.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.straszewski.DAOs.MeetingDAO;
import pl.straszewski.DTOs.AddMeetingDTO;
import pl.straszewski.DTOs.LocalDateDTO;
import pl.straszewski.exceptions.*;
import pl.straszewski.service.MeetingService;

import java.util.List;

@Controller

public class PlannerController {
    final MeetingService meetingService;

    public PlannerController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        return "index";
    }

    @GetMapping("/add")
    public String addMeeting() {
        return "add";
    }

    @PostMapping(value = "/meeting-cancel")
    public String cancelMeeting(String id, Model model) {
        try {
            meetingService.cancelMeeting(Long.parseLong(id));
            model.addAttribute("correct", "Meeting canceled");
        } catch (MeetingIsNotActualException e) {
            model.addAttribute("exception", e.getMessage());
            return "meetingsAtDay";
        }
        return "meetingsAtDay";
    }

    @PostMapping(value = "/meeting-delete")
    public String deleteMeeting(String id, Model model) {

        try {
            meetingService.deleteMeeting(Long.parseLong(id));
            model.addAttribute("correct", "Meeting deleted");
        } catch (MeetingIsActualException e) {
            model.addAttribute("exception", e.getMessage());
            return "meetingsAtDay";
        }
        return "meetingsAtDay";
    }

    @ModelAttribute("addMeetingDTO")
    public AddMeetingDTO addMeetingDTO() {
        return new AddMeetingDTO();
    }


    @PostMapping(value = "/meeting-add")
    public String addMeeting(@ModelAttribute("addMeetingDTO") AddMeetingDTO addMeetingDTO, BindingResult result, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {

            return "add";
        }

        try {
            if (addMeetingDTO.isValid()) {
                meetingService.addMeeting(addMeetingDTO.getMeetingStartDate(), addMeetingDTO.getMeetingEndDate(), addMeetingDTO.getTitle(),
                        Integer.parseInt(addMeetingDTO.getRoom()), addMeetingDTO.getDescription());
                model.addAttribute("status", "Success");
            }
        }
        catch (ConflictMeetingException | MeetingBeyond8And18Exception |  MeetingDurationException e) {
            model.addAttribute("conflict", e.getMessage());
            return "add";
        }
        catch ( IncorrectDataException e) {
            model.addAttribute("exceptions", e.getErrors());
            return "add";
        }
        return "add";
    }

    @GetMapping("/meetingsAtDay")
    public String getProperDayMeetings() {
        return "meetingsAtDay";
    }

    @PostMapping("/meetingsAtDay")
    @ModelAttribute("meetings")
    public List<MeetingDAO> getMeetingsByDay(@ModelAttribute("localDateDTO") LocalDateDTO localDateDTO, BindingResult result, Model model) {
        List<MeetingDAO> allMeetingsAtThisDay = null;
        if (result.hasErrors()) {

        }
        try {
            if (localDateDTO.isValid())
                allMeetingsAtThisDay = meetingService.getAllMeetingsAtThisDay(localDateDTO.getLocalDate());

        } catch (MeetingNotFoundException e ) {
            model.addAttribute("exception", e.getMessage());
        }
        catch (IncorrectDataException e) {
            model.addAttribute("exceptions", e.getErrors());
        }

        return allMeetingsAtThisDay;
    }

    @ModelAttribute("localDateDTO")
    public LocalDateDTO localDateDTO() {
        return new LocalDateDTO();
    }



    @GetMapping("/aboutApi")
    public String aboutApi()
    {
        return "aboutApi";
    }


}
