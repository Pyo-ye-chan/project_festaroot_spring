package com.study.app.domains.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.study.app.domains.activity.dto.UserActivityLogDTO;

@Service
public class UserActivityLogService {

    @Autowired
    private UserActivityLogDAO userActivityLogDAO;

    public int saveLog(UserActivityLogDTO log) {
        return userActivityLogDAO.insertLog(log);
    }

    public List<UserActivityLogDTO> getRecentLogs(String memberId) {
        return userActivityLogDAO.selectRecentLogs(memberId);
    }
}
