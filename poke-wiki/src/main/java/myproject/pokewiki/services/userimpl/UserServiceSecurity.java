package myproject.pokewiki.services.userimpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import myproject.pokewiki.entities.UserAccount;
import myproject.pokewiki.payloaduser.CustomUser;
import myproject.pokewiki.repository.UserAccountRepo;


@Service
public class UserServiceSecurity implements UserDetailsService {
	
	@Autowired
	private UserAccountRepo userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) {
		// Kiểm tra xem user có tồn tại trong database không?
		UserAccount user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException(email);
		}
		CustomUser customUser = new CustomUser(user);
		return customUser;
	}

}
