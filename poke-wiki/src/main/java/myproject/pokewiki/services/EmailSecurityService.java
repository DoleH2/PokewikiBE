/**
 * HungLV2185
 */
package myproject.pokewiki.services;

import org.springframework.mail.SimpleMailMessage;

import myproject.pokewiki.entities.UserAccount;

/**
 * @author HungLV2185
 *
 */
public interface EmailSecurityService {
	public void sendMail(SimpleMailMessage mail);
	public void sendVerifyEmail(UserAccount user);
	public void sendPasswordMail(String mail, String password);
	public void sendConfirmResetPassMail(UserAccount user);
}
