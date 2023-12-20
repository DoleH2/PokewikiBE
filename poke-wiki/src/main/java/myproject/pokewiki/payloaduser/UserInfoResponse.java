/**
 * HungLV2185
 */
package myproject.pokewiki.payloaduser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author HungLV2185
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfoResponse {
	private double id;
	private String email;
	private String name;
	private String role;
	private String jwt;
}
