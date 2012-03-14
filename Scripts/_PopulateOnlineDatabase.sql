-- Warning: This file is generated, it should not be updated manually 
-- Concatenation of the INSERT INTO statements found in the other files 
-- The COMMIT statements are used here after each INSERT INTO because of an error with an unknown FOREIGN KEY while using the phpPgAdmin Web interface 
 
-- psql -h localhost -p 5432 -d toursims -U postgres -f ./PopulateTablesUser.sql 
 
-- Table: sso COMMIT; 
 
INSERT INTO sso (sso_id, name) VALUES (1, 'Google'); COMMIT; 
 
-- Table: preferences COMMIT; 
 
INSERT INTO preferences (share_position, is_guide) VALUES (true, true); COMMIT; 
INSERT INTO preferences (share_position, is_guide) VALUES (true, true); COMMIT; 
 
-- Table: message COMMIT; 
 
INSERT INTO message (text, latitude, longitude, "timestamp", rdv_latitude, rdv_longitude, rdv_timestamp, reply_message_id) VALUES ('Hello World!', 45.781389, 4.872222, now(), 48.854444, 2.320833, now(), null); COMMIT; 
INSERT INTO message (text, latitude, longitude, "timestamp", rdv_latitude, rdv_longitude, rdv_timestamp, reply_message_id) VALUES ('Answer to the Hello World!', 45.781389, 4.872222, now(), 48.854444, 2.320833, now(), 1); COMMIT; 
 
-- Table: "user" COMMIT; 
 
INSERT INTO "user" (name, avatar, preferences_id, sso_id, sso_name) VALUES ('Administrator', 'http://code.google.com/p/tour-sims/logo?cct=1330333365', 1, 1, 'administrator@toursims.com'); COMMIT; 
INSERT INTO "user" (name, avatar, preferences_id, sso_id, sso_name) VALUES ('Stéphane Seng', 'https://lh5.googleusercontent.com/-D6UO0_G4D2M/AAAAAAAAAAI/AAAAAAAAAAA/XHg1v57fjBU/photo.jpg?sz=50', 2, 1, '107988237770371698580'); COMMIT; 
 
-- Table: checkin COMMIT; 
 
INSERT INTO checkin (latitude, longitude, "timestamp", user_id) VALUES (45.767628, 4.83499, now(), 1); COMMIT; 
 
-- Table: user_user COMMIT; 
 
INSERT INTO user_user (user_a_id, user_b_id) VALUES (1, 2); COMMIT; 
INSERT INTO user_user (user_a_id, user_b_id) VALUES (2, 1); COMMIT; 
 
-- Table: user_message COMMIT; 
 
INSERT INTO user_message (user_id, message_id, is_writer) VALUES (1, 1, true); COMMIT; 
INSERT INTO user_message (user_id, message_id, is_writer) VALUES (1, 2, true); COMMIT; 
INSERT INTO user_message (user_id, message_id, is_writer) VALUES (2, 1, false); COMMIT; 
INSERT INTO user_message (user_id, message_id, is_writer) VALUES (2, 2, false); COMMIT; 
 
-- psql -h localhost -p 5432 -d toursims -U postgres -f ./PopulateTablesCityCoursePOI.sql 
 
-- Table: city COMMIT; 
 
INSERT INTO city (name, description, latitude, longitude) VALUES ('Lyon', 'Avant, avant, Lion le melhor', 45.7597, 4.8422); COMMIT; 
INSERT INTO city (name, description, latitude, longitude) VALUES ('Paris', 'Fluctuat nec mergitur', 48.8567, 2.3508); COMMIT; 
INSERT INTO city (name, description, latitude, longitude) VALUES ('Berlin', '', 52.500556, 13.398889); COMMIT; 
INSERT INTO city (name, description, latitude, longitude) VALUES ('Hong Kong', '', 22.278333, 114.158889); COMMIT; 
INSERT INTO city (name, description, latitude, longitude) VALUES ('Sydney', '', -33.859972, 151.211111); COMMIT; 
 
-- Table: course COMMIT; 
 
INSERT INTO course (name, description, difficulty, file, "timestamp", user_id, city_id) VALUES ('La DOUA - IF', 'Parcours de la DOUA qui prend environ 3h', 10, 'http://www.x00b.com/tour.kml', now(), 1, 1); COMMIT; 
INSERT INTO course (name, description, difficulty, file, "timestamp", user_id, city_id) VALUES ('Only Lyon', 'Un vrai tour de Lyon!', 10, 'http://www.x00b.com/tour2.kml', now(), 1, 1); COMMIT; 
 
-- Table: poi COMMIT; 
 
-- INSERT INTO poi(name, description, latitude, longitude, address, "timestamp", user_id) COMMIT; 
-- SELECT COMMIT; 
-- 	name, COMMIT; 
-- 	description, COMMIT; 
-- 	latitude, COMMIT; 
-- 	longitude, COMMIT; 
-- 	address, COMMIT; 
-- 	now() AS "timestamp", COMMIT; 
-- 	1 AS user_id COMMIT; 
-- FROM COMMIT; 
-- 	dblink( COMMIT; 
-- 		'dbname=osm port=5432 user=postgres password=postgres', COMMIT; 
-- 		'SELECT COMMIT; 
-- 			tags -> ''name'' AS name, COMMIT; 
-- 			tags -> ''address'' AS address, COMMIT; 
-- 			tags -> ''description'' AS description, COMMIT; 
-- 			ST_Y(geom) AS latitude, COMMIT; 
-- 			ST_X(geom) AS longitude COMMIT; 
-- 		FROM COMMIT; 
-- 			nodes COMMIT; 
-- 		WHERE COMMIT; 
-- 			tags ? ''name'' COMMIT; 
-- 			AND tags ? ''tourism'';') COMMIT; 
-- 	AS nodes(name character varying, address character varying, description character varying, latitude double precision, longitude double precision); COMMIT; 
 
-- Table: metadata_tag COMMIT; 
 
INSERT INTO metadata_tag (metadata_tag_id, name) VALUES (1, 'IMAGE'); COMMIT; 
INSERT INTO metadata_tag (metadata_tag_id, name) VALUES (2, 'HTML'); COMMIT; 
 
-- Table: category COMMIT; 
 
INSERT INTO category (category_id, name) VALUES (1, 'Touristic'); COMMIT; 
INSERT INTO category (category_id, name) VALUES (2, 'Administrative'); COMMIT; 
 
-- Table: rating COMMIT; 
 
INSERT INTO rating (rating, user_id, course_id) VALUES (5, 1, 1); COMMIT; 
 
-- Table: comment COMMIT; 
 
INSERT INTO comment (text, "timestamp", user_id, course_id) VALUES ('Hello World!', now(), 1, 1); COMMIT; 
 
-- Table: metadata COMMIT; 
 
INSERT INTO metadata (text, metadata_tag_id) VALUES ('http://www.lyon-serrurier.fr/images/logo-ville-lyon.png', 1); COMMIT; 
INSERT INTO metadata (text, metadata_tag_id) VALUES ('http://lh5.ggpht.com/I7ZXI1UTSQQIje-omgMRi5KAU2f0-wz2OHCbqrcaKohKu2QicRVBCGiX_b-ZuDK4wc0k5p6g_l9nqWX7NTg=s128-c', 1); COMMIT; 
 
-- Table: user_category COMMIT; 
   COMMIT; 
-- COMMIT; 
   COMMIT; 
-- Table: city_course_poi_metadata COMMIT; 
 
INSERT INTO city_course_poi_metadata (course_id, metadata_id, city_id, poi_id) VALUES (NULL, 1, 1, NULL); COMMIT; 
INSERT INTO city_course_poi_metadata (course_id, metadata_id, city_id, poi_id) VALUES (NULL, 2, 2, NULL); COMMIT; 
 
-- Table: course_poi_category COMMIT; 
 
INSERT INTO course_poi_category (course_id, category_id, poi_id) VALUES (1, 1, NULL); COMMIT; 
 
-- psql -h localhost -p 5432 -d toursims -U postgres -f ./PopulateTablesStepTrial.sql 
 
-- Table: step COMMIT; 
 
-- COMMIT; 
 
-- Table: subject COMMIT; 
 
-- COMMIT; 
 
-- Table: trial COMMIT; 
 
-- COMMIT; 
 
-- Table: course_step COMMIT; 
 
-- COMMIT; 
 
-- Table: poi_step_trial COMMIT; 
 
-- COMMIT; 
 
