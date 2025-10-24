-- Create the reservation schema
CREATE SCHEMA if NOT EXISTS reservation;

-- Grant privileges to the user
GRANT
ALL
PRIVILEGES
ON
SCHEMA
reservation TO yu71;
GRANT ALL PRIVILEGES ON ALL
TABLES IN SCHEMA reservation TO yu71;
GRANT ALL PRIVILEGES ON ALL
SEQUENCES IN SCHEMA reservation TO yu71;

-- Set the search path
ALTER
DATABASE reservation SET search_path TO reservation, PUBLIC;
