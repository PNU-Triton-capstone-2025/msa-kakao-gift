INSERT INTO product (name, price, image_url) VALUES
                                                 ('Test1', 30000, 'https://example.com/images/flower.jpg'),
                                                 ('Test2', 15000, 'https://example.com/images/chocolate.jpg'),
                                                 ('감사 카드', 5000, 'https://example.com/images/card.jpg'),
                                                 ('테디베어 인형', 25000, 'https://example.com/images/teddybear.jpg'),
                                                 ('향초 세트', 20000, 'https://example.com/images/candle.jpg');

-- Insert members
INSERT INTO member (email, password, role) VALUES
                                               ('test@example.com', 'l2h5', 'USER'),
                                               ('admin@example.com', '{noop}admin123', 'ADMIN');