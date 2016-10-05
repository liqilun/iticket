package com.iticket.web.util;

import java.io.Writer;

import com.iticket.util.JsonUtils;

public class JsonDirectOut implements DirectOut{
	private Writer writer;
	private Object json;
	public JsonDirectOut(Object json){
		this.json = json;
	}
	@Override
	public void write(){
		JsonUtils.writeObjectToWriter(writer, json, false);
	}
	@Override
	public void setWriter(Writer writer) {
		this.writer = writer;
	}
}