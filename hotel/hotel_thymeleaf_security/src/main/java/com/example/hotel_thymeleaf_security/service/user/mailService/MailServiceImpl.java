package com.example.hotel_thymeleaf_security.service.user.mailService;

import com.example.hotel_thymeleaf_security.entity.dtos.MailDto;
import com.example.hotel_thymeleaf_security.entity.mail.MailEntity;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.exception.DataNotFoundException;
import com.example.hotel_thymeleaf_security.repository.mailRepository.MailRepository;
import com.example.hotel_thymeleaf_security.repository.userRepository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MailServiceImpl  implements MailService{
    private final JavaMailSender mailSender;
    private final MailRepository mailRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void sendMessageToUser(String toUserEmail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        UserEntity user = userRepository.findByEmail(toUserEmail).orElseThrow(()->new DataNotFoundException("User not found"));
        String address = "http://localhost:8082/auth/"+user.getId().toString()+"/verify-code";
        String msgHtml = verificationTemplate().replace("{{email_address}}", address).replace("{{support_email}}", sender);
         messageHelper.setTo(user.getEmail());
        messageHelper.setFrom(sender,"Hotelier");//sender
        messageHelper.setSubject("Verify Your Email Address \uD83D\uDCE9");//subject
        messageHelper.setText(msgHtml, true);// html format
        mailSender.send(mimeMessage);
        MailDto mailMessageString = new MailDto();
        mailMessageString.setMessage(msgHtml);
        create(mailMessageString);
    }

    @Override
    public MailEntity create(MailDto mailDto) {
        return mailRepository.save(modelMapper.map(mailDto, MailEntity.class));
    }

    @Override
    public MailEntity getById(UUID id) {
        return mailRepository.findById(id).orElseThrow(()->new DataNotFoundException("Mail not found"));
    }

    @Override
    public MailEntity update(MailDto mailDto, UUID id) {
        MailEntity map = modelMapper.map(mailDto, MailEntity.class);
        MailEntity found = mailRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Mail_ID not found"));
        found.setMessage(map.getMessage());
        return mailRepository.save(found);
    }

    @Override
    public void deleteById(UUID id) {
        mailRepository.deleteById(id);
    }

    private String verificationTemplate(){
        return " <div style=\"max-width:600px\" src=\"https://ci6.googleusercontent.com/proxy/JcGbXGufQYSnvb2iAMLHg755hAaRlFcKFQmR0fafbrOevmSrd2izblesx6TQpnRqJQMT2KH--7QvR_hDGPp6CwXnbkEJPD0ccY4itYsWwtNjfME12RNCd83K-_qB=s0-d-e1-ft#https://email-images.mindbox.ru/bc5b838e-ff2d-47aa-98f4-2cb4fa3bea8e/4.png\">\n" +
                "        <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"max-width:600px;min-width:320px\">\n" +
                "           \n" +
                "           <tbody><tr>\n" +
                "              <td align=\"left\">\n" +
                "                 <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" align=\"center\">\n" +
                "                    <tbody><tr>\n" +
                "                       <td width=\"4\" align=\"left\">\n" +
                "                          <div style=\"width:4px;height:4px;line-height:4px;font-size:0\">&nbsp;</div>\n" +
                "                       </td>\n" +
                "                       <td align=\"left\">\n" +
                "                          <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" align=\"center\"><tbody><tr>\n" +
                "                                <td>\n" +
                "                                   <div style=\"height:20px;line-height:20px;font-size:0\">&nbsp;</div>\n" +
                "                                </td>\n" +
                "                             </tr><tr>\n" +
                "                                <td>\n" +
                "                                   <div style=\"height:16px;line-height:16px;font-size:0\">&nbsp;</div>\n" +
                "                                </td>\n" +
                "                             </tr><tr>\n" +
                "                                <td>\n" +
                "                                   <table class=\"m_7549120552491775110bg-color\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" align=\"center\" style=\"border-radius:20px;background-color:#ffffff\" bgcolor=\"#ffffff\">\n" +
                "                                      <tbody><tr>\n" +
                "                                         <td width=\"20\" align=\"left\">\n" +
                "                                            <div style=\"height:20px;line-height:20px;font-size:0\">&nbsp;</div>\n" +
                "                                         </td>\n" +
                "                                         <td align=\"left\"><div style=\"height:32px;line-height:32px;font-size:0\">&nbsp;</div><div class=\"m_7549120552491775110white m_7549120552491775110font-16\" style=\"font-family:Arial,sans-serif;font-weight:300;line-height:20px;text-align:left;font-size:14px;color:#000000;max-width:550px\">Hey there,<br>\n" +
                "                                            <br>Welcome to <strong>Hotelier!</strong> We're excited to have you on board. To get started and unlock all the fantastic features, we need to verify your email address.<br>\n" +
                "                                            <br>Here's how to do it in a snap: <br>\n" +
                "                                            <br>Click on the button below to verify your email: <br>\n" +
                "                                            <br>\n" +
                "                                            <a href=\"{{email_address}}\" class=\"m_7549120552491775110white m_7549120552491775110bg-violet\" \n" +
                "                                            style=\"max-width:220px;width:100%;display:block;text-decoration:none!important;font-size:16px;line-height:20px;font-family:Arial,sans-serif;font-weight:400;border-radius:8px;text-align:center;box-sizing:border-box;border:0;background-color:#3f2aff;color:#ffffff!important;overflow:hidden;padding:14px 20px\"\n" +
                "                                            >Verify Email</a>\n" +
                "                                            <br>If the button doesn't work, simply copy and paste this link into your browser: <br>\n" +
                "                                            <br> {{email_address}} <br>\n" +
                "                                            <br>Easy, right? \uD83D\uDE80<br>\n" +
                "                                            <br>Don't worry; this is a standard security measure to keep your account safe. If you didn't sign up for <strong>Hotelier.uz</strong>, or if this email landed in your spam folder by mistake, no problem. You can safely ignore it.<br>\n" +
                "                                            <br>If you have any questions or need assistance, feel free to reach out to our awesome support team at <a href=\"mailto:{{support_email}}\">support</a>. We're here to help!<br>\n" +
                "                                            <br>Cheers to a fantastic journey with ! <br>\n" +
                "                                            <br>Best regards,<br>\n" +
                "                                            <br>The Hotelier Team<br></div><div style=\"height:24px;line-height:24px;font-size:22px\">&nbsp;</div>\n" +
                "                                            <div style=\"height:32px;line-height:32px;font-size:0\">&nbsp;</div></td>\n" +
                "                                         <td width=\"20\" align=\"right\">\n" +
                "                                            <div style=\"height:20px;line-height:20px;font-size:0\">&nbsp;</div>\n" +
                "                                         </td>\n" +
                "                                      </tr>\n" +
                "                                   </tbody></table>\n" +
                "                                </td>\n" +
                "                             </tr><tr>\n" +
                "                                <td>\n" +
                "                                   <div style=\"height:16px;line-height:16px;font-size:0\">&nbsp;</div>\n" +
                "                                </td>\n" +
                "                             </tr><tr>\n" +
                "                                \n" +
                "                             </tr>\n" +
                "                                                                             <tr>\n" +
                "                                <td>\n" +
                "                                   <div style=\"height:16px;line-height:16px;font-size:0\">&nbsp;</div>\n" +
                "                                </td>\n" +
                "                             </tr><tr>\n" +
                "                                <td>\n" +
                "                                   <table class=\"m_7549120552491775110bg-color\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" align=\"center\" style=\"border-radius:20px;background-color:#ffffff\" bgcolor=\"#ffffff\">\n" +
                "                                      <tbody>\n" +
                "                                      <tr>\n" +
                "                                         <td colspan=\"3\">\n" +
                "                                            <div style=\"height:1px;line-height:1px;font-size:0\">&nbsp;</div>\n" +
                "                                         </td>\n" +
                "                                      </tr>\n" +
                "                                   </tbody></table>\n" +
                "                                </td>\n" +
                "                             </tr>\n" +
                "                                                                             <tr>\n" +
                "                                <td>\n" +
                "                                   <div style=\"height:16px;line-height:16px;font-size:0\">&nbsp;</div>\n" +
                "                                </td>\n" +
                "                             </tr><tr>\n" +
                "\n" +
                "                                <td align=\"left\" style=\"background-color:#000000;border-radius:25px\">\n" +
                "\n" +
                "                                   <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" align=\"center\">\n" +
                "\n" +
                "                                      <tbody><tr>\n" +
                "\n" +
                "                                         <td width=\"20\" align=\"left\">\n" +
                "\n" +
                "                                            <div style=\"width:20px;height:20px;line-height:20px;font-size:0\">&nbsp;</div>\n" +
                "\n" +
                "                                         </td>\n" +
                "\n" +
                "                                         <td>\n" +
                "\n" +
                "                                            <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" align=\"center\">\n" +
                "\n" +
                "                                               <tbody><tr>\n" +
                "\n" +
                "                                                  <td>\n" +
                "\n" +
                "                                                     <div style=\"height:32px;line-height:32px;font-size:0\">&nbsp;</div>\n" +
                "\n" +
                "                                                     <div class=\"m_7549120552491775110white m_7549120552491775110font-16\" style=\"font-family:Arial,sans-serif;font-weight:300;line-height:20px;text-align:left;font-size:14px;color:#ffffff;max-width:570px\"\n" +
                "                                                     >По всем вопросам пишите на&nbsp;<a href=\"mailto:{{support_email}}\" class=\"m_7549120552491775110white\" style=\"text-decoration:underline!important;color:#ffffff!important\" target=\"_blank\"\n" +
                "                                                     >{{support_email}}</a><br>или звоните по&nbsp;телефону <a href=\"tel:+999999999\" class=\"m_7549120552491775110white\" \n" +
                "                                                     style=\"text-decoration:none!important;color:#ffffff!important\" target=\"_blank\">+99&nbsp;999‑99‑99</a></div>\n" +
                "\n" +
                "                                   <div style=\"height:32px;line-height:32px;font-size:0\">&nbsp;</div>\n" +
                "\n" +
                "                                   <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" align=\"left\" style=\"max-width:292px\">\n" +
                "\n" +
                "                                      <tbody><tr>\n" +
                "\n" +
                "                                         <td align=\"left\" width=\"36\">\n" +
                "\n" +
                "                                            <a href=\"#\">\n" +
                "\n" +
                "                                            <img width=\"36\" src=\"https://ci3.googleusercontent.com/proxy/xSnEsFssQ9JWa7wluXxyGj-UzKWcFMSA91nsnZfUnpEeD2ia-QD4Dt-zzNDQ8zy7N5863o8-kNYxUWlu442CHnC5-4putz9gEf74ZubozVcG3wLBQ6mt8STk9ypV=s0-d-e1-ft#https://email-images.mindbox.ru/e33f38fb-2db3-494f-891f-b2375f24cbc9/2.png\" alt=\"fb\" style=\"display:block;border:none;text-align:center;width:36px;height:auto\" class=\"CToWUd\" data-bit=\"iit\"></a>\n" +
                "\n" +
                "                                         </td>\n" +
                "\n" +
                "                                         <td align=\"left\" width=\"36\">\n" +
                "\n" +
                "                                            <a href=\"#\" style=\"text-decoration:none;color:#ffffff!important;width:36px;height:auto;display:block\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://tn-pki.mckw.ru/c/nJ0SAAAA4BcAAHCU/8aGWAw/e0l8U2u1IdK1TBeF/?u%3Dhttps%253A%252F%252Fvk.com%252Fskillbox_education&amp;source=gmail&amp;ust=1697046637232000&amp;usg=AOvVaw1eyXTBeKxBKpNxgndIelV7\">\n" +
                "\n" +
                "                                            <img width=\"36\" src=\"https://ci4.googleusercontent.com/proxy/iqj2Hk4U-T6f-auPf8m9smvZ36f0rzNtlwQbAk9C_amvEC7AzXtmk4lZMoi4mhErrd3NoqUeyAuO1MQ61m1Dv-0No2NcB9tygabt7AHv1C4PoRat3so93dcPrnEZ=s0-d-e1-ft#https://email-images.mindbox.ru/bb7770ba-8a3c-40d3-98b0-721cb92c7ec8/6.png\" alt=\"vk\" style=\"display:block;border:none;text-align:center;width:36px;height:auto\" class=\"CToWUd\" data-bit=\"iit\"></a>\n" +
                "\n" +
                "                                         </td>\n" +
                "\n" +
                "                                         <td align=\"left\" width=\"36\">\n" +
                "\n" +
                "                                            <a href=\"#\" style=\"text-decoration:none;color:#ffffff!important;width:36px;height:auto;display:block\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://tn-pki.mckw.ru/c/nJ0SAAAA4BcAAHCU/8qGWAw/i2GrAQCYLcwl2RZJ/?u%3Dhttps%253A%252F%252Fwww.youtube.com%252Fchannel%252FUCHJZFCpwlXV7Sie1dV6pQLw&amp;source=gmail&amp;ust=1697046637232000&amp;usg=AOvVaw24r92uYMalkRclZm35c1pf\">\n" +
                "\n" +
                "                                            <img width=\"36\" src=\"https://ci5.googleusercontent.com/proxy/k43w0c2lvqWwP4jYKqH51HnV6qcj1FWufC3mITSjb4BnKDXTB-uMEBup3GnJbnXVGY6BchaHa1XogowHB8N6XozWjXJb-Ho5RS7CNLcHyLMza-VbJAemONn24QLC=s0-d-e1-ft#https://email-images.mindbox.ru/3da9f59f-65bc-4dde-9d37-994c526e5221/5.png\" alt=\"youtube\" style=\"display:block;border:none;text-align:center;width:36px;height:auto\" class=\"CToWUd\" data-bit=\"iit\"></a>\n" +
                "\n" +
                "                                         </td>\n" +
                "\n" +
                "                                         <td align=\"left\" width=\"36\">\n" +
                "\n" +
                "                                            <a href=\"#\" style=\"text-decoration:none;color:#ffffff!important;width:36px;height:auto;display:block\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://tn-pki.mckw.ru/c/nJ0SAAAA4BcAAHCU/9KGWAw/KsRe-wfOxCAnloSe/?u%3Dhttps%253A%252F%252Ft.me%252Fskillbox_uz&amp;source=gmail&amp;ust=1697046637232000&amp;usg=AOvVaw1d--Sbn-z-KjaFwVgzCATh\">\n" +
                "\n" +
                "                                            <img width=\"36\" src=\"https://ci5.googleusercontent.com/proxy/G7KL8A8DPOWTCNx1gjCN5g2zvSIKjmoMYLZgl4G95dubEW_aZOktkrGfVfuepTvwMfxn0s9nDmIoF3uAjCSTMVpmRgnxLUlhBBFc4dsXu344uvz_4PpvpWOuhkcm=s0-d-e1-ft#https://email-images.mindbox.ru/8020d568-cefb-47e1-872f-7efa749d96d8/4.png\" alt=\"telegram\" style=\"display:block;border:none;text-align:center;width:36px;height:auto\" class=\"CToWUd\" data-bit=\"iit\"></a>\n" +
                "\n" +
                "                                         </td>\n" +
                "\n" +
                "                                         <td align=\"left\" width=\"36\">\n" +
                "\n" +
                "                                            <a href=\"#\" style=\"text-decoration:none;color:#ffffff!important;width:36px;height:auto;display:block\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://tn-pki.mckw.ru/c/nJ0SAAAA4BcAAHCU/9aGWAw/RHtbmiIcrrsci_4W/?u%3Dhttps%253A%252F%252Fwww.instagram.com%252Fskillbox.uz%252F&amp;source=gmail&amp;ust=1697046637232000&amp;usg=AOvVaw3bBP9W-B4dky73rm5IMUb9\">\n" +
                "                                            <img width=\"36\" src=\"https://ci3.googleusercontent.com/proxy/a4dnzvb9bo3b2lfWbS5LqQsD2qzUyM0SIHyIWRpOv7FEmnHvfeWPEb68qKwLEn4k7HESUso1lvuCSXwERD95XAO021rHtVxqdLuEb9E2EMXbzy7v48U1GxEHxjjm=s0-d-e1-ft#https://email-images.mindbox.ru/4cbf353e-6f60-4443-a578-c1fb90be509e/3.png\" alt=\"instagram\" style=\"display:block;border:none;text-align:center;width:36px;height:auto\" class=\"CToWUd\" data-bit=\"iit\"></a> </td>\n" +
                "\n" +
                "                                                        </tr>\n" +
                "\n" +
                "                                                     </tbody></table>\n" +
                "\n" +
                "                                                  </td>\n" +
                "\n" +
                "                                               </tr>\n" +
                "\n" +
                "                                               <tr>\n" +
                "\n" +
                "                                                  <td>\n" +
                "\n" +
                "                                                     <div style=\"height:32px;line-height:32px;font-size:0\"> </div>\n" +
                "\n" +
                "                                                     <div class=\"m_7549120552491775110white\" style=\"font-family:Arial,sans-serif;font-weight:300;line-height:20px;text-align:left;font-size:14px;color:#ffffff;max-width:460px\">Вы получили это письмо, потому что подписались на нашу рассылку. Если вы больше не хотите получать наши письма, нажмите <a href=\"https://tn-pki.mckw.ru/c/nJ0SAAAA4BcAAHCU/9qGWAw/pC1IfWt52thjizn2/?u=https%3A%2F%2Ftn-pki.mckw.ru%2Fu%2FnJ0SAAAA4BcAAHCU%2FU3N6adz8ZzUM00Qx\" class=\"m_7549120552491775110white\" style=\"text-decoration:none!important;color:#ffffff!important;font-weight:bold\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://tn-pki.mckw.ru/c/nJ0SAAAA4BcAAHCU/9qGWAw/pC1IfWt52thjizn2/?u%3Dhttps%253A%252F%252Ftn-pki.mckw.ru%252Fu%252FnJ0SAAAA4BcAAHCU%252FU3N6adz8ZzUM00Qx&amp;source=gmail&amp;ust=1697046637232000&amp;usg=AOvVaw2AnnaNt0_T1XPg5nK9gl9b\">сюда</a>   </div>\n" +
                "\n" +
                "                                                     <div style=\"height:32px;line-height:32px;font-size:0\"> </div>\n" +
                "\n" +
                "                                                  </td>\n" +
                "\n" +
                "                                               </tr>\n" +
                "\n" +
                "                                            </tbody></table>\n" +
                "\n" +
                "                                         </td>\n" +
                "\n" +
                "                                         <td width=\"20\" align=\"right\">\n" +
                "\n" +
                "                                            <div style=\"width:20px;height:20px;line-height:20px;font-size:0\"> </div>\n" +
                "\n" +
                "                                         </td>\n" +
                "\n" +
                "                                      </tr>\n" +
                "\n" +
                "                                   </tbody></table>\n" +
                "\n" +
                "                                </td>\n" +
                "\n" +
                "                             </tr><tr>\n" +
                "                                <td>\n" +
                "                                   <div style=\"height:20px;line-height:20px;font-size:0\">&nbsp;</div>\n" +
                "                                </td>\n" +
                "                             </tr></tbody></table>\n" +
                "                       </td>\n" +
                "                       <td width=\"4\" align=\"right\">\n" +
                "                          <div style=\"width:4px;height:4px;line-height:4px;font-size:0\">&nbsp;</div>\n" +
                "                       </td>\n" +
                "                    </tr>\n" +
                "                 </tbody></table>\n" +
                "              </td>\n" +
                "           </tr>\n" +
                "           \n" +
                "        </tbody></table>\n" +
                "     </div>";
    }

}