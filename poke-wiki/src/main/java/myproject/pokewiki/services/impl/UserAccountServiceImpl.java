/**
 * HungLV2185
 */
package myproject.pokewiki.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import myproject.pokewiki.entities.UserAccount;
import myproject.pokewiki.payloaduser.AccountRequest;
import myproject.pokewiki.repository.UserAccountRepo;
import myproject.pokewiki.services.UserAccountService;

/**
 * @author HungLV2185
 *
 */
@Service
public class UserAccountServiceImpl implements UserAccountService {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserAccountRepo userAccountRepo;
	/**
	 * @author HungLV2185
	 *
	 */
	@Override
	public UserAccount createAccount(AccountRequest accountRequest) {
		// TODO Auto-generated method stub

		try {
			if (userAccountRepo.findByEmail(accountRequest.getEmail()) != null) {
				return null;
			}
			UserAccount userAccount = new UserAccount();
			userAccount.setEmail(accountRequest.getEmail());
			userAccount.setPassword(passwordEncoder.encode(
					accountRequest.getPassword()));
			userAccount.setRole("ROLE_USER");
			userAccount.setCash(0);
			userAccountRepo.save(userAccount);
			return userAccount;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
