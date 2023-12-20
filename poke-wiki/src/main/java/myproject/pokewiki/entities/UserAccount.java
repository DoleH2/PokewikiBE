/**
 * HungLV2185
 */
package myproject.pokewiki.entities;




import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import myproject.pokewiki.verifymail.VerificationToken;

/**
 * @author HungLV2185
 *
 */
@Entity
@Table(name="UserAccount")
@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(unique = true)
	private String email;
	private String name;
	private String password;
	private String role;	
	private double cash;
	private boolean actived;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "userAccount")
	private VerificationToken token;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userAccount")
	private List<PetUser> petUser;
	
    public UserAccount() {
		super();
		this.actived = false;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userAccount")
    private List<PetUser> listPetUser;
}
