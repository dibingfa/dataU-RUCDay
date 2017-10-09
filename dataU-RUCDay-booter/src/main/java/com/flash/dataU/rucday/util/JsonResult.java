package com.flash.dataU.rucday.util;

import com.alibaba.fastjson.JSON;
import java.io.Serializable;

/**
 * web请求响应包装类.
 *
 * @author flash (18811311416@sina.cn)
 * @version 0.0.1-SNAPSHOT
 * @since 2017年09月01日 10时01分
 */
public class JsonResult implements Serializable {

	private static final long serialVersionUID = -9215706706932134278L;
	private int error_code;
	private String error_msg;
	private Object data;

	public JsonResult() {
	}

	public JsonResult(int error_code, String error_msg, Object data) {
		this.error_code = error_code;
		this.error_msg = error_msg;
		this.data = data;
	}

	public static JsonResult buildSuccessResult(Object data) {
		return new JsonResult(ErrorCodeEnum.SUCCESS.getValue(), "", data);
	}

	public static JsonResult buildFailedResult(int error_code, String error_msg) {
		return new JsonResult(error_code, error_msg, "");
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}

	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
