/**
 * HungLV2185
 */
package myproject.pokewiki.services.impl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myproject.pokewiki.entities.PetUser;
import myproject.pokewiki.entities.UserAccount;
import myproject.pokewiki.repository.UserAccountRepo;
import myproject.pokewiki.services.GachaService;

/**
 * @author HungLV2185
 *
 */
@Service
public class GachaServiceImpl implements GachaService {
	private final static double RATE_COMMON = 50.5;
	private final static double RATE_RARE = 35.5;
	private final static double RATE_EPIC = 12;
	private final static double RATE_LEGEND = 2;
	/**
	 * @author HungLV2185
	 *
	 */

	@Autowired
	private UserAccountRepo userRepo;
	
	public int randomNumber(int min, int max) {
		// TODO Auto-generated method stub
		Random random = new Random();
		int numRandom = random.nextInt(max-min)+min;
		return numRandom;
	}
	
	public double randomNumber (double min, double max) {
		Random random = new Random();
		double randomNumber = random.nextDouble(max-min) + min;
		return randomNumber;
	}
	
	public int randomRate() {
		double randomRate = randomNumber(1.0, 100.0);
		int statsIV;
		
		if(randomRate >= 1 && randomRate <= RATE_COMMON) {
			statsIV = randomNumber(1,15);
		}else if(randomRate > RATE_COMMON 
				&& randomRate <= (RATE_COMMON+RATE_RARE)) {
			statsIV = randomNumber(16, 25);
		}else if(randomRate >(RATE_COMMON+RATE_RARE) 
				&& randomRate <=(RATE_COMMON+RATE_RARE+RATE_EPIC)) {
			statsIV = randomNumber(26,29);
		}else {
			statsIV = randomNumber(30, 31);
		}
		return statsIV;
	}
	
	@Override
	public PetUser randomPokemon(String emailUser,long idPokemon) {
		PetUser newPet = new PetUser();
		newPet.setIdPet(idPokemon);
		newPet.setStatsIVHp(randomRate());
		newPet.setStatsIVAtk(randomRate());
		newPet.setStatsIVDef(randomRate());
		newPet.setStatsIVSAtk(randomRate());
		newPet.setStatsIVSDef(randomRate());
		newPet.setStatsIVSpd(randomRate());
		newPet.setLevel(0);
		newPet.setXp(0);
		UserAccount user = userRepo.findByEmail(emailUser);
		if(user!= null) {
			newPet.setUserAccount(user);
		}
		return newPet;	
	}

}
