-- Add proper enum support for product categories
-- This migration ensures the category column works with our new enum system

-- Update the category column to be more descriptive (optional)
ALTER TABLE products MODIFY COLUMN category VARCHAR(100) COMMENT 'Product category using standardized enum values';

-- Add index for better category filtering performance
CREATE INDEX idx_products_category ON products(category);

-- Insert sample products with real categories
INSERT INTO products (name, description, price, stock, category, image_url, low_stock_threshold, seller_id) VALUES
('iPhone 15 Pro', 'Latest iPhone with A17 chip, 5G connectivity, and advanced camera system', 999.99, 50, 'MOBILE_PHONES', 'https://store.storeimages.cdn-apple.com/4982/asr-202402_GEO_US.jpg', 10, 1),
('Samsung 55" OLED TV', 'Stunning 4K OLED display with smart TV features', 1299.99, 25, 'TVS_APPLIANCES', 'https://images.samsung.com/is/image/samsung/us/tvs/od55-tqnua/001_Black.jpg', 5, 1),
('Nike Air Max 270', "Comfortable running shoes with Nike's Air technology", 189.99, 75, 'FOOTWEAR', 'https://static.nike.com/a/images/t_PDP_864c6e3-442c-49fd-9ff4f5c5a1_2400.png', 15, 1),
('MacBook Pro 16"', 'Powerful MacBook with M3 Pro chip, stunning display, and all-day battery life', 2499.99, 30, 'LAPTOPS_COMPUTERS', 'https://store.storeimages.cdn-apple.com/4982/asr-mbp314202402_GEO_US.jpg', 10, 1),
('Levi 501 Jeans', 'Classic fit jeans with modern styling and comfortable denim', 89.99, 100, 'MENS_CLOTHING', 'https://i.levi.com/image/upload/w_1000/q_auto/f_auto/levi/US/en-US/Detail/005116920-detail.jpg', 20, 1),
('Sony WH-1000XM4 Headphones', 'Premium wireless headphones with exceptional noise cancellation and sound quality', 349.99, 40, 'AUDIO_HEADPHONES', 'https://images.sony.com/is/image/sony/headphones/wh-1000xm4/main.jpg', 8, 1),
('Instant Pot Duo', '7-in-1 electric pressure cooker with advanced cooking programs', 79.99, 60, 'KITCHEN_APPLIANCES', 'https://www.instantpot.com/wp-content/uploads/2021/06/Duo-Plus-7in-1-Graphite.jpg', 12, 1),
('Summer Dress', 'Elegant summer dress perfect for warm weather occasions', 129.99, 45, 'WOMENS_CLOTHING', 'https://example.com/dress.jpg', 25, 1),
('Canon EOS R6', 'Professional mirrorless camera with 45MP full-frame sensor', 2499.99, 15, 'CAMERAS_PHOTOGRAPHY', 'https://www.usa.canon.com/images/2023/eos_r6_001.jpg', 5, 1),
('Yoga Mat Premium', 'Extra thick yoga mat with superior cushioning and non-slip surface', 39.99, 80, 'SPORTS_FITNESS', 'https://example.com/yogamat.jpg', 30, 1),
('Lancome Teint Idole Ultra Wear Foundation', 'Long-lasting foundation with natural finish and SPF protection', 45.99, 85, 'MAKEUP_SKINCARE', 'https://example.com/foundation.jpg', 50, 1),
('Michael Kors Smartwatch', 'Luxury smartwatch with health tracking and premium materials', 399.99, 35, 'WATCHES_ACCESSORIES', 'https://example.com/smartwatch.jpg', 20, 1),
('Samsonite Carry-On Luggage', 'Durable lightweight luggage set with spinner wheels and TSA lock', 159.99, 40, 'BAGS_LUGGAGE', 'https://example.com/luggage.jpg', 25, 1),
('Modern Sofa Set', 'Comfortable 3-piece sofa set with premium fabric', 899.99, 15, 'FURNITURE', 'https://example.com/sofa.jpg', 8, 1),
('Coffee Maker Deluxe', 'Programmable coffee maker with thermal carafe and auto-brew', 129.99, 55, 'KITCHEN_APPLIANCES', 'https://example.com/coffeemaker.jpg', 18, 1),
('Winter Jacket', 'Warm and waterproof winter jacket with removable hood', 199.99, 65, 'MENS_CLOTHING', 'https://example.com/jacket.jpg', 30, 1),
('Wireless Earbuds Pro', 'True wireless earbuds with active noise cancellation and 24hr battery', 179.99, 90, 'AUDIO_HEADPHONES', 'https://example.com/earbuds.jpg', 25, 1),
('Gaming Laptop RTX 4070', 'High-performance gaming laptop with RTX graphics and RGB keyboard', 1899.99, 22, 'LAPTOPS_COMPUTERS', 'https://example.com/gaminglaptop.jpg', 12, 1),
('Designer Handbag', 'Premium leather handbag with multiple compartments and gold hardware', 289.99, 35, 'BAGS_LUGGAGE', 'https://example.com/handbag.jpg', 20, 1),
('Protein Powder Vanilla', 'Premium whey protein powder with natural vanilla flavor', 54.99, 120, 'NUTRITION_SUPPLEMENTS', 'https://example.com/protein.jpg', 60, 1),
('Luxury Watch Collection', 'Premium watches with Swiss movement and leather straps', 899.99, 25, 'WATCHES_ACCESSORIES', 'https://example.com/luxurywatch.jpg', 15, 1),
('Bluetooth Speaker JBL', 'Portable Bluetooth speaker with 360-degree sound and waterproof design', 89.99, 70, 'AUDIO_HEADPHONES', 'https://example.com/speaker.jpg', 40, 1),
('Running Shoes Ultra', 'Professional running shoes with advanced cushioning and breathable mesh', 159.99, 85, 'FOOTWEAR', 'https://example.com/runningshoes.jpg', 35, 1),
('Smart TV 65" 4K', 'Smart TV with built-in streaming apps and voice control', 799.99, 30, 'TVS_APPLIANCES', 'https://example.com/smarttv.jpg', 15, 1);
