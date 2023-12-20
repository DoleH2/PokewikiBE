/**
 * HungLV2185
 */
package myproject.pokewiki.services.verifyimpl;

import java.util.Calendar;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myproject.pokewiki.entities.UserAccount;
import myproject.pokewiki.repository.VerifyTokenRepo;
import myproject.pokewiki.services.VerificationTokenService;
import myproject.pokewiki.verifymail.VerificationToken;

/**
 * @author HungLV2185
 *
 */
@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

	/**
	 * @author HungLV2185
	 *
	 */
	@Autowired
	private VerifyTokenRepo verifyTokenRepo;
	@Override
	public VerificationToken createVerificationToken(UserAccount user) {
		VerificationToken verToken = verifyTokenRepo.findVerTokenWithUser(user);
		String token = UUID.randomUUID().toString();
		if(verToken == null) {
			verToken = new VerificationToken();
			verToken.setToken(token);
			verToken.setUserAccount(user);
		}else {
			verToken.setToken(token);
			verToken.resetExpiryDate();
		}
		verifyTokenRepo.save(verToken);
		return verToken;
	}
	/**
	 * @author HungLV2185
	 *
	 */
	@Override
	public VerificationToken getVertificationToken(String token) {
		VerificationToken verToken = verifyTokenRepo.findByToken(token);
		return verToken;
	}
	/**
	 * @author HungLV2185
	 *
	 */
	@Override
	public boolean checkOutOfDate(VerificationToken verToken) {
		// TODO Auto-generated method stub
		Long timeNow = Calendar.getInstance().getTime().getTime();
		if(timeNow <= verToken.getExpiryTime()) {
			return false;
		}
		return true;
	}
	
	

}
