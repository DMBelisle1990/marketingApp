package com.vl.marketing.model;

import java.util.LinkedHashMap;

public abstract class User {

	private String name;
	private String username;
	private String title;
	private String email;
	private String phone;
	private String ext;
	private String fax;
	private String rank;
	private int activated;
	
	// List of all functionality the user is permitted to access
	protected LinkedHashMap<String, Boolean> privileges = new LinkedHashMap<String, Boolean>();
	
	public User(String name, String username, String title, String email, String phone, String ext, String fax, String rank, int activated) {
		this.name = name;
		this.username = username;
		this.title = title;
		this.email = email;
		this.phone = phone;
		this.ext = ext;
		this.fax = fax;
		this.setRank(rank);
		this.activated = activated;
		
		privileges.put("submit", false);
		privileges.put("delete", false);
		privileges.put("csv", true);
		privileges.put("approver", false);
		privileges.put("view all", false);
		privileges.put("account manager", false);
		privileges.put("account authorizer", false);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public LinkedHashMap<String, Boolean> getPrivileges() {
		return privileges;
	}
	
	public void setPrivilege(String privelege, Boolean b) {
		privileges.replace(privelege, b);
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public int getActivated() {
		return activated;
	}

	public void setActivated(int activated) {
		this.activated = activated;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

}
