package epsilon.filter;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import epsilon.core.AssetUtility;

public class AssetFilter implements Filter {

	public void init(FilterConfig config) throws ServletException {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String queryString = httpRequest.getQueryString();
		String servletPath = httpRequest.getServletPath();
		if (queryString != null && queryString.contains("download")) {
			File file = new File(servletPath);
			httpResponse.setHeader("Content-Disposition", "attachment; filename=\"" + AssetUtility.decodeAssetName(file.getName()) + "\"");
		}
		chain.doFilter(request, response);
	}

}
