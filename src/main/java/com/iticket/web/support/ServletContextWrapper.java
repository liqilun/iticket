package com.iticket.web.support;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

import org.apache.commons.lang.StringUtils;

public class ServletContextWrapper implements ServletContext {
	private ServletContext context = null;
	private Map<String, String> initParams = new HashMap<String, String>();

	public boolean setInitParams(String paramName, String paramValue) {
		if (StringUtils.isBlank(context.getInitParameter(paramName)) || StringUtils.isBlank(paramValue)) {
			return false;
		}
		initParams.put(paramName, paramValue);
		return true;
	}

	public ServletContextWrapper(ServletContext context) {
		this.context = context;
	}

	@Override
	public Object getAttribute(String name) {
		return context.getAttribute(name);
	}

	@Override
	public Enumeration getAttributeNames() {
		return context.getAttributeNames();
	}

	@Override
	public ServletContext getContext(String uripath) {
		return context.getContext(uripath);
	}

	@Override
	public String getContextPath() {
		return context.getContextPath();
	}

	@Override
	public String getInitParameter(String name) {
		String value = initParams.get(name);
		if (StringUtils.isNotBlank(value)) {
			return value;
		}
		return context.getInitParameter(name);
	}

	@Override
	public Enumeration getInitParameterNames() {
		return context.getInitParameterNames();
	}

	@Override
	public int getMajorVersion() {
		return context.getMajorVersion();
	}

	@Override
	public String getMimeType(String file) {
		return context.getMimeType(file);
	}

	@Override
	public int getMinorVersion() {
		return context.getMinorVersion();
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String name) {
		return context.getNamedDispatcher(name);
	}

	@Override
	public String getRealPath(String path) {
		return context.getRealPath(path);
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		return context.getRequestDispatcher(path);
	}

	@Override
	public URL getResource(String path) throws MalformedURLException {
		return context.getResource(path);
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		return context.getResourceAsStream(path);
	}

	@Override
	public Set getResourcePaths(String path) {
		return context.getResourcePaths(path);
	}

	@Override
	public String getServerInfo() {
		return context.getServerInfo();
	}

	@SuppressWarnings("deprecation")
	@Override
	public Servlet getServlet(String name) throws ServletException {
		return context.getServlet(name);
	}

	@Override
	public String getServletContextName() {
		return context.getServletContextName();
	}

	@SuppressWarnings("deprecation")
	@Override
	public Enumeration getServletNames() {
		return context.getServletNames();
	}

	@SuppressWarnings("deprecation")
	@Override
	public Enumeration getServlets() {
		return context.getServlets();
	}

	@Override
	public void log(String msg) {
		context.log(msg);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void log(Exception exception, String msg) {
		context.log(exception, msg);
	}

	@Override
	public void log(String message, Throwable throwable) {
		context.log(message, throwable);
	}

	@Override
	public void removeAttribute(String name) {
		context.removeAttribute(name);
	}

	@Override
	public void setAttribute(String name, Object object) {
		context.setAttribute(name, object);
	}

	@Override
	public Dynamic addFilter(String arg0, String arg1) {
		return context.addFilter(arg0, arg1);
	}

	@Override
	public Dynamic addFilter(String arg0, Filter arg1) {
		return context.addFilter(arg0, arg1);
	}

	@Override
	public Dynamic addFilter(String arg0, Class<? extends Filter> arg1) {
		return context.addFilter(arg0, arg1);
	}

	@Override
	public void addListener(String arg0) {
		context.addListener(arg0);
	}

	@Override
	public <T extends EventListener> void addListener(T arg0) {
		context.addListener(arg0);

	}

	@Override
	public void addListener(Class<? extends EventListener> arg0) {
		context.addListener(arg0);

	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0, String arg1) {
		return context.addServlet(arg0, arg1);
	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0, Servlet arg1) {
		return context.addServlet(arg0, arg1);
	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0, Class<? extends Servlet> arg1) {
		return context.addServlet(arg0, arg1);
	}

	@Override
	public <T extends Filter> T createFilter(Class<T> arg0) throws ServletException {
		return context.createFilter(arg0);
	}

	@Override
	public <T extends EventListener> T createListener(Class<T> arg0) throws ServletException {
		return context.createListener(arg0);
	}

	@Override
	public <T extends Servlet> T createServlet(Class<T> arg0) throws ServletException {
		return context.createServlet(arg0);
	}

	@Override
	public void declareRoles(String... arg0) {
		context.declareRoles(arg0);
	}

	@Override
	public ClassLoader getClassLoader() {
		return context.getClassLoader();
	}

	@Override
	public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
		return context.getDefaultSessionTrackingModes();
	}

	@Override
	public int getEffectiveMajorVersion() {
		return context.getEffectiveMajorVersion();
	}

	@Override
	public int getEffectiveMinorVersion() {
		return context.getEffectiveMinorVersion();
	}

	@Override
	public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
		return context.getEffectiveSessionTrackingModes();
	}

	@Override
	public FilterRegistration getFilterRegistration(String arg0) {
		return context.getFilterRegistration(arg0);
	}

	@Override
	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		return context.getFilterRegistrations();
	}

	@Override
	public JspConfigDescriptor getJspConfigDescriptor() {
		return context.getJspConfigDescriptor();
	}

	@Override
	public ServletRegistration getServletRegistration(String arg0) {
		return context.getServletRegistration(arg0);
	}

	@Override
	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		return context.getServletRegistrations();
	}

	@Override
	public SessionCookieConfig getSessionCookieConfig() {
		return context.getSessionCookieConfig();
	}

	@Override
	public boolean setInitParameter(String arg0, String arg1) {
		return context.setInitParameter(arg0, arg1);
	}

	@Override
	public void setSessionTrackingModes(Set<SessionTrackingMode> arg0) {
		context.setSessionTrackingModes(arg0);
	}
}
