package cn.model.entity;

public class MUserinfo {
	private int id ;
	private String uid;
	private String uname;
	private String upwd ;
	private String urole;
	private String note;
	private String img;
	private String phone;
	private String email;

	//	含参构造函数;
	public MUserinfo(String uid, String uname, String upwd,
			String urole, String note, String img, String phone, String email) {
		this.uid = uid;
		this.uname = uname;
		this.upwd = upwd;
		this.urole = urole;
		this.note = note;
		this.img = img;
		this.phone = phone;
		this.email = email;
		
	}
	//	构造函数;
	public MUserinfo() {
	
	}

	//数据的属性的抓取;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUpwd() {
		return upwd;
	}
	public void setUpwd(String upwd) {
		this.upwd = upwd;
	}
	public String getUrole() {
		return urole;
	}
	public void setUrole(String urole) {
		this.urole = urole;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
