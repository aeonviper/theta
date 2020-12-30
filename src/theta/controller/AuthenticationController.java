package theta.controller;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.inject.Inject;
import javax.servlet.http.Cookie;

import io.fusionauth.jwt.domain.JWT;
import orion.annotation.Parameter;
import orion.annotation.Path;
import orion.view.View;
import theta.core.Constant;
import theta.core.Utility;
import theta.model.Person;
import theta.security.Principal;
import theta.service.PersonService;

public class AuthenticationController extends BaseController {

	@Inject
	PersonService personService;

	@Path(value = "/system/me", allow = {}, deny = {})
	public View me() {
		return ok(principal);
	}

	@Path(value = "/system/password/edit", allow = {}, deny = {})
	public View editPassword(@Parameter("currentPassword") String currentPassword, @Parameter("newPassword") String newPassword, @Parameter("confirmNewPassword") String confirmNewPassword) {
		if (!( //
		validateRequiredString( //
				"currentPassword", currentPassword, //
				"newPassword", newPassword, //
				"confirmNewPassword", confirmNewPassword //
		) //
		)) {
			return badRequestNotification;
		}

		Person person = personService.findById(principal.getId());
		if (person != null) {
			if (Utility.checkPassword(currentPassword, person.getPassword())) {
				if (newPassword.equals(confirmNewPassword)) {
					person.setPassword(Utility.hashPassword(newPassword));
					return personService.editPassword(person) == 1 ? ok : error;
				} else {
					return badRequest("New password does not match confirm new password");
				}
			} else {
				return badRequest("Incorrect current password");
			}
		}
		return notFound;
	}

	@Path(value = "/system/authentication/register", allow = {}, deny = {})
	public View register(@Parameter("person") Person person) {
		if (!( //
		validateRequired("person", person) //
				&& validateRequired("role", person.getRole()) //
				&& validateRequiredString( //
						"name", person.getName(), //
						"email", person.getEmail(), //
						"password", person.getPassword() //
				) //
		)) {
			return badRequestNotification;
		}

		person.setActive(Boolean.TRUE);
		person.setPassword(Utility.hashPassword(person.getPassword()));
		return personService.save(person) == 1 ? ok : error;
	}

	@Path(value = "/system/authentication/login", allow = {}, deny = {})
	public View login(@Parameter("email") String email, @Parameter("password") String password) {
		if (!( //
		validateRequiredString( //
				"email", email, //
				"password", password //
		) //
		)) {
			return badRequestNotification;
		}

		Person person = personService.findByEmail(email);
		if (person != null && Utility.checkPassword(password, person.getPassword())) {
			if (!Utility.isTrue(person.getActive())) {
				return unauthorized("You have been marked as inactive");
			} else {
				Principal principal = new Principal(person.getId(), person.getEmail(), person.getName(), person.getRole());

				JWT jwt = new JWT().setIssuer("theta").setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC)).setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(1200));
				jwt.addClaim("principal", Utility.gson.toJson(principal));
				String encodedJWT = JWT.getEncoder().encode(jwt, Constant.jwtSigner);

				principal.setToken(encodedJWT);

				Cookie cookie = new Cookie(Constant.jwtCookieName, encodedJWT);
				cookie.setPath("/");
				cookie.setMaxAge(-1);
				response.addCookie(cookie);

				return ok(principal);
			}
		}
		return badRequest("Invalid email and/or password");
	}

	@Path(value = "/system/authentication/logout", allow = {}, deny = {})
	public View logout() {
		// remove jwt from database whitelist
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (Constant.jwtCookieName.equals(cookie.getName())) {
					cookie.setMaxAge(0);
					cookie.setValue("");
					cookie.setPath("/");
					response.addCookie(cookie);
				}
			}
		}
		return ok;
	}

}
