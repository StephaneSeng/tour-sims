-- Table: city

-- DROP TABLE city;

CREATE TABLE city
(
  city_id serial NOT NULL,
  name character varying,
  description character varying,
  latitude double precision,
  longitude double precision,
  CONSTRAINT city_pk PRIMARY KEY (city_id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE city
  OWNER TO toursims;

-- Table: course

-- DROP TABLE course;

CREATE TABLE course
(
  course_id serial NOT NULL,
  name character varying,
  description character varying,
  difficulty integer,
  file character varying,
  "timestamp" timestamp with time zone,
  user_id integer,
  city_id integer,
  CONSTRAINT course_pk PRIMARY KEY (course_id ),
  CONSTRAINT city_id_fk FOREIGN KEY (city_id)
      REFERENCES city (city_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_id_fk FOREIGN KEY (user_id)
      REFERENCES "user" (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE course
  OWNER TO toursims;

-- Table: poi

-- DROP TABLE poi;

CREATE TABLE poi
(
  poi_id serial NOT NULL,
  name character varying,
  description character varying,
  latitude double precision,
  longitude character varying,
  address character varying,
  "timestamp" timestamp with time zone,
  user_id integer,
  CONSTRAINT poi_pk PRIMARY KEY (poi_id ),
  CONSTRAINT user_id_fk FOREIGN KEY (user_id)
      REFERENCES "user" (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE poi
  OWNER TO toursims;
  
-- Table: metadata_tag

-- DROP TABLE metadata_tag;

CREATE TABLE metadata_tag
(
  metadata_tag_id integer NOT NULL,
  name character varying,
  CONSTRAINT metadata_tag_pk PRIMARY KEY (metadata_tag_id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE metadata_tag
  OWNER TO toursims;

-- Table: category

-- DROP TABLE category;

CREATE TABLE category
(
  category_id integer NOT NULL,
  name character varying,
  CONSTRAINT category_pk PRIMARY KEY (category_id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE category
  OWNER TO toursims;

-- Table: rating

-- DROP TABLE rating;

CREATE TABLE rating
(
  rating_id serial NOT NULL,
  rating double precision,
  user_id integer,
  course_id integer,
  city_id integer,
  poi_id integer,
  CONSTRAINT rating_pk PRIMARY KEY (rating_id ),
  CONSTRAINT city_id_fk FOREIGN KEY (city_id)
      REFERENCES city (city_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT course_id_fk FOREIGN KEY (course_id)
      REFERENCES course (course_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT poi_id_fk FOREIGN KEY (poi_id)
      REFERENCES poi (poi_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_id_fk FOREIGN KEY (user_id)
      REFERENCES "user" (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE rating
  OWNER TO toursims;

-- Table: comment

-- DROP TABLE comment;

CREATE TABLE comment
(
  comment_id serial NOT NULL,
  text character varying,
  "timestamp" timestamp with time zone,
  user_id integer,
  course_id integer,
  city_id integer,
  poi_id integer,
  CONSTRAINT comment_pk PRIMARY KEY (comment_id ),
  CONSTRAINT city_id_fk FOREIGN KEY (city_id)
      REFERENCES city (city_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT course_id_fk FOREIGN KEY (course_id)
      REFERENCES course (course_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT poi_id_fk FOREIGN KEY (poi_id)
      REFERENCES poi (poi_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_id_fk FOREIGN KEY (user_id)
      REFERENCES "user" (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE comment
  OWNER TO toursims;

-- Table: metadata

-- DROP TABLE metadata;

CREATE TABLE metadata
(
  metadata_id serial NOT NULL,
  text character varying,
  metadata_tag_id integer,
  CONSTRAINT metadata_pk PRIMARY KEY (metadata_id ),
  CONSTRAINT metadata_tag_id_fk FOREIGN KEY (metadata_tag_id)
      REFERENCES metadata_tag (metadata_tag_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE metadata
  OWNER TO toursims;

-- Table: user_category

-- DROP TABLE user_category;

CREATE TABLE user_category
(
  user_id integer NOT NULL,
  category_id integer NOT NULL,
  CONSTRAINT user_category_pk PRIMARY KEY (user_id , category_id ),
  CONSTRAINT category_id_fk FOREIGN KEY (category_id)
      REFERENCES category (category_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_id_fk FOREIGN KEY (user_id)
      REFERENCES "user" (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE user_category
  OWNER TO toursims;
  
-- Table: city_course_poi_metadata

-- DROP TABLE city_course_poi_metadata;

CREATE TABLE city_course_poi_metadata
(
  course_id integer,
  metadata_id integer,
  city_id integer,
  poi_id integer,
  CONSTRAINT city_id_fk FOREIGN KEY (city_id)
      REFERENCES city (city_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT course_id_fk FOREIGN KEY (course_id)
      REFERENCES course (course_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT metadata_id_fk FOREIGN KEY (metadata_id)
      REFERENCES metadata (metadata_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT poi_id_fk FOREIGN KEY (poi_id)
      REFERENCES poi (poi_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE city_course_poi_metadata
  OWNER TO toursims;

-- Table: course_poi_category

-- DROP TABLE course_poi_category;

CREATE TABLE course_poi_category
(
  course_id integer,
  category_id integer,
  poi_id integer,
  CONSTRAINT category_id_fk FOREIGN KEY (category_id)
      REFERENCES category (category_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT course_id_fk FOREIGN KEY (course_id)
      REFERENCES course (course_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT poi_id_fk FOREIGN KEY (poi_id)
      REFERENCES poi (poi_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE course_poi_category
  OWNER TO toursims;
