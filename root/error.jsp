<%
	response.setCharacterEncoding("UTF-8");
	response.setContentType("application/json");
	String message = "";
	Object object = request.getAttribute("javax.servlet.error.exception");
	if (object != null && object instanceof Throwable) {
		Throwable throwable = (Throwable) object;
		if (throwable.getCause() != null) {
			message = throwable.getCause().getMessage();
		} else {
			message = throwable.getMessage();
		}
		System.err.println("Throwable at " + java.time.LocalDateTime.now());
		throwable.printStackTrace();
	}
	java.io.PrintWriter pw = response.getWriter();
	pw.println(epsilon.core.Utility.gson.toJson(epsilon.core.Utility.makeMap( //
			"type", "NOTIFICATION", //
			"fieldErrorList", new String[0], //
			"errorList", new String[] { message }, //
			"noticeList", new String[0] //
	)));
%>