/**
 * HungLV2185
 */
package myproject.pokewiki.controller;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import myproject.pokewiki.dto.ErrorHandle;
import myproject.pokewiki.entities.UserAccount;
import myproject.pokewiki.payloaduser.AccountRequest;
import myproject.pokewiki.repository.UserAccountRepo;
import myproject.pokewiki.services.EmailSecurityService;
import myproject.pokewiki.services.UserAccountService;
import myproject.pokewiki.services.VerificationTokenService;
import myproject.pokewiki.verifymail.VerificationToken;

/**
 * @author HungLV2185
 *
 */
@RestController
@RequestMapping("/")
@CrossOrigin("http://localhost:3000")
public class RegisterController {
	@Autowired
	private EmailSecurityService emailService;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private UserAccountRepo userAccountRepo;
	@Autowired
	private VerificationTokenService verTokenService;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(
			@RequestBody AccountRequest accountRequest ){
		try {
			UserAccount userAccount = userAccountService
					.createAccount(accountRequest);
			if(userAccount == null) {
				return ResponseEntity.internalServerError()
						.body(new ErrorHandle("Email da ton tai",null));
			}else {
				emailService.sendVerifyEmail(userAccount);
				return ResponseEntity.ok("Register success!");						
			}
		}catch (Exception e) {
			ErrorHandle errHandle = new ErrorHandle("Xay ra Loi",e.getMessage());
			return ResponseEntity.internalServerError()
					.body(errHandle);
		}
	}
	
	@PostMapping("/resend-token")
	public ResponseEntity<?> resendToken(@RequestBody AccountRequest accountRequest){
		UserAccount userAccount = userAccountRepo.findByEmail(accountRequest.getEmail());
		if(userAccount == null) {
			return ResponseEntity.badRequest().body(new ErrorHandle("Email khong ton tai",null));
		}else {
			emailService.sendVerifyEmail(userAccount);
			return ResponseEntity.ok().body("Resend thanh cong");
		}
	}
	@GetMapping("/registration-confirm")
	public void confirmToken(@Param("token") String token,HttpServletResponse response){
		try {
			VerificationToken verToken = verTokenService.getVertificationToken(token);
			if(verToken==null || verTokenService.checkOutOfDate(verToken)) {
				response.sendRedirect("http://localhost:3000/");
			}else {
				UserAccount user = verToken.getUserAccount();
				user.setActived(true);
				userAccountRepo.save(user);
				response.sendRedirect("http://localhost:3000/");				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
