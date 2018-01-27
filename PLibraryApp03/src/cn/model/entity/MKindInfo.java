package cn.model.entity;

public class MKindInfo {
	private int id ;
	private String kid;
	private String kname;
	private String note;
	public MKindInfo(String kid, String kname, String note) {
		this.kid = kid;
		this.kname = kname;
		this.note = note;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKid() {
		return kid;
	}
	public void setKid(String kid) {
		this.kid = kid;
	}
	public String getKname() {
		return kname;
	}
	public void setKname(String kname) {
		this.kname = kname;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
