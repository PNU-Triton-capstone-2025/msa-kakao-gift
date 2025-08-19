INSERT INTO Member (email, password, role) VALUES ('admin@example.com', 'l2h5', 'ADMIN');
INSERT INTO Member (email, password, role) VALUES ('md@example.com', 'l2h5', 'MD');
INSERT INTO Member (email, password, role) VALUES ('test@example.com', 'l2h5', 'USER');
INSERT INTO Member (email, password, role) VALUES ('user2@example.com', 'user2_password', 'USER');

-- Product
INSERT INTO Product (name, price, image_url) VALUES ('고급 초콜릿 세트', 25000, 'https://via.placeholder.com/150/chocolate');
INSERT INTO Product (name, price, image_url) VALUES ('프리미엄 무선 이어폰', 159000, 'https://via.placeholder.com/150/earphone');
INSERT INTO Product (name, price, image_url) VALUES ('시그니처 향수', 78000, 'https://via.placeholder.com/150/perfume');
INSERT INTO Product (name, price, image_url) VALUES ('기계식 게이밍 키보드', 120000, 'https://via.placeholder.com/150/keyboard');
INSERT INTO Product (name, price, image_url) VALUES ('보온보냉 텀블러', 18000, 'https://via.placeholder.com/150/tumbler');
INSERT INTO Product (name, price, image_url) VALUES ('아로마 캔들', 32000, 'https://via.placeholder.com/150/candle');
INSERT INTO Product (name, price, image_url) VALUES ('가죽 지갑', 89000, 'https://via.placeholder.com/150/wallet');
INSERT INTO Product (name, price, image_url) VALUES ('스마트 워치', 349000, 'https://via.placeholder.com/150/smartwatch');
INSERT INTO Product (name, price, image_url) VALUES ('블루투스 스피커', 75000, 'https://via.placeholder.com/150/speaker');
INSERT INTO Product (name, price, image_url) VALUES ('에스프레소 머신', 220000, 'https://via.placeholder.com/150/coffee');
INSERT INTO Product (name, price, image_url) VALUES ('요가 매트', 28000, 'https://via.placeholder.com/150/yoga');
INSERT INTO Product (name, price, image_url) VALUES ('디퓨저 세트', 45000, 'https://via.placeholder.com/150/diffuser');
INSERT INTO Product (name, price, image_url) VALUES ('전기 면도기', 99000, 'https://via.placeholder.com/150/shaver');
INSERT INTO Product (name, price, image_url) VALUES ('캐주얼 백팩', 110000, 'https://via.placeholder.com/150/backpack');
INSERT INTO Product (name, price, image_url) VALUES ('핸드크림 선물 세트', 19000, 'https://via.placeholder.com/150/handcream');
INSERT INTO Product (name, price, image_url) VALUES ('캡슐 커피 30개입', 21000, 'https://via.placeholder.com/150/capsule');
INSERT INTO Product (name, price, image_url) VALUES ('LED 무드등', 38000, 'https://via.placeholder.com/150/lamp');
INSERT INTO Product (name, price, image_url) VALUES ('고급 만년필', 135000, 'https://via.placeholder.com/150/pen');
INSERT INTO Product (name, price, image_url) VALUES ('보드게임', 42000, 'https://via.placeholder.com/150/boardgame');
INSERT INTO Product (name, price, image_url) VALUES ('넥밴드 선풍기', 23000, 'https://via.placeholder.com/150/fan');
INSERT INTO Product (name, price, image_url) VALUES ('와인 오프너 세트', 31000, 'https://via.placeholder.com/150/wine');
INSERT INTO Product (name, price, image_url) VALUES ('패션 선글라스', 170000, 'https://via.placeholder.com/150/sunglasses');
INSERT INTO Product (name, price, image_url) VALUES ('캠핑 의자', 55000, 'https://via.placeholder.com/150/chair');
INSERT INTO Product (name, price, image_url) VALUES ('스마트 체중계', 39000, 'https://via.placeholder.com/150/scale');
INSERT INTO Product (name, price, image_url) VALUES ('전동칫솔', 68000, 'https://via.placeholder.com/150/toothbrush');
INSERT INTO Product (name, price, image_url) VALUES ('목 마사지기', 85000, 'https://via.placeholder.com/150/massage');
INSERT INTO Product (name, price, image_url) VALUES ('극세사 담요', 29000, 'https://via.placeholder.com/150/blanket');
INSERT INTO Product (name, price, image_url) VALUES ('멀티 비타민', 33000, 'https://via.placeholder.com/150/vitamin');
INSERT INTO Product (name, price, image_url) VALUES ('메탈 책갈피', 9000, 'https://via.placeholder.com/150/bookmark');
INSERT INTO Product (name, price, image_url) VALUES ('여행용 캐리어 24인치', 180000, 'https://via.placeholder.com/150/carrier');

-- ProductOption Test Data
-- Product 1: 고급 초콜릿 세트
INSERT INTO Product_Option (product_id, name, quantity) VALUES (1, '01. 다크 초콜릿 72%', 150);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (1, '02. 밀크 초콜릿 45%', 200);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (1, '03. 화이트 초콜릿', 180);

-- Product 2: 프리미엄 무선 이어폰
INSERT INTO Product_Option (product_id, name, quantity) VALUES (2, '01. 스페이스 블랙', 100);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (2, '02. 펄 화이트', 120);

-- Product 3: 시그니처 향수
INSERT INTO Product_Option (product_id, name, quantity) VALUES (3, '01 50ml', 80);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (3, '02 100ml (+20000원)', 50);

-- Product 4: 기계식 게이밍 키보드
INSERT INTO Product_Option (product_id, name, quantity) VALUES (4, '01 청축', 110);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (4, '02 갈축', 130);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (4, '03 적축', 90);

-- Product 5: 보온보냉 텀블러
INSERT INTO Product_Option (product_id, name, quantity) VALUES (5, '01 500ml (실버)', 300);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (5, '02 750ml (블랙)', 250);

-- Product 6: 아로마 캔들
INSERT INTO Product_Option (product_id, name, quantity) VALUES (6, '01 라벤더', 220);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (6, '02 우드 세이지', 180);

-- Product 7: 가죽 지갑
INSERT INTO Product_Option (product_id, name, quantity) VALUES (7, '01 블랙', 140);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (7, '02 브라운', 160);

-- Product 8: 스마트 워치
INSERT INTO Product_Option (product_id, name, quantity) VALUES (8, '01 44mm (그래파이트)', 70);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (8, '02 40mm (핑크골드)', 85);

-- Product 9: 블루투스 스피커
INSERT INTO Product_Option (product_id, name, quantity) VALUES (9, '01 기본 옵션', 200);

-- Product 10: 에스프레소 머신
INSERT INTO Product_Option (product_id, name, quantity) VALUES (10, '01 실버', 60);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (10, '02 레드', 40);

-- Product 11: 요가 매트
INSERT INTO Product_Option (product_id, name, quantity) VALUES (11, '01 8mm (퍼플)', 300);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (11, '02 10mm (그린)', 250);

-- Product 12: 디퓨저 세트
INSERT INTO Product_Option (product_id, name, quantity) VALUES (12, '01 블랙체리', 180);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (12, '02 클린코튼', 220);

-- Product 13: 전기 면도기
INSERT INTO Product_Option (product_id, name, quantity) VALUES (13, '01 기본 모델', 150);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (13, '02 여행용 파우치 포함', 100);

-- Product 14: 캐주얼 백팩
INSERT INTO Product_Option (product_id, name, quantity) VALUES (14, '01 블랙', 190);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (14, '02 네이비', 160);

-- Product 15: 핸드크림 선물 세트
INSERT INTO Product_Option (product_id, name, quantity) VALUES (15, '01 3종 세트', 400);

-- Product 16: 캡슐 커피 30개입
INSERT INTO Product_Option (product_id, name, quantity) VALUES (16, '01 인텐소', 500);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (16, '02 디카페나토', 300);

-- Product 17: LED 무드등
INSERT INTO Product_Option (product_id, name, quantity) VALUES (17, '01 기본 옵션', 280);

-- Product 18: 고급 만년필
INSERT INTO Product_Option (product_id, name, quantity) VALUES (18, '01 F촉', 120);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (18, '02 EF촉', 110);

-- Product 19: 보드게임
INSERT INTO Product_Option (product_id, name, quantity) VALUES (19, '01 기본판', 170);

-- Product 20: 넥밴드 선풍기
INSERT INTO Product_Option (product_id, name, quantity) VALUES (20, '01 화이트', 350);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (20, '02 핑크', 300);

-- Product 21: 와인 오프너 세트
INSERT INTO Product_Option (product_id, name, quantity) VALUES (21, '01 기본 세트', 240);

-- Product 22: 패션 선글라스
INSERT INTO Product_Option (product_id, name, quantity) VALUES (22, '01 기본 옵션', 130);

-- Product 23: 캠핑 의자
INSERT INTO Product_Option (product_id, name, quantity) VALUES (23, '01 베이지', 180);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (23, '02 카키', 200);

-- Product 24: 스마트 체중계
INSERT INTO Product_Option (product_id, name, quantity) VALUES (24, '01 화이트', 260);

-- Product 25: 전동칫솔
INSERT INTO Product_Option (product_id, name, quantity) VALUES (25, '01 칫솔모 2개 포함', 210);

-- Product 26: 목 마사지기
INSERT INTO Product_Option (product_id, name, quantity) VALUES (26, '01 기본 옵션', 140);

-- Product 27: 극세사 담요
INSERT INTO Product_Option (product_id, name, quantity) VALUES (27, '01 그레이', 280);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (27, '02 아이보리', 320);

-- Product 28: 멀티 비타민
INSERT INTO Product_Option (product_id, name, quantity) VALUES (28, '01 90정', 400);

-- Product 29: 메탈 책갈피
INSERT INTO Product_Option (product_id, name, quantity) VALUES (29, '01 실버', 500);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (29, '02 골드', 450);

-- Product 30: 여행용 캐리어 24인치
INSERT INTO Product_Option (product_id, name, quantity) VALUES (30, '01 실버', 90);
INSERT INTO Product_Option (product_id, name, quantity) VALUES (30, '02 블랙', 110);