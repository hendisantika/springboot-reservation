-- Update naruto user's password to '53cret'
-- BCrypt hash generated for password: 53cret
UPDATE users
SET password = '$2a$10$2AZw.ilVhbo1JVoHla/mFuerwpawSxR6aMumOFgzTfQ9X0wZrxXMi'
WHERE user_id = 'naruto';
