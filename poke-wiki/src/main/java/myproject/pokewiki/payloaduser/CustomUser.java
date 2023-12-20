package myproject.pokewiki.payloaduser;



import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import myproject.pokewiki.entities.UserAccount;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomUser implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double id;
	private String email;
	private String name;
	private String password;
	private String role;
	
	
	/**
	 * @author HungLV2185
	 *
	 */
	public CustomUser(UserAccount user) {
		super();
		this.id = user.getId();
		this.email = user.getEmail();
		this.name = user.getName();
		this.password = user.getPassword();
		this.role = user.getRole();
	}
	
	/**
	 * @author HungLV2185
	 * set quyền Authorities
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return Collections.singletonList(new SimpleGrantedAuthority(role));
	}
	/**
	 * @author HungLV2185
	 *
	 */
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * @author HungLV2185
	 *
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}



}
