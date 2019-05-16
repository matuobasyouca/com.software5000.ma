ALTER TABLE `Business`
ADD COLUMN `consultantId`  int(11) NULL AFTER `closingDateNum`,
ADD COLUMN `consultantName`  varchar(255) NULL AFTER `consultantId`,
ADD COLUMN `consultantDepartment`  varchar(255) NULL AFTER `consultantName`,
ADD COLUMN `consultantDepartmentId`  int NULL AFTER `consultantDepartment`;

UPDATE SystemCode SET codeValue = '/web/merchant/open/login.html' WHERE codeName = 'm.rdLoginUrl';
UPDATE SystemCode SET codeValue = '/web/merchant/home/index.html' WHERE codeName = 'm.dfUrl';
UPDATE SystemCode SET codeValue = '/web/merchant/home/index.html' WHERE codeName = 'o.dfUrl';