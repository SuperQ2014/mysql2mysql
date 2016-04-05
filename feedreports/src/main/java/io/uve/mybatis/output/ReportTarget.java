package io.uve.mybatis.output;

public class ReportTarget {
	
	private String date;
	private String platform;
	private String pr;
	
	private int requests;
	private int uv;
	private int imps;
	private int imp_uv;
	private int imp_groups;
	private int inventory;
	private long unread_status;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getPr() {
		return pr;
	}
	public void setPr(String pr) {
		this.pr = pr;
	}
	public int getRequests() {
		return requests;
	}
	public void setRequests(int requests) {
		this.requests = requests;
	}
	public int getUv() {
		return uv;
	}
	public void setUv(int uv) {
		this.uv = uv;
	}
	public int getImps() {
		return imps;
	}
	public void setImps(int imps) {
		this.imps = imps;
	}
	public int getImp_uv() {
		return imp_uv;
	}
	public void setImp_uv(int imp_uv) {
		this.imp_uv = imp_uv;
	}
	public int getImp_groups() {
		return imp_groups;
	}
	public void setImp_groups(int imp_groups) {
		this.imp_groups = imp_groups;
	}
	public int getInventory() {
		return inventory;
	}
	public void setInventory(int inventory) {
		this.inventory = inventory;
	}
	public long getUnread_status() {
		return unread_status;
	}
	public void setUnread_status(long unread_status) {
		this.unread_status = unread_status;
	}
}
