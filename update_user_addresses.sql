-- Update sample users with address data for testing
UPDATE users SET 
    address = '123 Main Street',
    city = 'Mumbai',
    state = 'Maharashtra',
    zip_code = '400001',
    country = 'India'
WHERE email IN ('buyer@example.com', 'seller@example.com', 'admin@example.com') OR user_id <= 3;

-- Check if users have address data
SELECT user_id, name, email, address, city, state, zip_code, country 
FROM users 
WHERE user_id <= 5 OR email IN ('buyer@example.com', 'seller@example.com', 'admin@example.com');
