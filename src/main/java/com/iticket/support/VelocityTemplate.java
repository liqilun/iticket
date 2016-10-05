package com.iticket.support;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import com.iticket.Config;

public class VelocityTemplate {
	private VelocityEngine velocityEngine;
	private Config config;
	public VelocityTemplate(){
	}
	public String parseTemplate(String template, Map model){
		model.putAll(Config.getPageTools());
		Context context = new VelocityContext(model);
		Writer writer = new StringWriter();
		try {
			velocityEngine.mergeTemplate(template, "UTF-8", context, writer);
		} catch (Exception e) {
		}
		return writer.toString();
	}
	public void parseTemplate(String template, Map model, Writer writer){
		model.putAll(Config.getPageTools());
		Context context = new VelocityContext(model);
		try {
			velocityEngine.mergeTemplate(template, "UTF-8", context, writer);
		} catch (Exception e) {
		}
	}
	public void parseTemplate(String template, Map model, OutputStream os){
		model.putAll(Config.getPageTools());
		Context context = new VelocityContext(model);
		Writer writer = new OutputStreamWriter(os);
		try {
			velocityEngine.mergeTemplate(template, "UTF-8", context, writer);
		} catch (Exception e) {
		}
	}
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	public Config getConfig() {
		return config;
	}
	public void setConfig(Config config) {
		this.config = config;
	}
}
