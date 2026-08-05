-- 평문 비밀번호: 12345678 (BCrypt 해시)
INSERT INTO users (nickname, login_id, password, role)
VALUES ('관리자', 'admin', '$2a$10$51s3xbOGkxjsZTEKtY36mOAIM9FDong1o7qFwdHtP5QXZIa.P2aaK', 'USER');

-- 페이징 확인용 게시글 12건 (10건씩 2페이지)
INSERT INTO posts (title, content, author_id, created_at) VALUES
('공지: 게시판을 오픈했습니다', '많이 이용해주세요.', 1, DATEADD('DAY', -11, CURRENT_TIMESTAMP)),
('첫 번째 게시글', '테스트 내용입니다.', 1, DATEADD('DAY', -10, CURRENT_TIMESTAMP)),
('두 번째 게시글', '테스트 내용입니다.', 1, DATEADD('DAY', -9, CURRENT_TIMESTAMP)),
('세 번째 게시글', '테스트 내용입니다.', 1, DATEADD('DAY', -8, CURRENT_TIMESTAMP)),
('네 번째 게시글', '테스트 내용입니다.', 1, DATEADD('DAY', -7, CURRENT_TIMESTAMP)),
('다섯 번째 게시글', '테스트 내용입니다.', 1, DATEADD('DAY', -6, CURRENT_TIMESTAMP)),
('여섯 번째 게시글', '테스트 내용입니다.', 1, DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
('일곱 번째 게시글', '테스트 내용입니다.', 1, DATEADD('DAY', -4, CURRENT_TIMESTAMP)),
('여덟 번째 게시글', '테스트 내용입니다.', 1, DATEADD('DAY', -3, CURRENT_TIMESTAMP)),
('아홉 번째 게시글', '테스트 내용입니다.', 1, DATEADD('DAY', -2, CURRENT_TIMESTAMP)),
('열 번째 게시글', '테스트 내용입니다.', 1, DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
('열한 번째 게시글', '줄바꿈 테스트입니다.' || CHAR(10) || '두 번째 줄입니다.', 1, CURRENT_TIMESTAMP);
