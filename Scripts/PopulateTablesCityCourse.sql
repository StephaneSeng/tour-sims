-- Table: city

INSERT INTO city (city_id, name, description, latitude, longitude) VALUES (1, 'Lyon', 'Avant, avant, Lion le melhor', 45.7597, 4.8422);

-- Table: course

INSERT INTO course (course_id, name, description, difficulty, file, user_id) VALUES (1, 'INSA de Lyon', 'Découvrez le campus de la Doua', 10, '', 1);

-- Table: metadata_tag

INSERT INTO metadata_tag (metadata_tag_id, name) VALUES (1, 'IMAGE');
INSERT INTO metadata_tag (metadata_tag_id, name) VALUES (2, 'HTML');

-- Table: category

INSERT INTO category (category_id, name) VALUES (1, 'Touristic');
INSERT INTO category (category_id, name) VALUES (2, 'Administrative');

-- Table: rating

--

-- Table: comment

--

-- Table: metadata

INSERT INTO metadata (metadata_id, text, metadata_tag_id) VALUES (1, 'http://www.lyon-serrurier.fr/images/logo-ville-lyon.png', 1);

-- Table: user_category
  
--
  
-- Table: city_course_metadata

INSERT INTO city_course_metadata (course_id, metadata_id, city_id) VALUES (NULL, 1, 1);

-- Table: course_category

INSERT INTO course_category (course_id, category_id) VALUES (1, 1);
