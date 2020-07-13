package pl.straszewski.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import pl.straszewski.repository.MeetingRepository;

import pl.straszewski.service.MeetingService;
import pl.straszewski.service.PlannerService;

@Configuration
public class AppConfiguration {

    final MeetingRepository meetingRepository;

    public AppConfiguration(MeetingRepository meetingRepository, MeetingService meetingService) {
        this.meetingRepository = meetingRepository;

    }

    @Bean
    public PlannerService plannerService()
    {
        return new PlannerService(meetingRepository.findAll());
    }


}
