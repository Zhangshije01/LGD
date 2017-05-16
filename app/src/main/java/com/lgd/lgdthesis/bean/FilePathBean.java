package com.lgd.lgdthesis.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class FilePathBean extends BmobObject{
	private String name;
	private BmobFile file;
	private String fileUrl;
	private String size;
	private String path;
	private String time;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BmobFile getFile() {
		return file;
	}
	public void setFile(BmobFile file) {
		this.file = file;
	}
	
	public FilePathBean() {
		super();
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public FilePathBean(String name, BmobFile file, String fileUrl,
			String size, String path, String time) {
		super();
		this.name = name;
		this.file = file;
		this.fileUrl = fileUrl;
		this.size = size;
		this.path = path;
		this.time = time;
	}
	@Override
	public String toString() {
		return "FilePathBean [name=" + name + ", file=" + file + ", fileUrl="
				+ fileUrl + ", size=" + size + ", path=" + path + ", time="
				+ time + "]"; 
	}
	
	

}
