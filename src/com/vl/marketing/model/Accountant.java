package com.vl.marketing.model;

public class Accountant extends User {

	public Accountant(String name, String username, String title, String email, String phone, String ext, String fax, String rank, int activated) {
		super(name, username, title, email, phone, ext, fax, rank, activated);
		privileges.replace("view all", true);
	}
	
}
