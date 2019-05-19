INSERT INTO usr (user_id, first_name, last_name, role_name, password)
VALUES ('naruto', 'Uzumaki', 'Naruto', 'USER', '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK')/;
INSERT INTO usr (user_id, first_name, last_name, role_name, password)
VALUES ('sasuke', 'Uchiha', 'Sasuke', 'USER', '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK')/;
INSERT INTO usr (user_id, first_name, last_name, role_name, password)
VALUES ('sakura', 'Haruno', 'Sakura', 'USER', '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK')/;
INSERT INTO usr (user_id, first_name, last_name, role_name, password)
VALUES ('kakashi', 'Hatake', 'KAkahsi', 'ADMIN', '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK')/;
--
INSERT INTO meeting_room (room_name) VALUES ('Konohagakure')/;
INSERT INTO meeting_room (room_name) VALUES ('Sunagakure')/;
INSERT INTO meeting_room (room_name) VALUES ('Kirigakure')/;
INSERT INTO meeting_room (room_name) VALUES ('Kusagakure')/;
INSERT INTO meeting_room (room_name) VALUES ('Kumogakure')/;
INSERT INTO meeting_room (room_name) VALUES ('Tsucikage')/;
INSERT INTO meeting_room (room_name) VALUES ('Raikage')/;
-- Stored Procedure
DROP FUNCTION IF EXISTS REGISTER_RESERVABLE_ROOMS()/;
CREATE OR REPLACE FUNCTION REGISTER_RESERVABLE_ROOMS()
  RETURNS
    INT AS $$
DECLARE
  total INT;
  i INT4;
  id INT4;
BEGIN
  total := 0;
  FOR id IN SELECT room_id
            FROM meeting_room LOOP
    i := 0;
    FOR i IN 0..77 LOOP
      INSERT INTO reservable_room (reserved_date, room_id)
      VALUES (CURRENT_DATE + i - 7, id);
    END LOOP;
    total := total + i;
  END LOOP;
  RETURN total;
END;
$$ LANGUAGE plpgsql
/;
SELECT REGISTER_RESERVABLE_ROOMS() /;
COMMIT /;
