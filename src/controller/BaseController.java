package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import orion.annotation.Request;
import orion.annotation.Response;
import security.Principal;

public class BaseController extends epsilon.controller.BaseController {

	protected Principal principal;

	protected HttpServletRequest request;

	protected HttpServletResponse response;

	@Request
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		this.principal = (Principal) request.getAttribute(Principal.class.getCanonicalName());
	}

	@Response
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

}
