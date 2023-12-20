/**
 * HungLV2185
 */
package myproject.pokewiki.services.verifyimpl;

import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import myproject.pokewiki.entities.UserAccount;
import myproject.pokewiki.services.EmailSecurityService;
import myproject.pokewiki.services.VerificationTokenService;
import myproject.pokewiki.verifymail.VerificationToken;

/**
 * @author HungLV2185
 *
 */
@Service
public class EmailSecurityServiceImpl implements EmailSecurityService {
	@Autowired
	private JavaMailSender emailSender;
	@Autowired
	private VerificationTokenService verifyTokenService;
	public void sendMail(SimpleMailMessage mail) {
		emailSender.send(mail);
		
	}
	public void sendVerifyEmail(UserAccount user) {
		VerificationToken verifyToken = verifyTokenService.createVerificationToken(user);
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmail());
		mail.setSubject("Verification Account Pokewiki");
		mail.setText("Click here to verify. Link out of date 10 min "+"\r\n"
		+"http://localhost:8080"+"/registration-confirm?token="+verifyToken.getToken());
		emailSender.send(mail);
	}
	/**
	 * @author HungLV2185
	 *
	 */
	@Override
	public void sendPasswordMail(String mail,String pass) {
		// TODO Auto-generated method stub
		SimpleMailMessage simplemail = new SimpleMailMessage();
		simplemail.setTo(mail);
		simplemail.setSubject("Reset Password Pokewiki");
		simplemail.setText("Here is your new password : \r\n"+pass);
		emailSender.send(simplemail);
		
	}
	/**
	 * @author HungLV2185
	 *
	 */
	@Override
	public void sendConfirmResetPassMail(UserAccount user) {
		// TODO Auto-generated method stub
		VerificationToken verifyToken = verifyTokenService.createVerificationToken(user);
		MimeMessage  simplemail = emailSender.createMimeMessage();
		MimeMessageHelper mailHelper = new MimeMessageHelper(simplemail,"utf-8");
		try {
			mailHelper.setTo(user.getEmail());
			mailHelper.setSubject("Confirm Reset Password Pokewiki");
			mailHelper.setText("Did you just submit a password reset request?<br>"+
					"<a href=\"http://localhost:8080/reset-password?token="+verifyToken.getToken()+"\">"
					+ "   <button style=\"width:200px;height:50px;"
					+ "background:#F62447;color:white;border:0\">"
					+ "Click Here To Reset Password</button>"
					+ "</a>",true);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		simplemail.setText("You want to reset password ? Click here : \r\n"+
//				"<button href='%s'>Click Here To Reset Password</button>",  
//						"http://localhost:8080/reset-password?token="+verifyToken.getToken(),
//						"Click Here To Reset Password"));

		emailSender.send(simplemail);
		
	}
	
}
