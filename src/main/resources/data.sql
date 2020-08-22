INSERT ignore INTO zhixiang.authority (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT ignore INTO zhixiang.authority (id, name) VALUES (2, 'ROLE_USER');
INSERT ignore INTO zhixiang.authority (id, name) VALUES (3, 'ROLE_CHAOJI');

INSERT ignore INTO zhixiang.category (id, guid, is_hidden, name, position, user_id) VALUES (1, null, null, '先贤文化', null, null);
INSERT ignore INTO zhixiang.category (id, guid, is_hidden, name, position, user_id) VALUES (2, null, null, '特色民风', null, null);
INSERT ignore INTO zhixiang.category (id, guid, is_hidden, name, position, user_id) VALUES (3, null, null, '地方古迹', null, null);
INSERT ignore INTO zhixiang.category (id, guid, is_hidden, name, position, user_id) VALUES (4, null, null, '文化艺术', null, null);

INSERT ignore INTO zhixiang.user (id, answer_size, article_size, avatar, city, contact, create_time, email, fan_size, follow_size, github, homepage, is_verify_email, last_login_time, nickname, password, permission, profile, question_size, reputation, sex, status, username, view_size, job_id) VALUES (1, 0, 0, null, '超级管理', null, '2019-09-19 22:23:54', null, 0, 0, null, null, 'N', '2019-09-19 22:23:54', 'admin', '$2a$10$lCrQrtav8j/VMgA1lN9E2O05cqmIhiP7BJoh59tgUFHM8bTdnk/Dq', null, null, 0, 10, 0, 'normal', 'admin', 1, null);

INSERT ignore INTO zhixiang.user_authority (user_id, authority_id) VALUES (1, 1);
INSERT ignore INTO zhixiang.user_authority (user_id, authority_id) VALUES (1, 3);