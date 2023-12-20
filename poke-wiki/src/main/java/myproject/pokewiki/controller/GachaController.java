/**
 * HungLV2185
 */
package myproject.pokewiki.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import myproject.pokewiki.dto.PetUserDTO;
import myproject.pokewiki.entities.PetUser;
import myproject.pokewiki.jwt.JwtTokenProvider;
import myproject.pokewiki.repository.PetUserRepo;
import myproject.pokewiki.services.GachaService;

/**
 * @author HungLV2185
 *
 */
@RestController
@RequestMapping("/admin/")
public class GachaController {
	@Autowired
	private PetUserRepo petUserRepo;
	@Autowired
	private GachaService gachaService;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@GetMapping("/gacha")
	public ResponseEntity<?> gachaSystemGET(){
		System.out.println("vao");
		return null;
	}
	@PostMapping("/gacha")
	public ResponseEntity<?> gachaSystem(HttpServletRequest request){
		String jwt = tokenProvider.getJwtFromCookies(request);
		String email = tokenProvider.getUserIdFromJWT(jwt);
		int idPokemon = gachaService.randomNumber(1, 1017);
		PetUser petUser = gachaService.randomPokemon(email, idPokemon);
		petUserRepo.save(petUser);
		PetUserDTO petUserDTO = new PetUserDTO(petUser);
		return ResponseEntity.ok()
				.body(petUserDTO);

	}
}
