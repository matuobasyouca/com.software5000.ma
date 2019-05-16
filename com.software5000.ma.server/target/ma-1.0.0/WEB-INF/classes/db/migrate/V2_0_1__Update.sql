-- 配置脚本

CREATE TABLE `BusinessUser` (
`id` int(11) NOT NULL AUTO_INCREMENT,
 `realName` VARCHAR(255) NULL,
`sex` VARCHAR(255) NULL,
`mobile` VARCHAR(255) NULL,
`pwd` VARCHAR(255) NULL,
`bindWeChat` INT NULL,
`wxOpenId` VARCHAR(255) NULL,
`businessId` INT NULL,
`mercharType` VARCHAR(255) NULL,
 `createTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',
 PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;