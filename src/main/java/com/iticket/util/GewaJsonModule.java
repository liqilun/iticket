package com.iticket.util;

import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.PropertyNamingStrategyBase;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class GewaJsonModule extends SimpleModule{
	private static final long serialVersionUID = 4093223126017721944L;
	public static GewaJsonModule GEWA_MODULE = new GewaJsonModule();
	private GewaJsonModule(){
		addDeserializer(Timestamp.class, GewaDateSerializers.timestampDeserializer);
		addDeserializer(Date.class, GewaDateSerializers.dateDeserializer);
		addDeserializer(java.sql.Date.class, GewaDateSerializers.sqlDateDeserializer);
		addSerializer(Timestamp.class, new GewaDateSerializer());
		addSerializer(Date.class, new GewaDateSerializer());
		addSerializer(java.sql.Date.class, new GewaDateSerializer());
	}
	public static class UpperCasePropertyNamingStrategy extends PropertyNamingStrategyBase {
		
		private static final long serialVersionUID = -6510034237130928673L;
		@Override
		/**
		 * this method call back by PropertyNamingStrategy
		 */
		public String translate(String propertyName) {
			if ("objectName".equals(propertyName)) {
				return propertyName;
			}
			String name = propertyName.replaceAll("^\\w", propertyName.toUpperCase().substring(0, 1));
			
			return name;
		}

	}
}
