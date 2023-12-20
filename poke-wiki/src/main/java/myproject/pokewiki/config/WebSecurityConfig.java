package myproject.pokewiki.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import myproject.pokewiki.jwt.JwtAuthenticationEntryPoint;
import myproject.pokewiki.jwt.JwtAuthenticationFilter;

@Configuration
public class WebSecurityConfig {
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	private JwtAuthenticationFilter jwtRequestFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
	    // Chúng ta không cần CSRF trong ví dụ này
	    httpSecurity
	        .csrf().disable() // Tắt bảo vệ CSRF
	        .authorizeHttpRequests() // Bắt đầu cấu hình quy tắc phân quyền
	            .antMatchers("/login","/register","/signout","/resend-token",
	            		"/registration-confirm","/reset-password"
	            		,"/confirm-reset-pass").permitAll() // Các đường dẫn không yêu cầu xác thực
	            .antMatchers("/user/**","/gacha","/signout").hasRole("USER") // Các đường dẫn yêu cầu vai trò "USER"
	            .antMatchers("/admin/**","/gacha").hasRole("ADMIN") // Các đường dẫn yêu cầu vai trò "ADMIN"
	            .anyRequest().authenticated() // Mọi yêu cầu khác đều phải được xác thực
	        .and()
	        .exceptionHandling() // Cấu hình xử lý ngoại lệ
	            .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Thiết lập điểm vào xác thực JWT
	        .and()
	        .sessionManagement() // Cấu hình quản lý phiên
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Sử dụng chính sách tạo phiên STATELESS (không sử dụng phiên)

	    // Thêm một bộ lọc để xác thực các mã thông báo với mỗi yêu cầu
	    httpSecurity.addFilterBefore(jwtRequestFilter, 
	    		UsernamePasswordAuthenticationFilter.class);

	    return httpSecurity.build();
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();

		// Cấu hình các tùy chọn CORS tại đây
		config.addAllowedOrigin("*"); // Chấp nhận tất cả các nguồn
		config.addAllowedHeader("*"); // Chấp nhận tất cả các tiêu đề
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");

		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	// Cấu hình mã hóa mật khẩu, sử dụng BCrypt.
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
