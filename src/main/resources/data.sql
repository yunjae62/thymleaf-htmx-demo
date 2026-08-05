-- 평문 비밀번호: 12345678 (BCrypt 해시)
INSERT INTO users (nickname, login_id, password, role)
VALUES ('관리자', 'admin', '$2a$10$51s3xbOGkxjsZTEKtY36mOAIM9FDong1o7qFwdHtP5QXZIa.P2aaK', 'USER');
