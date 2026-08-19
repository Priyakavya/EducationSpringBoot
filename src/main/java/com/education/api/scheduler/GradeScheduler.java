package com.education.api.scheduler;


import com.education.api.entity.Grade;
import com.education.api.entity.GradeStatus;
import com.education.api.repository.GradeRepository; // ADD
import org.springframework.beans.factory.annotation.Autowired; // ADD
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class GradeScheduler {

    @Autowired // ADD
    private GradeRepository gradeRepository; // ADD

    // Runs every day at 2 AM
    @Scheduled(cron = "0 0 2 * * *")
    public void autoFailStudents() {
        System.out.println("Auto Fail Job Started at: " + LocalDateTime.now());
        
        List<Grade> allGrades = gradeRepository.findAll();
        int count = 0;
        
        for(Grade grade : allGrades) {
            if(grade.getScore() != null && grade.getScore().doubleValue() < 35.00) {
                grade.setStatus(GradeStatus.FAIL);
                gradeRepository.save(grade);
                count++;
            }
        }
        System.out.println("Auto Fail Job Completed. " + count + " students marked as FAIL");
    }
}