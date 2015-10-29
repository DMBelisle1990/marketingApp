package com.vl.marketing.model;

public class Admin extends User {

	public Admin(String name, String username, String title, String email, String phone, String ext, String fax, String rank, int activated) {
		super(name, username, title, email, phone, ext, fax, rank, activated);
		privileges.replace("submit", true);
		privileges.replace("delete", true);
		privileges.replace("approver", true);
		privileges.replace("view all", true);
		privileges.replace("account manager", true);
		privileges.replace("account authorizer", true);
	}
	
}
