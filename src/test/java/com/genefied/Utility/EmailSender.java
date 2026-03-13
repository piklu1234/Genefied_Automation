package com.genefied.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.testng.Reporter;

import com.genefied.nonSAAS.Vega.VegaTesting;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailSender {

//    private Properties pro;
    String logoUrl = "https://vega.genuinemark.org/images/logo-vega.png";

    public void sendReport() throws Exception {

//        allUtility util = new allUtility();
//        pro = util.loadPropertiesFile();

        final String fromEmail = "ganesh@genefied.ai";
        final String password = System.getenv("EMAIL_PASS");
        final String toEmail = "satgan1764@gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
            String currentDate = dateFormat.format(Calendar.getInstance().getTime());

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Automation Execution Status - " + currentDate);

            MimeBodyPart messageBodyPart = new MimeBodyPart();

            List<TestStepResult> steps = VegaTesting.stepResults;

            

            StringBuilder html = new StringBuilder();

            html.append("<html>");
            html.append("<body style='font-family:Arial;margin:0;background:#f4f4f4;'>");

            html.append("<div style='background:#e41f26;padding:30px;text-align:center;color:white;'>");
            html.append("<img src='").append(logoUrl).append("' height='60'><br>");
            html.append("<h2 style='margin:10px 0 0 0;'>Automation Report</h2>");
            html.append("<p style='margin:5px;'>Report generated on ").append(currentDate).append("</p>");
            html.append("</div>");

            html.append("<div style='padding:30px;'>");

            html.append("<table style='border-collapse:collapse;width:100%;font-size:14px;'>");

            html.append("<tr style='background:#9d1f1f;color:white;'>");
            html.append("<th style='padding:10px;border:1px solid #ddd;'>Scenario</th>");
            html.append("<th style='padding:10px;border:1px solid #ddd;'>Status</th>");
            html.append("</tr>");

            if (steps == null || steps.isEmpty()) {

                html.append("<tr>");
                html.append("<td colspan='2' style='padding:10px;border:1px solid #ddd;text-align:center;'>");
                html.append("No scenarios executed");
                html.append("</td>");
                html.append("</tr>");

            } else {

                for (TestStepResult step : steps) {

                    String color = step.getStatus().equalsIgnoreCase("PASS") ? "#2ecc71" : "#e74c3c";

                    html.append("<tr>");

                    html.append("<td style='padding:10px;border:1px solid #ddd;'>");
                    html.append(step.getStepName());
                    html.append("</td>");

                    html.append("<td style='padding:10px;border:1px solid #ddd;color:");
                    html.append(color);
                    html.append(";font-weight:bold;'>");
                    html.append(step.getStatus());
                    html.append("</td>");

                    html.append("</tr>");
                }
            }

            html.append("</table>");

            html.append("<br>");
            html.append("<p>Regards,<br>Automation Team</p>");
            html.append("</div>");

            html.append("</body>");
            html.append("</html>");

            messageBodyPart.setContent(html.toString(), "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            Reporter.log("Email Sent Successfully!", true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}