package cn.model.tool;

public class Test {
	public static String table_user_info=
			"create table user_info (" +
			"id Integer primary key auto_increment," +
			"uid varchar(32) not null," +
			"uname varchar(32) not null," +
			"upwd varchar(32) not null," +
			"urole varchar(32) not null," +
			"note varchar(500)," +
			"img varchar(500) ," +
			"phone varchar(32) not null," +
			"email varchar(32) not null" +
			")";
	//	书大类信息;
	public static String table_kind_book_info=
			"create table kind_book_info (" +
			"id integer primary key auto_increment," +
			"kid varchar(32) not null," +
			"kname varchar(32) not null," +
			"note varchar(500))";
	//	书小类信息;
	public static String table_item_book_info=
			"create table item_book_info (" +
			"id integer primary key auto_increment," +
			"iid varchar(32) not null," +
			"iname varchar(32) not null," +
			"note varchar(500)," +
			"author varchar(32) not null," +
			"press varchar(100) not null," +
			"ptime varchar(100) not null," +
			"count varchar(100) not null," +
			"kid varchar(32) not null," +
			"img varchar(100)" +
			")";
	//	书借阅信息;
	public static String table_borrow_info=
			"create table borrow_info (" +
			"id integer primary key auto_increment," +
			"bid varchar(32) not null," +
			"iid varchar(32) not null," +
			"iname varchar(32) not null," +
			"borrower varchar(32) not null," +
			"btime varchar(32) not null," +
			"deadline varchar(32) not null," +
			"state varchar(32) not null," +
			"outstate varchar(32) not null," +
			"instate varchar(32) not null," +
			"inimg varchar(1000) " +
			")";
	//	借书状态信息;
	public static String table_book_state_info=
			"create table book_state_info (" +
			"id integer primary key auto_increment," +
			"iid varchar(32) not null," +
			"count varchar(32) not null," +
			"ccount varchar(32) not null," +
			"ebids varchar(1000))";
	public static void main(String[] args) {
		MTDataBaseTool	baseTool=new MTDataBaseTool();
                 
		
		baseTool.doDBUpdate(table_kind_book_info);
		baseTool.doDBUpdate(table_item_book_info);
		baseTool.doDBUpdate(table_borrow_info);
		baseTool.doDBUpdate(table_book_state_info);
		
	}
}
