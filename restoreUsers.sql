use activiti;

INSERT INTO `ACT_ID_USER` (ID_,REV_,FIRST_,LAST_,EMAIL_,PWD_,PICTURE_ID_) VALUES ('fozzie',8,'Fozzie','Bear','fozzie@activiti.org','fozzie','1303');
INSERT INTO `ACT_ID_USER` (ID_,REV_,FIRST_,LAST_,EMAIL_,PWD_,PICTURE_ID_) VALUES ('gonzo',8,'Gonzo','The Great','gonzo@activiti.org','gonzo','1302');
INSERT INTO `ACT_ID_USER` (ID_,REV_,FIRST_,LAST_,EMAIL_,PWD_,PICTURE_ID_) VALUES ('kermit',8,'Kermit','The Frog','kermit@activiti.org','kermit','1301');


INSERT INTO `ACT_ID_GROUP` (ID_,REV_,NAME_,TYPE_) VALUES ('admin',1,'Admin','security-role');
INSERT INTO `ACT_ID_GROUP` (ID_,REV_,NAME_,TYPE_) VALUES ('engineering',1,'Engineering','assignment');
INSERT INTO `ACT_ID_GROUP` (ID_,REV_,NAME_,TYPE_) VALUES ('management',1,'Management','assignment');
INSERT INTO `ACT_ID_GROUP` (ID_,REV_,NAME_,TYPE_) VALUES ('marketing',1,'Marketing','assignment');
INSERT INTO `ACT_ID_GROUP` (ID_,REV_,NAME_,TYPE_) VALUES ('sales',1,'Sales','assignment');
INSERT INTO `ACT_ID_GROUP` (ID_,REV_,NAME_,TYPE_) VALUES ('user',1,'User','security-role');

INSERT INTO `ACT_ID_MEMBERSHIP` (USER_ID_,GROUP_ID_) VALUES ('kermit','admin');
INSERT INTO `ACT_ID_MEMBERSHIP` (USER_ID_,GROUP_ID_) VALUES ('fozzie','engineering');
INSERT INTO `ACT_ID_MEMBERSHIP` (USER_ID_,GROUP_ID_) VALUES ('kermit','engineering');
INSERT INTO `ACT_ID_MEMBERSHIP` (USER_ID_,GROUP_ID_) VALUES ('gonzo','management');
INSERT INTO `ACT_ID_MEMBERSHIP` (USER_ID_,GROUP_ID_) VALUES ('kermit','management');
INSERT INTO `ACT_ID_MEMBERSHIP` (USER_ID_,GROUP_ID_) VALUES ('fozzie','marketing');
INSERT INTO `ACT_ID_MEMBERSHIP` (USER_ID_,GROUP_ID_) VALUES ('gonzo','marketing');
INSERT INTO `ACT_ID_MEMBERSHIP` (USER_ID_,GROUP_ID_) VALUES ('kermit','marketing');
INSERT INTO `ACT_ID_MEMBERSHIP` (USER_ID_,GROUP_ID_) VALUES ('gonzo','sales');
INSERT INTO `ACT_ID_MEMBERSHIP` (USER_ID_,GROUP_ID_) VALUES ('kermit','sales');
INSERT INTO `ACT_ID_MEMBERSHIP` (USER_ID_,GROUP_ID_) VALUES ('fozzie','user');
INSERT INTO `ACT_ID_MEMBERSHIP` (USER_ID_,GROUP_ID_) VALUES ('gonzo','user');
INSERT INTO `ACT_ID_MEMBERSHIP` (USER_ID_,GROUP_ID_) VALUES ('kermit','user');
