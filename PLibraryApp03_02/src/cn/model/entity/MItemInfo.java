package cn.model.entity;

public class MItemInfo {
	private String iid;
	private String iname;
	private String note;
	private String author;
	private String press;
	private String ptime;
	private String count;
	private String kid;
	private String img;
	public MItemInfo(String iid, String iname, String note, String author,
			String press, String ptime, String count, String kid, String img) {
		super();
		this.iid = iid;
		this.iname = iname;
		this.note = note;
		this.author = author;
		this.press = press;
		this.ptime = ptime;
		this.count = count;
		this.kid = kid;
		this.img = img;
	}
	public String getIid() {
		return iid;
	}
	public void setIid(String iid) {
		this.iid = iid;
	}
	public String getIname() {
		return iname;
	}
	public void setIname(String iname) {
		this.iname = iname;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPress() {
		return press;
	}
	public void setPress(String press) {
		this.press = press;
	}
	public String getPtime() {
		return ptime;
	}
	public void setPtime(String ptime) {
		this.ptime = ptime;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getKid() {
		return kid;
	}
	public void setKid(String kid) {
		this.kid = kid;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
}
