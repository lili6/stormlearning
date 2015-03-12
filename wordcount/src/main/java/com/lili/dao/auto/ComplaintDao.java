package com.lili.dao.auto;

import com.lili.dao.annotation.AutoDao;
import com.lili.dao.model.DayReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liguofang on 2014/10/30.
 */
@AutoDao
public interface ComplaintDao {

  /*  public void insertComplaint(@Param("complaint") Complaint complaint);

    public int updateComplaint(@Param("complaint") Complaint complaint);

    public Complaint queryComplaintByAid(@Param("complaint") Complaint complaint);

    public List<Complaint> queryComplaint(@Param("complaint") Complaint complaint) ;
    //原生sql的查询
    public List<Complaint> queryComplaintBySQL(@Param("sqlstr") String sqlstr) ;*/

    //查询统计消息
    public List<DayReport> queryReport(@Param("dayReport") DayReport dayReport) ;

}
