package tn.esprit.pidevspringbootbackend.RestControllers.Massoud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UserDTO;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IAuthService;

@RestController
public class GithubController {

	@Autowired
	private IAuthService authService;

	@GetMapping("/hello")
	public ResponseEntity<Object> hello() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getPrincipal() instanceof OAuth2User) {
			OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
			UserDTO userDTO = authService.createUserFromOAuth2(oAuth2User);
			return ResponseEntity.status(HttpStatus.FOUND).header("Location", "http://localhost:4200").build();
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated with OAuth2");
		}
	}

	//massoud
	@GetMapping("/oauth2/authorization/github")
	public String redirectToGitHubAuthorization() {
		return "redirect:/oauth2/authorization/github";
	}

	@GetMapping("/oauth2/authorization/google")
	public String redirectToGoogleAuthorization() {
		return "redirect:/oauth2/authorization/google";
	}

}

