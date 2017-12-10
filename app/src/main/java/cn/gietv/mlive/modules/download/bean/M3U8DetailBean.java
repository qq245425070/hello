package cn.gietv.mlive.modules.download.bean;

import java.io.Serializable;
public class M3U8DetailBean implements Serializable{
	private long id;
	private String url;
	private String nativePath;
	private String status;
	private String parentName;
	private String fileName;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getNativePath() {
		return nativePath;
	}
	
	public void setNativePath(String nativePath) {
		this.nativePath = nativePath;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "M3U8DetailBean [id=" + id + ", url=" + url + ", nativePath="
				+ nativePath + ", status=" + status + ", parent_name="
				+ parentName + "]";
	}
	
}
