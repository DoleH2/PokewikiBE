/**
 * HungLV2185
 */
package myproject.pokewiki.services;

import myproject.pokewiki.entities.UserAccount;
import myproject.pokewiki.verifymail.VerificationToken;

/**
 * @author HungLV2185
 *
 */
public interface VerificationTokenService {
	public VerificationToken createVerificationToken(UserAccount user);
	public VerificationToken getVertificationToken(String token);
	public boolean checkOutOfDate(VerificationToken verToken);
}
