/**
 * HungLV2185
 */
package myproject.pokewiki.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author HungLV2185
 *
 */
@Entity
@Table(name="PetUser")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PetUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	@ManyToOne
	@JoinColumn(name="idUser")
	private UserAccount userAccount;
}
