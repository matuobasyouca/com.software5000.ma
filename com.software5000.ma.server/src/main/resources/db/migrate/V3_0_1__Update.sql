-- 链接
INSERT INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('CashierDesk', 'shareRedPacketUrl', '/wap/customer/open/pay_success.html', '分享红包链接', '1');

-- 红包卡券
INSERT INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('CashierDesk', 'shareRedPacketUuid', '', '分享红包的uuid', '9');

-- 工单详情添加项目使用卡券信息
ALTER TABLE WorkOrderDetail ADD couponUuid varchar(1024) DEFAULT NULL AFTER discountNumber;
ALTER TABLE WorkOrderDetail ADD couponName varchar(256) DEFAULT NULL AFTER couponUuid;
ALTER TABLE WorkOrderDetail ADD couponDeduct double DEFAULT NULL AFTER couponName;
-- 工单详情添加项目总价
ALTER TABLE WorkOrderDetail ADD totalPrice double DEFAULT NULL AFTER salePrice;

-- 更新项目信息
UPDATE WorkOrderDetail SET discountPrice = null, discountNumber = null;
UPDATE WorkOrderDetail SET totalPrice = (salePrice * (itemsTimes - IFNULL(usePackageTimes, 0)));


-- 郑华裕
ALTER TABLE `MemberLvlRecord` ADD COLUMN `remarks` varchar(255) NULL;

-- 吴瑾
CREATE TABLE `PackCluster` (
`id` int(11) NOT NULL AUTO_INCREMENT,
 `clusterName` VARCHAR(255) NULL,
`shareImgPath` VARCHAR(255) NULL,
`beginDay` TIMESTAMP NULL,
`endDay` TIMESTAMP NULL,
`businessId` INT NULL,
`salePrice` DOUBLE NULL,
`subsidyPrice` DOUBLE NULL,
`clusterNum` INT NULL,
`clusterHour` INT NULL,
`description` VARCHAR(255) NULL,
`state` INT NULL,
`packId` INT NULL,
 `createTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
 PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `PackClusterBuyRecord` (
`id` int(11) NOT NULL AUTO_INCREMENT,
 `openRecordId` INT NULL,
`orderNo` VARCHAR(255) NULL,
`packClusterId` INT NULL,
`wxOpenId` VARCHAR(255) NULL,
`mobile` VARCHAR(255) NULL,
`carNumber` VARCHAR(255) NULL,
`payTime` VARCHAR(255) NULL,
`payMoney` VARCHAR(255) NULL,
`payState` INT NULL,
 `createTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
 PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;


CREATE TABLE `PackClusterImg` (
`id` int(11) NOT NULL AUTO_INCREMENT,
 `packClusterId` INT NULL,
`imgPath` VARCHAR(255) NULL,
`imgSort` INT NULL,
 `createTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
 PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;



CREATE TABLE `PackClusterOpenRecord` (
`id` int(11) NOT NULL AUTO_INCREMENT,
 `packClusterId` INT NULL,
`packClusterNo` INT NULL,
`colonelOpenId` VARCHAR(255) NULL,
`endDay` TIMESTAMP NULL,
`state` INT NULL,
 `createTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
 PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;


 CREATE TABLE `RefundPackCluster` (
`id` int(11) NOT NULL AUTO_INCREMENT,
 `orderNo` VARCHAR(255) NULL,
`refundOrderNo` VARCHAR(255) NULL,
`refundFee` INT NULL,
`totalFee` INT NULL,
`refundId` VARCHAR(255) NULL,
`settlementTotalFee` INT NULL,
`transactionId` VARCHAR(255) NULL,
 `createTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
 PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

ALTER TABLE `SystemCode`
MODIFY COLUMN `codeValue`  varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代码值' AFTER `codeName`;
ALTER TABLE `PackClusterOpenRecord`
MODIFY COLUMN `packClusterNo`  varchar(255) NULL DEFAULT NULL AFTER `packClusterId`;
ALTER TABLE `PackClusterBuyRecord`
MODIFY COLUMN `payMoney`  int(11) NULL DEFAULT NULL AFTER `payTime`;
ALTER TABLE `PackClusterBuyRecord`
ADD COLUMN `userId`  int(11) NULL AFTER `payMoney`;
ALTER TABLE `PayOrder`
ADD COLUMN `businessId`  int(11) NULL AFTER `prepayId`;
ALTER TABLE `PackClusterBuyRecord`
MODIFY COLUMN `payTime`  timestamp NULL DEFAULT '2016-01-01 00:00:00' AFTER `carNumber`;
ALTER TABLE `PackClusterBuyRecord`
ADD INDEX `openRecordId` (`openRecordId`) USING BTREE ;

-- 榜哥
insert into SystemCode(codeType,codeName,codeValue) values('ImagePath','packClusterImg','/ma/packClusterImg/');

ALTER TABLE `PackCluster` ADD COLUMN `canEdit`  tinyint NULL;

-- 会变的
INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ('SubscribeText','packClusterContent',"欢迎您加入诚品好车网-社区车生活\n <a href='%s/wap/customer/open/group/activity.html?id=%s&openRecordId=%s&showShare=1'>点击邀请好友拼单</a>","拼团后扫码关注的回复内容");
INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ('SubscribeText','searchContent',"感谢您关注诚品好车网\n谢谢您参与父亲节活动，下期活动敬请期待！\n\n“诚”先生教你玩转公众号：\n1.热门车型：<a href='http://www.mmgoodcar.com/open/emkt/activity/618ershouchehuodon/index.html?weixinzidonhuifu'>4万公里宝马3系6折开抢 首付仅需5.3万</a>。火热开枪\n2.我要买车：<a href='http://www.mmgoodcar.com/weixin/buy.html'>15天帮买</a>，1年2万公里质保\n3.我要卖车：<a href='http://www.mmgoodcar.com/weixin/sale.html'>15天帮卖</a>，高效便捷\n4.更多服务：我要激活，<a href='http://www.mmgoodcar.com/weixin/shiTi.html'>实体店导航</a>",'拼团后扫码关注的回复内容');
INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ('SubscribeText','crm','CRM正式地址：\nPC端   http://crm.mmgoodcar.com/web/open/login.html \n微信端    http://crm.mmgoodcar.com/wap/open/login.html', 'crm自动回复');
INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ('NormalSetting','packNoteUrl','/wap/customer/open/scan.html?type=2&recodeType=1','参团后的关注页');
INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ('NormalSetting','packDetailUrl','/wap/customer/open/group/activity.html?id=%s','参团后的详情页');

ALTER TABLE `PayOrder` MODIFY COLUMN `orderDetail` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `id`;

