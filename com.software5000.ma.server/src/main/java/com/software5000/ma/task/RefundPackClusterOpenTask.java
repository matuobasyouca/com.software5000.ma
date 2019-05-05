package com.software5000.ma.task;

import com.software5000.base.Constant;
import com.software5000.ma.service.PackClusterBuyRecordService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * 成团失败 退款定时
 * Created by Jiang on 2017/07/26.
 */
@Controller
public class RefundPackClusterOpenTask {

    @Resource
    private PackClusterBuyRecordService packClusterBuyRecordService;

    @RequestMapping(value = "/open/packClusterOpen/refundPackClusterOpen")
    @Scheduled(cron = "0 */10 * * * *")
    public void refundPackClusterOpen() throws SQLException {
        //阻止本地执行
        if(Constant.maUrl.contains("localhost")) return;
        packClusterBuyRecordService.updateRefundPackClusterBuyRecord();
    }
}
