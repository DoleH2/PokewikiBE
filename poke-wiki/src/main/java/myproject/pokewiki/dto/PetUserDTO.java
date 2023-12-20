/**
 * HungLV2185
 */
package myproject.pokewiki.dto;

import org.springframework.beans.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import myproject.pokewiki.entities.PetUser;

/**
 * @author HungLV2185
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PetUserDTO {
	private long idPetUser;
	private long idPet;
	private int statsIVHp;
	private int statsIVAtk;
	private int statsIVDef;
	private int statsIVSAtk;
	private int statsIVSDef;
	private int statsIVSpd;
	private long xp;
	private int level;
	
	public PetUserDTO(PetUser petUser) {
		BeanUtils.copyProperties(petUser, this);
	}
}
