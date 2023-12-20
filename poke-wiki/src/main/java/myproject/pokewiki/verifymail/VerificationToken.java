/**
 * HungLV2185
 */
package myproject.pokewiki.verifymail;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import myproject.pokewiki.entities.PetUser;
import myproject.pokewiki.entities.UserAccount;

/**
 * @author HungLV2185
 *
 */
@Entity
@Table(name="VerificationToken")
@Getter
@Setter
@AllArgsConstructor
@ToString
public class VerificationToken {
	private static final int EXPIRATION = 60*10;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String token;
	private Long expiryTime;
	@OneToOne
	private UserAccount userAccount;
	
	public VerificationToken() {
		super();
		this.expiryTime = calculateExpiryTime(EXPIRATION);
	}
	
	private Long calculateExpiryTime(int expiryTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.SECOND, expiryTime);
		return cal.getTime().getTime();
	}
	public void resetExpiryDate() {
		this.expiryTime = calculateExpiryTime(EXPIRATION);
	}

}
