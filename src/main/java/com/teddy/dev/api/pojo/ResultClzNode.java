/**
 * TODO
 */
package com.teddy.dev.api.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 类节点
 * 
 * @author Teddy.D.Share 2018年2月6日
 */
public class ResultClzNode extends ResultNode<List<ResultNode<?>>> {

	public void add(ResultNode<?> subNode) {
		if (super.getV() == null) {
			super.setV(new ArrayList<>());
		}
		super.getV().add(subNode);
	}
}
