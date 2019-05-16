CREATE TABLE `CheckMoneyOrder` (
`id` int(11) NOT NULL AUTO_INCREMENT,
 `name` VARCHAR(255) NULL,
`totalPrice` INT NULL,
`state` INT NULL,
 `createTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
 PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

ALTER TABLE WeChatPayOrder ADD checkMoneyOrderId int;
ALTER TABLE CheckMoneyOrder ADD businessId int;
INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ('CheckMoneyOrder','maxMoney','20000',' 每天对账单的最大的金额(单位元)--微信最大金额为2W，故不应该大于2W.');
INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ('CheckMoneyOrder','minMoney','1',' 每天对账单的最小的金额(单位元)--微信最小金额为1元，故最小金额不能低于1元.');
INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ('CheckMoneyOrder','noConfirmState','0','对账单还未确认');
INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ('CheckMoneyOrder','haveConfirmState','1','对账单商家已经确认');

INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ('CheckMoneyOrder','havePayState','2','对账单商家已收到钱');

UPDATE SystemCode SET codeValue = ",WorkOrder,BusinessPackage,MemberLvlRecord,MemberPackageRecord,MemberLvl,Booking,BusinessPackageOrder,CheckMoneyOrder," WHERE codeName = ",merchant,operator,";