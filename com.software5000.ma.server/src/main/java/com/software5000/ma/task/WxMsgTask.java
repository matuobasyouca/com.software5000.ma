package com.software5000.ma.task;

import com.software5000.ma.dto.WxMsgDto;
import com.software5000.base.mybatis.plugins.PermissionHelper;
import com.software5000.ma.service.MemberPackageRecordService;
import com.software5000.util.WxMsgUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Jiang on 2017/07/26.
 */
@Controller
public class WxMsgTask {

    @Resource
    private MemberPackageRecordService memberPackageRecordService;

    @RequestMapping(value = "/home/businessPackage/sendMsgForExpirePackage")
    @Scheduled(cron = "0 0 9 * * *")
    public void sendMsgForExpirePackage() throws SQLException {
        //获取需要提醒的套餐信息
        PermissionHelper.ignorePermissionThisTime();
        List<WxMsgDto> dtoList = memberPackageRecordService.selectExpirePackageMsg();
        if(dtoList != null && dtoList.size() > 0) {
            dtoList.forEach(dto -> {
                WxMsgUtil.sendMsgForExpirePackage(dto.getOpenId(), dto);
            });
        }
    }
}
