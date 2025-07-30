package rca.ac.rw.scheduling;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Scheduler {

    @Autowired
    private JavaMailSender mailSender;

    // List of quotes to cycle through
    private static final List<String> QUOTES = Arrays.asList(
            "The only way to do great work is to love what you do. – Steve Jobs",
            "Success is not final, failure is not fatal. – Winston Churchill",
            "Be the change that you wish to see in the world. – Mahatma Gandhi",
            "Stay hungry, stay foolish. – Steve Jobs",
            "The best way to predict the future is to create it. – Peter Drucker",
            "Life is what happens when you're busy making other plans. – John Lennon",
            "Do or do not, there is no try. – Yoda",
            "Every moment is a fresh beginning. – T.S. Eliot"
    );

    // Index to track the current quote
    private final AtomicInteger quoteIndex = new AtomicInteger(0);

    private void sendEmail(String subject, String htmlBody) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo("abayohirwajovin@gmail.com");
            helper.setSubject(subject);
            helper.setFrom("ntwali.isimbivieira1@gmail.com");
            helper.setText(htmlBody, true); // true indicates HTML content
            mailSender.send(message);
            System.out.println("Email sent for subject: " + subject);
        } catch (MessagingException e) {
            System.err.println("Failed to send email for " + subject + ": " + e.getMessage());

    }
    }

    @Scheduled(cron = "0 */2 * * * *") // Run every two minutes
    public void sendQuoteEveryTwoMinutes() {
        // Get the next quote and increment the index (cycle back to 0 if at the end)
        int currentIndex = quoteIndex.getAndUpdate(i -> (i + 1) % QUOTES.size());
        String quote = QUOTES.get(currentIndex);
        String timestamp = LocalDateTime.now().toString();

        // HTML email content
        String htmlBody = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; color: #333; background-color: #f4f4f4; padding: 20px; }
                    .container { max-width: 600px; margin: 0 auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
                    .header { text-align: center; background-color: #007bff; color: #fff; padding: 10px; border-radius: 8px 8px 0 0; }
                    .header h1 { margin: 0; font-size: 24px; }
                    .content { padding: 20px; }
                    .quote { font-style: italic; font-size: 18px; color: #007bff; border-left: 4px solid #007bff; padding-left: 15px; margin: 20px 0; }
                    .greeting { font-size: 16px; line-height: 1.6; }
                    .signature { font-size: 14px; color: #555; margin-top: 20px; }
                    .footer { text-align: center; font-size: 12px; color: #777; margin-top: 20px; border-top: 1px solid #eee; padding-top: 10px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>InspireDaily</h1>
                    </div>
                    <div class="content">
                        <p class="greeting">Dear Customer,</p>
                        <p class="greeting">Welcome back to our every-two-minute dose of inspiration! We're thrilled to share a spark of wisdom to brighten your day. Let this quote inspire you to dream big, stay positive, and embrace every moment with enthusiasm.</p>
                        <p class="quote">%s</p>
                        <p class="greeting">Take a moment to reflect on these words and let them fuel your motivation. Stay tuned for more quotes to keep your spirits high!</p>
                        <p class="signature">Warm regards,<br>The InspireDaily Team</p>
                    </div>
                    <div class="footer">
                        <p>Sent at: %s | <a href="#">Unsubscribe</a></p>
                        <p>Contact us at: <a href="mailto:support@inspiredaily.com">support@inspiredaily.com</a></p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(quote, timestamp);

        System.out.println("Sending quote at: " + timestamp + ": " + quote);
        sendEmail("Your Daily Dose of Inspiration", htmlBody);
    }
}
