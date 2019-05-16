INSERT INTO SystemCode (codeType, codeName, codeShowName, codeFiter) VALUES ('StateCode', 'ZS012040', '该微信已购买过，无法重复购买', '0');
INSERT INTO SystemCode (codeType, codeName, codeShowName, codeFiter) VALUES ('StateCode', 'ZS012041', '该车牌已购买过，无法重复购买', '0');

INSERT INTO SystemCode (codeType, codeName, codeValue, codeShowName, codeFiter) VALUES ('CashierDesk', 'cashierDeskUrl', 'http://ma.mmgoodscar.com/open/cashier_desk.html', '正在跳转地址', '9');

CREATE TABLE `CouponsPack` (
	`id` INT (11) NOT NULL AUTO_INCREMENT,
	`title` VARCHAR (255) DEFAULT NULL,
	`content` VARCHAR (1024) DEFAULT NULL,
	`buyAmouont` double DEFAULT NULL,
	`cpUuids` VARCHAR (1024) DEFAULT NULL,
	`createTime` TIMESTAMP NOT NULL DEFAULT '2016-1-1 00:00:00',
	`updateTime` TIMESTAMP NOT NULL DEFAULT '2016-1-1 00:00:00',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COLLATE = utf8_bin;

CREATE TABLE `CouponsPackBuyRecord` (
	`id` INT (11) NOT NULL AUTO_INCREMENT,
	`orderNo` VARCHAR (255) DEFAULT NULL,
	`businessId` int DEFAULT NULL,
	`wxOpenId` VARCHAR (255) DEFAULT NULL,
	`mobile` VARCHAR (255) DEFAULT NULL,
	`carNumber` VARCHAR (255) DEFAULT NULL,
	`packId` int DEFAULT NULL,
	`buyCount` int DEFAULT NULL,
	`state` int DEFAULT NULL,
	`prepayId` VARCHAR (255) DEFAULT NULL,
	`payTime` TIMESTAMP NULL,
	`createTime` TIMESTAMP NOT NULL DEFAULT '2016-1-1 00:00:00',
	`updateTime` TIMESTAMP NOT NULL DEFAULT '2016-1-1 00:00:00',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COLLATE = utf8_bin;

UPDATE SystemCode SET codeValue = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcGkiLCJuYmYiOjE0OTkyNDcwOTEsImlzcyI6ImNvbS56c2NwLmVta3QiLCJleHAiOjQxMDIzMjk2MDAsImlhdCI6MTQ5OTI0NzA5MSwianRpIjoiN2M4ZjI3MDctNDMzNC00ODRkLThkZGQtMzZkNGNjN2U0Nzk0In0.kR-idw5tKygnvj6yq72cwMNKhGMKxw2YQEgQXLoY-D0' WHERE codeName = 'maToken';

UPDATE WeChatPayOrder SET packageOrderId = workOrderId WHERE orderType = 0 AND workOrderId is NOT NULL;

CREATE TABLE `PayOrder` (
`id` int(11) NOT NULL AUTO_INCREMENT,
 `orderTitle` VARCHAR(255) NULL,
`totalPrice` INT NULL,
`orderService` VARCHAR(255) NULL,
`orderNo` VARCHAR(255) NULL,
`orderDetail` VARCHAR(255) NULL,
`orderContent` VARCHAR(255) NULL,
`redirectUrl` VARCHAR(255) NULL,
`notifyUrl` VARCHAR(255) NULL,
`payState` INT NULL,
`prepayId` VARCHAR(255) NULL,
 `createTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
 PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

 INSERT INTO SystemCode (codeType, codeName, codeValue, codeFiter) VALUES ('CashierDesk', 'payOrderNotifyUrl', 'http://ma.mmgoodcar.com/open/payOrderNotify', '9');
 INSERT INTO SystemCode (codeType, codeName, codeShowName, codeFiter) VALUES ('StateCode', 'ZS012042', 'code已经被使用', '0');
 ALTER TABLE `PayOrder` MODIFY COLUMN `orderDetail`  varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `id`;

INSERT INTO SystemCode (codeType, codeName, codeValue, codeFiter) VALUES ('CashierDesk', 'lotteryUrl', 'http://ma.mmgoodcar.com/open/lottery.html', '9');
INSERT INTO SystemCode (codeType, codeName, codeValue, codeFiter) VALUES ('CashierDesk', 'lotteryBusinessIds', ',21,5,38,', '9');

INSERT INTO SystemCode (codeType, codeName, codeShowName, codeFiter) VALUES ('StateCode', 'ZS012043', '订单已失效', '0');

INSERT INTO `CouponsPack` VALUES (1, '99元诚品车友会VIP卡', '内容：<br/>1、车有礼包：小米充电宝一个 + 车载手机支架 + 炭包公仔一只（价值 100元+）；<br/>2、车友优惠：免费洗车券12张（直接充值发放到微信账户，价值300元+）；<br/>3、车友活动：免费观影或亲子自驾游等（定期组织）；', 99, 'af157c07-fe17-4911-a68f-b1ef6801ce78,2d6164c4-c17b-4a6d-b820-a8d92ac575b7,267f2c8f-2221-479c-ad61-40c08d124978,b6a08983-3095-4172-b7c2-c35ba6d15297,953d0420-9505-46a4-92d0-cd0623dd5b64,7b11cde5-3c9c-4f40-93ac-95b755d3565d,b84e31b5-6721-40a9-ba5e-7adc276186c4,c9c83ffa-0d43-4405-b4f3-aa8b4eb129a6,38d7ae30-2dd6-4160-bc95-d3a8376b2b1b,0f88e500-fdcd-4850-94d8-6311758de4c1,d3953909-1de7-41de-83a7-8a30a3e1813f,4e6c3c2d-8b6e-406d-b073-791cffb1fecc', '2016-1-1 00:00:00', '2016-1-1 00:00:00');


INSERT INTO SystemCode (codeType, codeName, codeValue, codeFiter, codeDesc) VALUES ('lottery', 'lotteryCoupons', '197acbb6-546b-4324-9e18-ba94480b48ae,031bd1fb-1d39-43a9-830d-fd7f8038bd0e,6b01cd63-14c4-4544-a88a-e5a5bf4d5b62', '9', '空调清洗1次,免费洗车1次,10元现金券');