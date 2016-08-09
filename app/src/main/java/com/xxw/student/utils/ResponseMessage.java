package com.xxw.student.utils;

/** 
* @ClassName: ResponseMessage 
* @Description: 封装json的对象
* @author Peter Jia
* @date 2016年3月8日 下午11:04:59 
*  
*/
public class ResponseMessage{

	public int code;
	public String message;
	public Object object;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	@Override
	public String toString() {
		return "ResponseMessage [code=" + code + ", message=" + message + ", object=" + object + "]";
	}
	
}
