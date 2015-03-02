package com.lili.monitor.bolt;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.lili.monitor.jdbc.DBManager;
import com.lili.monitor.matcher.RecordMatcher;
import com.lili.monitor.utils.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.ArrayList;
import java.util.Map;


/**
 * Created by lili on 2015/3/2.
 *  数据过滤Bolt
 */
public class FilterBolt extends BaseBasicBolt {

	private static final long serialVersionUID = 1314953762070003754L;

	private static final Logger log = LoggerFactory.getLogger(FilterBolt.class);

	private ArrayList<String> columnNameList = new ArrayList<String>();
	private String seprator;
	private String tableName;
	private String filterConfig;
	private RecordMatcher recordMatcher;

	public FilterBolt(String tableName, String filterConfig) {
		this.tableName = tableName;
		this.filterConfig = filterConfig;
	}

	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context) {
		try {
			Connection conn = DBManager.getConnection();
			String selectSql = "SELECT * FROM " + tableName;
			PreparedStatement pstmt = conn.prepareStatement(selectSql);
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnName(i);
				columnNameList.add(columnName);
			}
			DBManager.closeConnection(conn);
		} catch (SQLException e) {
			log.error("获取元数据失败", e);
			throw new RuntimeException("获取元数据失败", e);
		}
		seprator = PropertyUtil.getProperty("seprator");
		recordMatcher = new RecordMatcher(filterConfig);
		recordMatcher.start();

	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		String data = input.getString(0);
		String[] lines = data.split("\n");
		for (String line : lines) {
			String[] fields = line.split(seprator);
			if (recordMatcher.match(columnNameList, fields)) {
				collector.emit(new Values(line));
			}
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}
}
