package com.lili.monitor.spout;

/**
 * Created by lili on 2015/3/2.
 */

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class StringScheme implements Scheme {

	private static final long serialVersionUID = -1641199638262927802L;

	public List<Object> deserialize(byte[] bytes) {
		try {
			return new Values(new String(bytes, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public Fields getOutputFields() {
		return new Fields("str");
	}
}