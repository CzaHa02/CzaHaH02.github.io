package com.crud.tasks.scheduler;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.Mail;
import com.crud.tasks.repository.TaskRepository;
import com.crud.tasks.service.SimpleEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.crud.tasks.service.TrelloService.TOCCMAIL;

@Component
@RequiredArgsConstructor
public class EmailScheduler {
    private final TaskRepository taskRepository;
    private static final String SUBJECT = "Tasks: Once a day email";
    private final SimpleEmailService simpleEmailService;
    private final AdminConfig adminConfig;


    @Scheduled(cron = "0 0 10 * * *")
    public void sendInformationEmail() {
        long size = taskRepository.count();

        String tasks ="tasks";
        if(size==1){tasks.replace("tasks", "task");}


        simpleEmailService.send(
                new Mail(
                        adminConfig.getAdminMail(),
                        SUBJECT,
                        "Currently in database you got: "+tasks,
                        TOCCMAIL
                )
        );
    }
}