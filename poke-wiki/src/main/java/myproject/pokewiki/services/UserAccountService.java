/**
 * HungLV2185
 */
package myproject.pokewiki.services;

import org.springframework.stereotype.Service;

import myproject.pokewiki.entities.UserAccount;
import myproject.pokewiki.payloaduser.AccountRequest;

/**
 * @author HungLV2185
 *
 */

public interface UserAccountService {
	public UserAccount createAccount(AccountRequest accountRequest);
}
