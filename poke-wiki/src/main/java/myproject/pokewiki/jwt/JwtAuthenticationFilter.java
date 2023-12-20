package myproject.pokewiki.jwt;



import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import myproject.pokewiki.payloaduser.CustomUser;
import myproject.pokewiki.services.userimpl.UserServiceSecurity;

/**
 * Mục đích chính của JwtAuthenticationFilter là thực hiện xác thực người dùng
 * dựa trên thông tin trong token JWT. Nếu xác thực thành công, thông tin người
 * dùng được cài đặt trong SecurityContextHolder, giúp Spring Security hiểu rằng
 * người dùng hiện đang được xác thực và cho phép truy cập các phần tài nguyên
 * được bảo vệ.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private UserServiceSecurity customUserDetailsService;

	// được gọi mỗi khi có request
	// Lấy chuỗi JWT từ request.
	// Nếu chuỗi JWT hợp lệ, kiểm tra và lấy thông tin người dùng từ chuỗi JWT.
	// Nếu thông tin người dùng hợp lệ, tạo và cài đặt Authentication trong
	// SecurityContextHolder
	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			// Lấy jwt từ request
			String jwt = getJwtFromRequest(request);
			if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
				// Lấy id user từ chuỗi jwt
				String userEmail = tokenProvider.getUserIdFromJWT(jwt);
				// Lấy thông tin người dùng từ id
				UserDetails userDetails = customUserDetailsService
						.loadUserByUsername(userEmail);

				if (userDetails != null) {
					Authentication  authentication =
							new UsernamePasswordAuthenticationToken(
									userDetails,null, userDetails.getAuthorities());
					SecurityContextHolder.getContext()
					.setAuthentication(authentication);
					
				}
			}
		} catch (Exception ex) {

		}

		filterChain.doFilter(request, response);
	}

	// Lấy chuỗi JWT từ header "Cookie" của request.
	// Đảm bảo rằng chuỗi JWT được trả về chỉ chứa token (loại bỏ tiền tố "Booking="
	// nếu có).
	private String getJwtFromRequest(HttpServletRequest request) {
		String[] bearerTokenArr = request.getHeader("Cookie").split("; ");
		String bearerToken = bearerTokenArr[0];
		// Kiểm tra xem header Authorization có chứa thông tin jwt không
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Poke=")) {
			return bearerToken.substring(5);
		}
		return null;
	}

}
