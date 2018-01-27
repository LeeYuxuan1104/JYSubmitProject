package com.model.tool;

public class Test {
	public static void main(String[] args) {
		MTDataBaseTool baseTool=new MTDataBaseTool();
		String sql="create table user_info (uid varchar(32) primary key not null,uname varchar(32) not null,upwd varchar(32) not null,uright varchar(32) not null)";
		baseTool.doDBUpdate(sql);
		sql="create table book_info (id integer primary key auto_increment,bid varchar(32) not null,bname varchar(32) not null,author varchar(32) not null,press varchar(500) not null,state varchar(32) not null,number varchar(32) not null)";
		baseTool.doDBUpdate(sql);
	}
}
