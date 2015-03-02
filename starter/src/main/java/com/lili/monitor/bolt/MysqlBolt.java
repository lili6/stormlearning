package com.lili.monitor.bolt;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.lili.monitor.jdbc.DBManager;
import com.lili.monitor.utils.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
/**
 * Created by lili on 2015/3/2.
 * 将数据写入到mysq的Bolt
 */
public class MysqlBolt extends BaseBasicBolt {

	private static final long serialVersionUID = 2747459780088405879L;

	private static final Logger log = LoggerFactory.getLogger(MysqlBolt.class);

	private String seprator;

	private String tableName;

	private Connection conn;

	private PreparedStatement pstmt;

	private String insertSql;

	public MysqlBolt(String tableName, String filterConfig) {
		this.tableName = tableName;
	}

	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context) {
		seprator = PropertyUtil.getProperty("seprator");
		try {
			conn = DBManager.getConnection();
			String selectSql = "SELECT * FROM " + tableName;
			insertSql = "INSERT INTO " + tableName;
			PreparedStatement pstmt = conn.prepareStatement(selectSql);
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			String fields = " (";
			String vals = " (";
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnName(i);
				if (i < columnCount) {
					fields += columnName + ", ";
					vals += "?, ";
				} else {
					fields += columnName + ")";
					vals += "?) ";
				}
			}
			insertSql += (fields + " VALUES " + vals);
		} catch (SQLException e) {
			log.error("获取元数据失败", e);
			throw new RuntimeException("获取元数据失败", e);
		}
	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		String line = input.getString(0);
		String[] fields = line.split(seprator);
		try {
			pstmt = conn.prepareStatement(insertSql);
			for (int i = 0; i < fields.length; i++) {
				pstmt.setString(i + 1, fields[i]);
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.error("数据插入失败", e);
			throw new RuntimeException("数据插入失败", e);
		}
		collector.emit(new Values(line));
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}

	public void cleanup() {
		DBManager.closeConnection(conn);
	}
}
