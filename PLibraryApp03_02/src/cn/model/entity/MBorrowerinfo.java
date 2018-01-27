package cn.model.entity;

public class MBorrowerinfo {
	private int id ;
	private String bid;
	private String iid;
	private String iname;
	private String borrower;
	private String btime;
	private String deadline;
	private String state;
	private String outstate;
	private String instate;
	private String inimg;
	public MBorrowerinfo(String bid, String iid, String iname, String borrower,
			String btime, String deadline, String state, String outstate,
			String instate, String inimg) {
		super();
		this.bid = bid;
		this.iid = iid;
		this.iname = iname;
		this.borrower = borrower;
		this.btime = btime;
		this.deadline = deadline;
		this.state = state;
		this.outstate = outstate;
		this.instate = instate;
		this.inimg = inimg;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
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
	public String getBorrower() {
		return borrower;
	}
	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}
	public String getBtime() {
		return btime;
	}
	public void setBtime(String btime) {
		this.btime = btime;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOutstate() {
		return outstate;
	}
	public void setOutstate(String outstate) {
		this.outstate = outstate;
	}
	public String getInstate() {
		return instate;
	}
	public void setInstate(String instate) {
		this.instate = instate;
	}
	public String getInimg() {
		return inimg;
	}
	public void setInimg(String inimg) {
		this.inimg = inimg;
	}
	
}
