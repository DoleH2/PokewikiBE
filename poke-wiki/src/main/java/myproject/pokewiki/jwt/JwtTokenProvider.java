package myproject.pokewiki.jwt;



import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

import myproject.pokewiki.payloaduser.CustomUser;

/**
 * Cung cấp các phương thức hỗ trợ cho việc tạo, xác thực và quản lý JWT trong
 * ứng dụng Spring Security. Các phương thức này giúp thực hiện các chức năng an
 * toàn xác thực và duy trì phiên đăng nhập giữa client và server.
 */
@Component
public class JwtTokenProvider {

	// Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
	private final String JWT_SECRET = "HungLV2185";

	// Thời gian có hiệu lực của chuỗi jwt
	private final long JWT_EXPIRATION = 604800000L;

	private final String JWWT_COOKIE = "Poke";

	// Tạo ra jwt từ thông tin user
	public String generateToken(CustomUser userDetails) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
		// Tạo chuỗi json web token từ id của user.
		return Jwts.builder().setSubject(userDetails.getEmail())
				.setIssuedAt(now).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact();
	}

	// Lấy thông tin user từ jwt
	public String getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(JWT_SECRET)
				.parseClaimsJws(token)
				.getBody();
		return (claims.getSubject());
	}

	// Lấy chuỗi JWT từ cookie có tên là Poke trong request. Phương thức này được
	// sử dụng để lấy token từ cookie để kiểm tra và xác thực.
	public String getJwtFromCookies(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, JWWT_COOKIE);
		if (cookie != null) {
			return cookie.getValue();
		} else {
			return null;
		}
	}

	// Tạo một cookie chứa chuỗi JWT.
	// Sử dụng cookie để truyền thông tin xác thực giữa client và server.
	public ResponseCookie generateJwtCookie(CustomUser userPrincipal) {
		String jwt = generateToken(userPrincipal);
		ResponseCookie cookie = ResponseCookie.from(JWWT_COOKIE, jwt)
				.path("/")
				.domain("localhost")
				.maxAge(24 * 60 * 60)
				.httpOnly(true)
				.build();
		return cookie;
	}
	public Cookie getJwtCookie(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, JWWT_COOKIE);
		return cookie;
	}
	// Tạo một cookie không chứa giá trị (có thể dùng để xóa cookie).
	public ResponseCookie getCleanJwtCookie() {
		ResponseCookie cookie = ResponseCookie.from(JWWT_COOKIE, null).maxAge(0).path("/").build();
		return cookie;
	}

	// Kiểm tra tính hợp lệ của token.
	// Nếu token hợp lệ, phương thức trả về true, ngược lại trả về false.
	// Bắt và xử lý các loại lỗi có thể xảy ra, bao gồm MalformedJwtException,
	// ExpiredJwtException, UnsupportedJwtException, và IllegalArgumentException.
	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
			return true;
		} catch (MalformedJwtException ex) {

		} catch (ExpiredJwtException ex) {

		} catch (UnsupportedJwtException ex) {

		} catch (IllegalArgumentException ex) {
		}
		return false;
	}

}
