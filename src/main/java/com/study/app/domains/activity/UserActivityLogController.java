package com.study.app.domains.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.study.app.domains.activity.dto.UserActivityLogDTO;
import java.util.List;

@RestController
@RequestMapping("/activities")
public class UserActivityLogController {

    @Autowired
    private UserActivityLogService userActivityLogService;

    @PostMapping("/log")
    public String saveLog(@RequestBody UserActivityLogDTO logDTO, @RequestAttribute("id") String memberId) {
        logDTO.setMember_id(memberId);
        int result = userActivityLogService.saveLog(logDTO);
        return result > 0 ? "success" : "fail";
    }

    @GetMapping("/recent")
    public List<UserActivityLogDTO> getRecentLogs(@RequestAttribute("id") String memberId) {
        return userActivityLogService.getRecentLogs(memberId);
    }
}
