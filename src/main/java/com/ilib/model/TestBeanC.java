package com.ilib.model;

import java.util.List;

public class TestBeanC {
private String name;
private Integer age;
private List<TestBeanD> subs;

public List<TestBeanD> getSubs() {
	return subs;
}
public void setSubs(List<TestBeanD> subs) {
	this.subs = subs;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public Integer getAge() {
	return age;
}
public void setAge(Integer age) {
	this.age = age;
}

}
