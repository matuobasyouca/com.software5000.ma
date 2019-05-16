

ALTER TABLE WeChatPayOrder ADD businessId int ;
ALTER TABLE Business ADD wxOpenId varchar(255) ;
INSERT INTO SystemCode (codeType,codeName,codeValue)VALUES ('WeChatMCH','target4','formalMa4');
ALTER TABLE WeChatPayOrder ADD confirmState int DEFAULT 0;
INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ('WeChatPayOrderState','noConfirm','0','未确认');
INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ('WeChatPayOrderState','settlement','1','已结算');
INSERT INTO SystemCode (codeType,codeName,codeShowName)VALUES ('StateCode','ZS012026','单笔核销结算的金额必须大于1元');
INSERT INTO SystemCode (codeType,codeName,codeShowName)VALUES ('StateCode','ZS012027','单笔核销结算的金额不得超过2万元，如有疑问请联系客服处理');
INSERT INTO SystemCode (codeType,codeName,codeShowName)VALUES ('StateCode','ZS012028','商家核销结算失败');
ALTER TABLE WeChatPayOrder ADD orderType int ;
INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ('WeChatPayOrderType','workOrderType','0','工单');
INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ('WeChatPayOrderType','packageOrderType','1','套餐');
INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ('WeChatPayOrderType','settlementOrderType','2','核销结算的订单');
INSERT INTO SystemCode (codeType,codeName,codeShowName)VALUES ('StateCode','ZS012029','商家未绑定微信，无法进行核销');
INSERT INTO SystemCode (codeType,codeName,codeShowName)VALUES ('StateCode','ZS012030','金额不一致，请联系客服');