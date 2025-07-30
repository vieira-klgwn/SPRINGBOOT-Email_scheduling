package rca.ac.rw.scheduling;

import org.springframework.scheduling.annotation.Scheduled;

public  class Automation {
    @Scheduled(fixedRate = 10000)
    public void surveyTenSeconds() {
        System.out.println("Automation survey ten seconds");
    }
}