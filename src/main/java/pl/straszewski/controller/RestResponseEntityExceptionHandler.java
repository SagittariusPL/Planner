package pl.straszewski.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.straszewski.exceptions.ConflictMeetingException;
import pl.straszewski.exceptions.MeetingIsActualException;
import pl.straszewski.exceptions.MeetingNotFoundException;

import java.util.Collections;
import java.util.List;
//@RestControllerAdvice
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(value = {ConflictMeetingException.class})
//    public ResponseEntity<Object> handleConflictMeetingException(ConflictMeetingException e, WebRequest request) {
//        List<String> bodyOfResponse = Collections.singletonList(e.getMessage());
//        return handleExceptionInternal(e, bodyOfResponse,
//                new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
//    }

//    @ExceptionHandler(value = {MeetingNotFoundException.class})
//    public ResponseEntity<Object> handleMeetingNotFoundException(MeetingNotFoundException e, WebRequest request) {
//        String bodyOfResponse = e.getMessage();
//        return handleExceptionInternal(e, bodyOfResponse,
//                new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
//    }

    @ExceptionHandler(value = {MeetingNotFoundException.class})
    public String handleMeetingNotFoundException(MeetingNotFoundException e, Model model) {

        model.addAttribute("exception",e.getMessage());
        return "meetingsAtDay";
    }

    @ExceptionHandler(value = {MeetingIsActualException.class})
    public String handleMeetingIsActualException(MeetingIsActualException e, Model model) {

        model.addAttribute("exception",e.getMessage());
        return "meetingsAtDay";
    }

}
