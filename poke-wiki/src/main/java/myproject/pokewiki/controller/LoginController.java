package myproject.pokewiki.controller;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import myproject.pokewiki.dto.ErrorHandle;
import myproject.pokewiki.entities.UserAccount;
import myproject.pokewiki.jwt.JwtTokenProvider;
import myproject.pokewiki.payloaduser.AccountRequest;
import myproject.pokewiki.payloaduser.CustomUser;
import myproject.pokewiki.payloaduser.UserInfoResponse;
import myproject.pokewiki.repository.UserAccountRepo;
import myproject.pokewiki.services.EmailSecurityService;
import myproject.pokewiki.services.VerificationTokenService;
import myproject.pokewiki.verifymail.VerificationToken;


@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")
public class LoginController {
	@Autowired
	private EmailSecurityService emailService;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	UserAccountRepo userRepo;
	@Autowired
	private VerificationTokenService verTokenService;
	@Autowired
	private JwtTokenProvider tokenProvider;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseEntity<UserInfoResponse> authenticateUser(
			@RequestBody AccountRequest loginRequest) {
		// Xác thực thông tin người dùng Request lên
		// Nếu thông tin không chính xác, ném ra ngoại lệ
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getEmail(), loginRequest.getPassword()));

		// Set thông tin authentication vào Security Context
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Lấy thông tin chi tiết của người dùng từ đối tượng xác thực
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		// Trả về jwt cho người dùng.
		String jwt = tokenProvider.generateToken(customUser);

		// Tạo một cookie HTTP chứa token JWT.
		ResponseCookie jwtCookie = tokenProvider.generateJwtCookie(customUser);

//		ResponseCookie jwtCookie = ResponseCookie
//				.from("Poke", tokenProvider.generateJwtCookie(customUser).toString())
//				.httpOnly(true)
//				.sameSite("None")
//				.secure(true)
//				.path("/")
//				.maxAge(Math.toIntExact(10000))
//				.build();

		UserInfoResponse userResponse = new UserInfoResponse(
							customUser.getId(),
							customUser.getEmail(),
							customUser.getName(),
							customUser.getRole(),
							jwt
					);
		// Trả về trạng thái Http 200
		// Đồng thời đặt header cho cookie chứa token và trả về thông tin người dùng
		// UserInfoResponse
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.body(userResponse);
	}

	@GetMapping("/signout")
	public ResponseEntity<?> logoutUser(HttpServletRequest request
			,HttpServletResponse response) {
		if(request.getCookies() != null) {
			SecurityContextHolder.getContext().setAuthentication(null);
			ResponseCookie cookie = tokenProvider.getCleanJwtCookie();
			response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
		}
		return ResponseEntity.ok().body("Success");
	}
	
	@PostMapping("/confirm-reset-pass")
	public ResponseEntity<?> confirmResetPass(@RequestBody String email){
		UserAccount user = userRepo.findByEmail(email);
		if(user == null) {
			return ResponseEntity.badRequest()
					.body(new ErrorHandle("Email khong ton tai",null));
		}
		emailService.sendConfirmResetPassMail(user);
		return ResponseEntity.ok().body("Sent");
	}
	
	@GetMapping("/reset-password")
	public void resetPassword(@Param("token") String token, HttpServletResponse response){
		String newPass = UUID.randomUUID().toString();
		VerificationToken verToken = verTokenService.getVertificationToken(token);
		try {
			if(verToken == null || verTokenService.checkOutOfDate(verToken)) {
				response.sendRedirect("http://localhost:3000/");
			}else {
				UserAccount userAccount = verToken.getUserAccount();
				userAccount.setPassword(passwordEncoder.encode(newPass));
				userRepo.save(userAccount);
				
				emailService.sendPasswordMail(userAccount.getEmail(), newPass);
				
				response.sendRedirect("http://localhost:3000/");
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
