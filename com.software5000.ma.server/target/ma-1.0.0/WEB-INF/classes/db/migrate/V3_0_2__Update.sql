-- B
-- 工单-余额扣减
ALTER TABLE WorkOrder ADD balanceDeduct double DEFAULT NULL AFTER discountNum;

-- 工单项目-折扣类型
ALTER TABLE WorkOrderDetail ADD discountType int(11) DEFAULT NULL AFTER discountPrice;

-- 建雄
ALTER TABLE MemberLvl ADD (rechargeMoney DOUBLE NULL);

CREATE TABLE ItemAndMemberLvl (
	id INT (11) NOT NULL auto_increment,
	createTime TIMESTAMP NOT NULL DEFAULT '2016-1-1 00:00:00',
	updateTime TIMESTAMP NOT NULL DEFAULT '2016-1-1 00:00:00',
	memberLvlId INT NULL,
	itemType INT NULL,
	serviceItemId INT NULL,
	discountType INT NULL,
	discountNumber DOUBLE NULL,
	PRIMARY KEY (id)
) ENGINE = INNODB DEFAULT charset = utf8 COLLATE = utf8_general_ci;

-- 榜哥
CREATE TABLE `RechargeOrder` (
	`id` INT (11) NOT NULL AUTO_INCREMENT,
	`orderNumber` VARCHAR (255) NULL,
	`businessId` INT NULL,
	`userId` INT NULL,
	`state` INT NULL,
	`reChargeMoney` DOUBLE NULL,
	`grantMoney` DOUBLE NULL,
	`payType` INT NULL,
	`createTime` TIMESTAMP NOT NULL DEFAULT '2016-1-1 00:00:00',
	`updateTime` TIMESTAMP NOT NULL DEFAULT '2016-1-1 00:00:00',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci;

UPDATE SystemCode set codeValue=CONCAT(codeValue,'RechargeOrder,') where codeType='PermissionRule' and codeName=',merchant,operator,';

-- 华裕
ALTER TABLE `MemberLvlRecord` ADD COLUMN `memberBalance` DOUBLE (11, 2) NULL;

ALTER TABLE `UserExcelData`
ADD COLUMN `memberBalance`  double(11,2) NULL,
ADD COLUMN `serviceItem4`  varchar(255) NULL,
ADD COLUMN `restTime4`  varchar(255) NULL,
ADD COLUMN `serviceItem5`  varchar(255) NULL,
ADD COLUMN `restTime5`  varchar(255) NULL;


-- 鸡
-- ma
INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ("NormalSetting","businessUserTags","","商家微信标签Id，多个标签用,隔开");
INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ("NormalSetting","operatorUserTags","","运营微信标签Id，多个标签用,隔开");
INSERT INTO SystemCode (codeType,codeName,codeValue,codeShowName)VALUES ("NormalSetting","superUserTags","","更高级的标签，即如果用户的标签中包含了这些，则不重新赋值标签，多个标签用,隔开");


-- 谢
ALTER table PackCluster ADD shareDescription VARCHAR(255) NULL,ADD attachedText text;