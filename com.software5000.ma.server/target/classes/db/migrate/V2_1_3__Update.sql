ALTER TABLE `CouponsPackBuyRecord`
ADD INDEX `businessId` (`businessId`) USING BTREE ;


ALTER TABLE `WeChatPayOrder`
ADD INDEX `businessId` (`businessId`) USING BTREE ;


ALTER TABLE `CouponsPackBuyRecord`
ADD COLUMN `tradeFee`  int(11) NULL AFTER `buyCount`;


UPDATE CouponsPackBuyRecord SET tradeFee=9900;
UPDATE `SystemCode` SET codeValue = '2017-01-01 00:00:00' WHERE codeType = 'SecuritySetting' AND codeName = 'm.expiredTime';