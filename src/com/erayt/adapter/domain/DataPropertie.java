package com.erayt.adapter.domain;

public class DataPropertie {

	private String field;
	private int fieldType;
	private int valueType;
	private String value;
	private String format;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public int getValueType() {
		return valueType;
	}
	public void setValueType(int valueType) {
		this.valueType = valueType;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public int getFieldType() {
		return fieldType;
	}
	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}
	
}
