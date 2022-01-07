package services;

import org.apache.commons.lang3.StringUtils;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.List;

@Singleton
public class MailerService {
    @Inject MailerClient mailerClient;

    public void sendEmail(String fromAddress, List<String> toAddresses, String subject, List<File> attachedFiles, String bodyText, String bodyHtml) {
//        String cid = "1234";
//        Email email = new Email()
//                .setSubject("Simple email")
//                .setFrom("Mister FROM <from@email.com>")
//                .addTo("Miss TO <to@email.com>")
//                // adds attachment
//                .addAttachment("attachment.pdf", new File("/some/path/attachment.pdf"))
//                // adds inline attachment from byte array
//                .addAttachment("data.txt", "data".getBytes(), "text/plain", "Simple data", EmailAttachment.INLINE)
//                // adds cid attachment
//                .addAttachment("image.jpg", new File("/some/path/image.jpg"), cid)
//                // sends text, HTML or both...
//                .setBodyText("A text messages")
//                .setBodyHtml("<html><body><p>An <b>html</b> messages with cid <img src=\"cid:" + cid + "\"></p></body></html>");
        if (StringUtils.isEmpty(fromAddress) ||
                toAddresses == null || toAddresses.isEmpty()){
            return;
        }
        Email email = new Email()
                .setSubject(subject)
                .setFrom(fromAddress);
        for (String to: toAddresses){
            email.addTo(to);
        }
        if (attachedFiles!=null&&!attachedFiles.isEmpty()){
            for (File file: attachedFiles){
                email.addAttachment(file.getName(), file);
            }
        }
        if (!StringUtils.isEmpty(bodyText)){
            email.setBodyText(bodyText);
        }
        if (!StringUtils.isEmpty(bodyHtml)){
            email.setBodyHtml(bodyHtml);
        }
        mailerClient.send(email);
    }
}
