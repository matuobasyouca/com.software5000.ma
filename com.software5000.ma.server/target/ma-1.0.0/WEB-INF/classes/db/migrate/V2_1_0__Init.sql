ALTER TABLE ServiceItem ADD topState int;
ALTER TABLE WorkOrder ADD discountDeduct double;
ALTER TABLE WorkOrder ADD discountNum double;

UPDATE WeChatPayOrder SET checkMoneyFee=tradeFee;
update WeChatPayOrder,BusinessPackageOrder,BusinessPackage 
set WeChatPayOrder.showName=BusinessPackage.packageName,WeChatPayOrder.tradingTime=BusinessPackageOrder.updateTime
where WeChatPayOrder.packageOrderId IS NOT NULL
AND WeChatPayOrder.packageOrderId=BusinessPackageOrder.id
AND BusinessPackageOrder.businessPackageId=BusinessPackage.id;
update WeChatPayOrder,WorkOrder
set WeChatPayOrder.showName=WorkOrder.carNumber,WeChatPayOrder.tradingTime=WorkOrder.updateTime
where WeChatPayOrder.workOrderId=WorkOrder.id 
AND WeChatPayOrder.workOrderId IS NOT NULL;

DROP TABLE IF EXISTS `CooperCombo`;
CREATE TABLE `CooperCombo` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`comboName` VARCHAR(255) NULL,
`comboSalePrice` DOUBLE NULL,
`comboBackPrice` DOUBLE NULL,
`comboState` INT NULL,
`instructions` VARCHAR(255) NULL,
`businessId` INT NULL,
`couponUUID` VARCHAR(255) NULL,
`saleComboNum` INT NULL,
`validTerm` INT NULL,
`creatorId` INT NULL,
`createTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

DROP TABLE IF EXISTS `CooperComboOrder`;
CREATE TABLE `CooperComboOrder` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`orderNumber` VARCHAR(255) NULL,
`businessId` INT NULL,
`userId` INT NULL,
`carNumber` VARCHAR(255) NULL,
`mobile` VARCHAR(255) NULL,
`cooperComboId` INT NULL,
`state` INT NULL,
`quantity` INT NULL,
`totalAmount` DOUBLE NULL,
`backAmount` DOUBLE NULL,
`createTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
DROP TABLE IF EXISTS `Operator`;
CREATE TABLE `Operator` (
`id` int NOT NULL AUTO_INCREMENT ,
`userName` varchar(255) NULL ,
`password` varchar(255) NULL ,
`realName` varchar(255) NULL ,
`state` tinyint NULL ,
`createTime` timestamp NOT NULL DEFAULT '2016-01-01 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '2016-01-01 00:00:00',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
-- 2. 数据变更（增删改查数据库脚本）
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('Emkt', 'emktDomainName', 'http://192.168.31.103/', '营销平台主域名', '0');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('Emkt', 'maToken', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYSIsIm5iZiI6MTQ5MzI5NTQ3MCwiaXNzIjoiY29tLnpzY3AuZW1rdCIsImV4cCI6NDEwMjMyOTYwMCwiaWF0IjoxNDkzMjk1NDcwLCJqdGkiOiIyMTI1MDUzNy1kMDA4LTQ2ZDItYjEzNi02MjI3ODAwMmFiZjYifQ.46XeJsskiXZ2dHnMdp8lr87-IfihiUnkdL7p2CJjdF8', '联盟商家token', '0');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('StateCode', 'ZS012031', null, '请输入车牌号', '0');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('StateCode', 'ZS012034', null, '优惠券失效', '0');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('WeChatPayOrderType', 'comboOrderType', '4', '合作套餐', '0');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('ComboState', 'comboOnline', '1', '合作套餐上架', '1');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('ComboState', 'comboOffline', '2', '合作套餐下架', '2');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('OrderState', 'cancelOrder', '5', '已取消', '0');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('StateCode', 'ZS012032', null, '订单已取消！', '0');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('StateCode', 'ZS012033', null, '订单已支付！', '0');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('CouponSellState', 'couponCanSell', '1', '合作套餐还可以卖', '0');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('CouponSellState', 'couponNotSell','2', '合作套餐已经售完', '0');

Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('StateCode', 'ZS012035', null, '优惠券处理失败', '0');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('WeChatPayOrderType', 'couponOrderType', '3', null, '0');
Replace INTO `SystemCode` VALUES (null, 'SecuritySetting', 'zs.dfUrl', '/operator/workbench.html', null, '运营默认首页', '0', null, null, null, '2016-01-01 00:00:00', '2016-01-01 00:00:00');
Replace INTO `SystemCode` VALUES (null, 'SecuritySetting', 'zs.dLoginUrl', '/operator/login.html', null, '运营登录页', '0', null, null, null, '2016-01-01 00:00:00', '2016-01-01 00:00:00');

UPDATE `SystemCode` SET codeValue = 10, codeShowName = '最多添加10辆车！' WHERE codeType = 'SettingValue' AND codeName = 'carBrindMaxNum';

UPDATE `SystemCode` SET codeValue = 'http://pay.zhongshengchengpin.com/cmrd.jsp?a=f&r=' WHERE codeType = 'WeChatMCH' AND codeName = 'redirectUri';
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('SysConfig','jwtSharedKey', 'ZSCP@4009945888', null, '0');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('WeChatMCH','maWeChatPayUrl', 'ma.mmgoodcar.com/wechat/pay/jsapi', null, '0');
update SystemCode set codeValue='/merchant/login.html' where codeName='m.rdLoginUrl';
update MemberLvl set discount=10 where discount=0;
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('ServiceItemTopState', 'top', 1, '置顶', '0');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('ServiceItemTopState', 'noTop', 0, '不置顶', '0');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('ImagePath','imageDomianUrl', 'http://image.mmgoodcar.com', null, '0');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('ImagePath','imageSaveRootPath', '/home/zscpdev/zscp_image', null, '0');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('ImagePath','workOrderImg', '/ma/workOrderImg/', null, '0');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('StateCode', 'ZS012036', null, '图片添加失败', '0');
Replace INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('StateCode', 'ZS012037', null, '图片删除失败', '0');
DELETE FROM SystemCode WHERE CodeType = 'ImgUrl';
update SystemCode set codeValue='/merchant/index.html' where codeName='o.dfUrl';
