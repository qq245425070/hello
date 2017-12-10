package cn.gietv.mlive.modules.download.bean;

import java.io.Serializable;

public class M3U8Bean implements Serializable{
	private String name;
	private String nativePath;
	private String networkPath;
	private String status;
	private String image;
	private int progress;
	private int total;
	private String anchor;
	private boolean check;

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNativePath() {
		return nativePath;
	}
	public void setNativePath(String nativePath) {
		this.nativePath = nativePath;
	}
	public String getNetworkPath() {
		return networkPath;
	}
	public void setNetworkPath(String networkPath) {
		this.networkPath = networkPath;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "M3U8Bean [name=" + name + ", nativePath=" + nativePath + ", networkPath="
				+ networkPath + ", status=" + status + ", image="
				+ image + ",progress = "+progress+",anchor="+anchor+",total="+total+"]";
	}
}
