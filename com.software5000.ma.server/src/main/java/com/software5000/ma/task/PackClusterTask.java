package com.software5000.ma.task;

import com.software5000.base.Constant;
import com.software5000.ma.service.PackClusterService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 成团失败 退款定时
 * Created by Jiang on 2017/07/26.
 */
@Controller
public class PackClusterTask {

    @Resource
    private PackClusterService packClusterService;

    @RequestMapping(value = "/open/packCluster/updatePackClusterState")
    @Scheduled(cron = "0 2 0 * * *")
    public void updatePackClusterState() throws SQLException {
        Map param =new HashMap<>();
        param.put("state", Constant.PackageState.UNSHELVE.codeName);
        packClusterService.updatePackClusterState(param);
    }
}
