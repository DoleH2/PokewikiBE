/**
 * HungLV2185
 */
package myproject.pokewiki.services;

import myproject.pokewiki.entities.PetUser;

/**
 * @author HungLV2185
 *
 */
public interface GachaService {
	public int randomNumber(int min, int max);
	public double randomNumber(double min, double max);
	public PetUser randomPokemon(String emailUser,long idPokemon);
}
