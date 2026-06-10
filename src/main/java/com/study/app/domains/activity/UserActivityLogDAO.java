package com.study.app.domains.activity;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.study.app.domains.activity.dto.UserActivityLogDTO;

@Mapper
public interface UserActivityLogDAO {
    int insertLog(UserActivityLogDTO log);
    List<UserActivityLogDTO> selectRecentLogs(String member_id);
    int checkTodayAttendance(String member_id);
}
