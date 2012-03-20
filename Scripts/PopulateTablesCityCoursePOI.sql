-- Table: city

INSERT INTO city (name, description, latitude, longitude) VALUES ('Lyon', 'Avant, avant, Lion le melhor', 45.7597, 4.8422);
INSERT INTO city (name, description, latitude, longitude) VALUES ('Paris', 'Fluctuat nec mergitur', 48.8567, 2.3508);
INSERT INTO city (name, description, latitude, longitude) VALUES ('Berlin', '', 52.500556, 13.398889);
INSERT INTO city (name, description, latitude, longitude) VALUES ('Hong Kong', '', 22.278333, 114.158889);
INSERT INTO city (name, description, latitude, longitude) VALUES ('Sydney', '', -33.859972, 151.211111);

-- Table: course

INSERT INTO course (name, description, difficulty, file, "timestamp", user_id, city_id) VALUES ('La DOUA - IF', 'Parcours de la DOUA qui prend environ 3h', 10, 'http://www.x00b.com/tour.kml', now(), 1, 1);
INSERT INTO course (name, description, difficulty, file, "timestamp", user_id, city_id) VALUES ('Only Lyon', 'Un tour de Lyon', 10, 'http://www.x00b.com/tour2.kml', now(), 1, 1);
INSERT INTO course (name, description, difficulty, file, "timestamp", user_id, city_id) VALUES ('Only Lyon', 'Un vrai tour de Lyon!', 10, 'http://www.x00b.com/tour3.kml', now(), 1, 1);

-- Table: poi

-- INSERT INTO poi(name, description, latitude, longitude, address, "timestamp", user_id)
-- SELECT
-- 	name,
-- 	description,
-- 	latitude,
-- 	longitude,
-- 	address,
-- 	now() AS "timestamp",
-- 	1 AS user_id
-- FROM
-- 	dblink(
-- 		'dbname=osm port=5432 user=postgres password=postgres',
-- 		'SELECT
-- 			tags -> ''name'' AS name,
-- 			tags -> ''address'' AS address,
-- 			tags -> ''description'' AS description,
-- 			ST_Y(geom) AS latitude,
-- 			ST_X(geom) AS longitude
-- 		FROM
-- 			nodes
-- 		WHERE
-- 			tags ? ''name''
-- 			AND tags ? ''tourism'';')
-- 	AS nodes(name character varying, address character varying, description character varying, latitude double precision, longitude double precision);

-- Table: metadata_tag

INSERT INTO metadata_tag (metadata_tag_id, name) VALUES (1, 'IMAGE');
INSERT INTO metadata_tag (metadata_tag_id, name) VALUES (2, 'HTML');

-- Table: category

INSERT INTO category (category_id, name) VALUES (1, 'Touristic');
INSERT INTO category (category_id, name) VALUES (2, 'Administrative');

-- Table: rating

INSERT INTO rating (rating, user_id, course_id) VALUES (5, 1, 1);

-- Table: comment

INSERT INTO comment (text, "timestamp", user_id, course_id) VALUES ('Hello World!', now(), 1, 1);

-- Table: metadata

INSERT INTO metadata (text, metadata_tag_id) VALUES ('http://www.lyon-serrurier.fr/images/logo-ville-lyon.png', 1);
INSERT INTO metadata (text, metadata_tag_id) VALUES ('http://lh5.ggpht.com/I7ZXI1UTSQQIje-omgMRi5KAU2f0-wz2OHCbqrcaKohKu2QicRVBCGiX_b-ZuDK4wc0k5p6g_l9nqWX7NTg=s128-c', 1);

-- Table: user_category
  
--
  
-- Table: city_course_poi_metadata

INSERT INTO city_course_poi_metadata (course_id, metadata_id, city_id, poi_id) VALUES (NULL, 1, 1, NULL);
INSERT INTO city_course_poi_metadata (course_id, metadata_id, city_id, poi_id) VALUES (NULL, 2, 2, NULL);

-- Table: course_poi_category

INSERT INTO course_poi_category (course_id, category_id, poi_id) VALUES (1, 1, NULL);
