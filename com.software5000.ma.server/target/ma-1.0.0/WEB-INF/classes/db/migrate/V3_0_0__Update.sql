CREATE TABLE `ServiceItemExcelData` (
`id` int(11) NOT NULL AUTO_INCREMENT,
 `serviceItemName` VARCHAR(255) NULL,
`salePrice` DOUBLE NULL,
`itemType` VARCHAR(255) NULL,
`sort` INT NULL,
`remark` VARCHAR(255) NULL,
`businessId` INT NULL,
`successFlag` INT NULL,
 `createTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
 PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `UserExcelData` (
`id` int(11) NOT NULL AUTO_INCREMENT,
 `realName` VARCHAR(255) NULL,
`carNumber` VARCHAR(255) NULL,
`mobile` VARCHAR(255) NULL,
`packageName` VARCHAR(255) NULL,
`validDate` VARCHAR(255) NULL,
`serviceItem1` VARCHAR(255) NULL,
`restTime1` VARCHAR(255) NULL,
`serviceItem2` VARCHAR(255) NULL,
`restTime2` VARCHAR(255) NULL,
`serviceItem3` VARCHAR(255) NULL,
`restTime3` VARCHAR(255) NULL,
`successFlag` INT NULL,
`remark` VARCHAR(255) NULL,
`businessId` INT NULL,
 `createTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
 PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `Finance` (
`id` int(11) NOT NULL AUTO_INCREMENT,
 `businessId` INT NULL,
`financeType` INT NULL,
`explainInfo` VARCHAR(255) NULL,
`money` INT NULL,
`payType` INT NULL,
`orderId` INT NULL,
`payOrderId` INT NULL,
`userId` INT NULL,
`orderNo` VARCHAR(255) NULL,
`inOrOutCome` INT NULL,
 `createTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
 PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

 CREATE TABLE `BusinessCheckMoney` (
`id` int(11) NOT NULL AUTO_INCREMENT,
 `businessId` INT NULL,
`canCheckMoney` INT NULL,
`haveCheckMoney` INT NULL,
 `createTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
 PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `WorkItemUserRecord` (
`id`  int NOT NULL ,
`workOrderId`  int NULL ,
`memberItemUseRecordId`  int NULL ,
`times`  int NULL ,
`createTime`  timestamp NOT NULL DEFAULT '2016-1-1 00:00:00' ,
`updateTime`  timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
PRIMARY KEY (`id`)
);

ALTER TABLE `Business`
ADD COLUMN `closingDateNum`  int NULL;

ALTER table BusinessPackageOrder add column businessUserId int;
ALTER table BusinessPackageOrder add column payType int;

ALTER TABLE `WorkItemUserRecord`
MODIFY COLUMN `id`  int(11) NOT NULL AUTO_INCREMENT FIRST ;

ALTER TABLE `ServiceItem`
ADD COLUMN `workBonus`  double NULL AFTER `topState`,
ADD COLUMN `saleBonus`  double NULL AFTER `workBonus`,
ADD COLUMN `itemType`  int NULL AFTER `saleBonus`;

ALTER TABLE `BusinessUser`
ADD COLUMN `itemTypes`  varchar(255) NULL AFTER `businessId`,
ADD COLUMN `state`  int(11) NULL AFTER `itemTypes`,
ADD COLUMN `salary`  double NULL AFTER `state`;

ALTER TABLE `BusinessUser`
CHANGE COLUMN `realName` `userName`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
ADD COLUMN `realName`  varchar(255) NULL;

ALTER TABLE `BusinessUser`
ADD COLUMN `bossType`  int NULL;

ALTER TABLE `WeChatPayOrder`
ADD COLUMN `realTimeMoneyFee`  int(11) NULL;

alter table MemberLvlRecord add(totalTimes int(11)default null);

ALTER TABLE `ServiceItem`
ADD COLUMN `sort`  int NULL;

ALTER TABLE MemberPackageRecord
ADD COLUMN packageOrderId int NULL;

ALTER TABLE WorkOrder ADD payType int DEFAULT NULL;
ALTER TABLE WorkOrder ADD payTime timestamp NULL ;
ALTER TABLE WorkOrder ADD couponName VARCHAR(255) DEFAULT NULL ;
ALTER TABLE WorkOrder ADD comment VARCHAR(1024) DEFAULT NULL;

-- 工单明细
ALTER TABLE WorkOrderDetail ADD serviceItemName VARCHAR(255) DEFAULT NULL ;
ALTER TABLE WorkOrderDetail ADD workerId int DEFAULT NULL ;
ALTER TABLE WorkOrderDetail ADD salerId int DEFAULT NULL ;

-- 商家默认地区
ALTER TABLE Business ADD defaultCar VARCHAR(255) DEFAULT NULL ;

-- 会员等级
ALTER TABLE MemberLvl DROP COLUMN singleConsume;
ALTER TABLE MemberLvl DROP COLUMN description;
ALTER TABLE MemberLvl DROP COLUMN businessPackageId;
ALTER TABLE MemberLvl ADD state int DEFAULT NULL ;

ALTER TABLE `PayOrder`
ADD COLUMN `payCheckUrl`  varchar(255) NULL;

ALTER TABLE `ServiceItem`
ADD INDEX `businessId` (`businessId`) USING BTREE ;

ALTER TABLE `WorkOrder`
ADD INDEX `state` (`state`) USING BTREE ,
ADD INDEX `payType` (`payType`) USING BTREE ;

ALTER TABLE `WorkOrder`
ADD INDEX `businessId` (`businessId`) USING BTREE ;

ALTER TABLE `BusinessUser`
ADD INDEX `businessId` (`businessId`) USING BTREE ;

UPDATE SystemCode SET codeValue='http://ma.mmgoodcar.com/wap/customer/open/cashier.html' where codeName='cashierDeskUrl' and codeType='CashierDesk';
UPDATE MemberLvl SET state = 1;
UPDATE BusinessUser SET state = 1;
UPDATE BusinessUser SET realName = userName;
UPDATE SystemCode SET codeValue=',WorkOrder,BusinessPackage,MemberLvlRecord,MemberPackageRecord,MemberLvl,Booking,BusinessPackageOrder,CheckMoneyOrder,WeChatPayOrder,BusinessUser,ServiceItem,Finance,BusinessCheckMoney,' where codeType='PermissionRule' AND codeName=',merchant,operator,';
UPDATE Business SET closingDateNum =28;
UPDATE Business SET businessEnable=2 WHERE businessEnable=1;
UPDATE Business SET businessEnable=1 WHERE businessEnable=0;
UPDATE BusinessUser SET bossType =1 where mercharType='merchant';

INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName,codeFiter,codeDesc) VALUES ('PermissionRule',',operator,',',ServiceItem,','操作员对服务项的权限控制',1,"{me.a}.businessId={bid} AND itemType IN (SELECT substring_index(substring_index(t.itemTypes,',', b.help_topic_id + 1), ',', -1) as itemType FROM BusinessUser t join mysql.help_topic b ON b.help_topic_id <  (LENGTH(t.itemTypes) - LENGTH(REPLACE(t.itemTypes, ',', '')) + 1)WHERE t.id= {uid})");


-- 施工图片默认路径
INSERT INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('ImagePath', 'defaultPath', '/ma/default/', '默认图片路径', '1');

-- 工单付款方式处理（根据在WebChatPayOrder是否有值来判断付款方式）
UPDATE WorkOrder SET payType = 1 WHERE state = 4;
UPDATE WorkOrder wo SET payType = 2 WHERE state = 4 AND (SELECT COUNT(wc.id) FROM WeChatPayOrder wc WHERE wc.workOrderId = wo.id and wc.status = 2) > 0;
-- 更新工单服务项目中的名称
UPDATE WorkOrderDetail detail SET detail.serviceItemName = (SELECT item.itemName FROM ServiceItem item WHERE item.id = detail.serviceItemId) WHERE detail.serviceItemId IS NOT NULL;
-- 更新未结算状态的工单的完工时间为更新时间
UPDATE WorkOrder SET commitTime = updateTime WHERE state = 3;
-- 更新已结算状态的工单的完工时间、结算时间为更新时间
UPDATE WorkOrder SET commitTime = updateTime, payTime = updateTime WHERE state = 4;
-- 更新排队状态的工单为未完工
UPDATE WorkOrder SET state = 1 WHERE state = 2;

-- PermissionRule
UPDATE SystemCode SET codeDesc = '{me.a}.businessId={bid}' WHERE codeName = ',merchant,operator,';

INSERT INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter, codeDesc) VALUES ('PermissionRule', ',operator,WorkOrder,', ',WorkOrder,', '操作员对工单的权限控制', '1', '((select count(wod.id) from WorkOrderDetail wod where wod.workOrderId = {me.a}.id and wod.workerId = {uid}) > 0)');

INSERT INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter, codeDesc) VALUES ('PermissionRule', ',operator,WorkOrderDetail,', ',WorkOrderDetail,', '操作员对工单详情的权限控制', '1', '{me.a}.workerId={uid}');



-- 微信消息提醒
INSERT INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('WxTemplateId', 'settleOrder', 'l7BbcYD9TznCX4J5evlJw2Kd8r20fZPBkt6FaO-9rKI', '工单结算提醒模板ID', '9');

INSERT INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('WxTemplateId', 'consumePackage', 'aRIuZMZQfQsdmRbhwso93kw9cK_X-NmtL80dNkQpXpo', '套餐消费提醒模板ID', '9');

INSERT INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('WxTemplateId', 'expirePackage', '59SMX2XRMGm2Bt8YOq0le2eqRqixqfANhEZEvxdIUvc', '套餐到期提醒模板ID', '9');

-- 工单字段修改
ALTER TABLE WorkOrder MODIFY COLUMN couponUuid VARCHAR(1024);
ALTER TABLE WorkOrder MODIFY COLUMN couponName VARCHAR(1024);
ALTER TABLE WorkOrder ADD couponEveryDeduct VARCHAR(1024) DEFAULT NULL AFTER couponName;

UPDATE SystemCode SET codeValue = 'http://ma.mmgoodcar.com/wap/customer/open/lottery.html' where codeName = 'lotteryUrl' and codeType='CashierDesk';

UPDATE SystemCode SET codeValue='http://ma.mmgoodcar.com/open/bank/payOrderNotify' where codeName='payOrderNotifyUrl' and codeType='CashierDesk';

ALTER TABLE `PayOrder`
ADD COLUMN `wxOpenId`  varchar(255) NULL AFTER `payCheckUrl`;

update SystemCode set codeValue=REPLACE(codeValue,'http://ma.mmgoodcar.com','');
update SystemCode set codeValue=REPLACE(codeValue,'ma.mmgoodcar.com','');

ALTER TABLE `Car`
ADD INDEX `userId` (`userId`) USING BTREE ;
ALTER TABLE `MemberLvlRecord`
ADD INDEX `userId` (`userId`) USING BTREE ;

ALTER TABLE `WorkOrderDetail`
ADD INDEX `workOrderId` (`workOrderId`) USING BTREE ;


ALTER TABLE `WeChatPayOrder`
ADD INDEX `workOrderId` (`workOrderId`) USING BTREE ;


UPDATE Business,BusinessUser SET Business.wxOpenId=BusinessUser.wxOpenId WHERE Business.id=BusinessUser.businessId AND BusinessUser.bossType=1;

ALTER TABLE `Finance`
ADD COLUMN `userPayMoney`  int(11) NULL AFTER `orderNo`;

update SystemCode set codeValue='/web/merchant/home/index.html' where codeName='m.dfUrl';
update SystemCode set codeValue='/wap/merchant/home/index.html' where codeName='o.dfUrl';
update SystemCode set codeValue='/wap/operator/home/workbench.html' where codeName='zs.dfUrl';
update SystemCode set codeValue='/wap/operator/open/login.html' where codeName='zs.dLoginUrl';