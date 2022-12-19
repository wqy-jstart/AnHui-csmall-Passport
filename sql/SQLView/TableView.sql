# 创建视图前,先定位数据库(在哪个数据库下创建?)
USE csmall_ams;
# 创建视图:admin_view为自定的视图名称
CREATE VIEW admin_view as
SELECT ams_admin.username, ams_role.name rname, ams_permission.name pname
FROM csmall_ams.ams_admin
         JOIN csmall_ams.ams_admin_role
              ON ams_admin_role.admin_id = ams_admin.id
         JOIN csmall_ams.ams_role
              ON ams_role.id = ams_admin_role.role_id
         JOIN csmall_ams.ams_role_permission
              ON ams_role.id = ams_role_permission.role_id
         JOIN csmall_ams.ams_permission
              ON ams_permission.id = ams_role_permission.permission_id;
# 根据视图查询需要的数据
SELECT username,rname,pname FROM admin_view;
# 删除视图
DROP VIEW IF EXISTS admin_view;

CREATE TABLE user(
                     id int unsigned primary key ,
                     age int ,
                     name varchar(50)
);
DROP TABLE IF EXISTS user;

