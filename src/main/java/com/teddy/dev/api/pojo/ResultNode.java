/**
 * TODO
 */
package com.teddy.dev.api.pojo;

/**
 * 返回体节点
 * 
 * @author Teddy.D.Share 2018年2月1日
 */
public class ResultNode<T> {

	private String k; // key
	private T v; // value
	private String desc; // desc

	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public T getV() {
		return v;
	}

	public void setV(T v) {
		this.v = v;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "ResultNode [k=" + k + ", v=" + v + ", desc=" + desc + "]";
	}
}
