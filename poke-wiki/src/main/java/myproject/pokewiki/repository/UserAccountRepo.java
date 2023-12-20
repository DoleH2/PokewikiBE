/**
 * HungLV2185
 */
package myproject.pokewiki.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myproject.pokewiki.entities.UserAccount;

/**
 * @author HungLV2185
 *
 */
@Transactional
@Repository
public interface UserAccountRepo extends JpaRepository<UserAccount, Long> {

	/**
	 * @author HungLV2185
	 *
	 */
	UserAccount findByEmail(String email);
	
}
