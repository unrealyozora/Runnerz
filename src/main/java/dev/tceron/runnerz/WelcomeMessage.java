package dev.tceron.runnerz;
import org.springframework.stereotype.Component;

@Component
public class WelcomeMessage {
    public String getWelcomeMessage() {
        return "Welcome to the Runnerz application!";
    }
}
