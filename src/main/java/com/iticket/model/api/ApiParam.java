package com.iticket.model.api;

import java.io.Serializable;

import com.iticket.model.BaseObject;

public class ApiParam extends BaseObject{
	private static final long serialVersionUID = -4624844332508903597L;
	private Long id;
	//字段
	private String field;
	//字段名称
	private String fieldName;
	//方法Id
	private Long methodId;
	//描述
	private String description;
	//类型(in,out)
	private String type;	
	//字段类型
	private String fieldType;	
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public ApiParam(){
		
	}
	@Override
	public Serializable realId() {
		return id;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Long getMethodId() {
		return methodId;
	}
	public void setMethodId(Long methodId) {
		this.methodId = methodId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
