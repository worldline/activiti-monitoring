use activiti;

INSERT INTO `act_id_user` (ID_,REV_,FIRST_,LAST_,EMAIL_,PWD_,PICTURE_ID_) VALUES ('fozzie',8,'Fozzie','Bear','fozzie@activiti.org','fozzie','1303');
INSERT INTO `act_id_user` (ID_,REV_,FIRST_,LAST_,EMAIL_,PWD_,PICTURE_ID_) VALUES ('gonzo',8,'Gonzo','The Great','gonzo@activiti.org','gonzo','1302');
INSERT INTO `act_id_user` (ID_,REV_,FIRST_,LAST_,EMAIL_,PWD_,PICTURE_ID_) VALUES ('kermit',8,'Kermit','The Frog','kermit@activiti.org','kermit','1301');


INSERT INTO `act_id_group` (ID_,REV_,NAME_,TYPE_) VALUES ('admin',1,'Admin','security-role');
INSERT INTO `act_id_group` (ID_,REV_,NAME_,TYPE_) VALUES ('engineering',1,'Engineering','assignment');
INSERT INTO `act_id_group` (ID_,REV_,NAME_,TYPE_) VALUES ('management',1,'Management','assignment');
INSERT INTO `act_id_group` (ID_,REV_,NAME_,TYPE_) VALUES ('marketing',1,'Marketing','assignment');
INSERT INTO `act_id_group` (ID_,REV_,NAME_,TYPE_) VALUES ('sales',1,'Sales','assignment');
INSERT INTO `act_id_group` (ID_,REV_,NAME_,TYPE_) VALUES ('user',1,'User','security-role');

INSERT INTO `act_id_membership` (USER_ID_,GROUP_ID_) VALUES ('kermit','admin');
INSERT INTO `act_id_membership` (USER_ID_,GROUP_ID_) VALUES ('fozzie','engineering');
INSERT INTO `act_id_membership` (USER_ID_,GROUP_ID_) VALUES ('kermit','engineering');
INSERT INTO `act_id_membership` (USER_ID_,GROUP_ID_) VALUES ('gonzo','management');
INSERT INTO `act_id_membership` (USER_ID_,GROUP_ID_) VALUES ('kermit','management');
INSERT INTO `act_id_membership` (USER_ID_,GROUP_ID_) VALUES ('fozzie','marketing');
INSERT INTO `act_id_membership` (USER_ID_,GROUP_ID_) VALUES ('gonzo','marketing');
INSERT INTO `act_id_membership` (USER_ID_,GROUP_ID_) VALUES ('kermit','marketing');
INSERT INTO `act_id_membership` (USER_ID_,GROUP_ID_) VALUES ('gonzo','sales');
INSERT INTO `act_id_membership` (USER_ID_,GROUP_ID_) VALUES ('kermit','sales');
INSERT INTO `act_id_membership` (USER_ID_,GROUP_ID_) VALUES ('fozzie','user');
INSERT INTO `act_id_membership` (USER_ID_,GROUP_ID_) VALUES ('gonzo','user');
INSERT INTO `act_id_membership` (USER_ID_,GROUP_ID_) VALUES ('kermit','user');
