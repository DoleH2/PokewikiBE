/**
 * HungLV2185
 */
package myproject.pokewiki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import myproject.pokewiki.entities.UserAccount;
import myproject.pokewiki.verifymail.VerificationToken;

/**
 * @author HungLV2185
 *
 */
public interface VerifyTokenRepo extends JpaRepository<VerificationToken, Long> {
	@Query(nativeQuery = true,
			value = "SELECT VT.id,VT.expiry_time,VT.token FROM verification_token VT "
					+ "JOIN user_account U ON VT.user_account_id = U.id "
					+ "WHERE U.id = :id")
	VerificationToken findVerTokenWithUser(@Param("id") UserAccount userAccount);
	
	VerificationToken findByToken(String token);
}
