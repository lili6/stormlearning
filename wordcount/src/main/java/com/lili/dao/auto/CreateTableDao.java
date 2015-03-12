package com.lili.dao.auto;

import com.lili.dao.annotation.AutoDao;
import org.apache.ibatis.annotations.Param;

/**
 * Created by liguofang on 2014/10/30.
 */
@AutoDao
public interface CreateTableDao {

    public void createTable(@Param("tablename") String tablename);
}
