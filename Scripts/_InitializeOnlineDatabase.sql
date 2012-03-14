-- Warning: This file is generated, it should not be updated manually 
-- Concatenation of the CREATE TABLE statements found in the other files 
 
-- psql -h localhost -p 5432 -d toursims -U postgres -f ./CreateTablesUser.sql 
 
-- Table: sso

-- DROP TABLE sso;

CREATE TABLE sso
(
  sso_id integer NOT NULL,
  name character varying,
  CONSTRAINT sso_pk PRIMARY KEY (sso_id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sso
  OWNER TO toursims;

-- Table: preferences

-- DROP TABLE preferences;

CREATE TABLE preferences
(
  preferences_id serial NOT NULL,
  share_position boolean,
  is_guide boolean,
  CONSTRAINT preferences_pk PRIMARY KEY (preferences_id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE preferences
  OWNER TO toursims;

-- Table: message

-- DROP TABLE message;

CREATE TABLE message
(
  message_id serial NOT NULL,
  text character varying,
  latitude double precision,
  longitude double precision,
  "timestamp" timestamp with time zone,
  rdv_latitude double precision,
  rdv_longitude double precision,
  rdv_timestamp timestamp with time zone,
  reply_message_id integer,
  CONSTRAINT message_pk PRIMARY KEY (message_id ),
  CONSTRAINT reply_message_id_fk FOREIGN KEY (reply_message_id)
      REFERENCES message (message_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE message
  OWNER TO toursims;
  
-- Table: "user"

-- DROP TABLE "user";

CREATE TABLE "user"
(
  user_id serial NOT NULL,
  name character varying,
  avatar character varying,
  preferences_id integer,
  sso_id integer,
  sso_name character varying,
  CONSTRAINT user_pk PRIMARY KEY (user_id ),
  CONSTRAINT preferences_id_fk FOREIGN KEY (preferences_id)
      REFERENCES preferences (preferences_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT sso_id_fk FOREIGN KEY (sso_id)
      REFERENCES sso (sso_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "user"
  OWNER TO toursims;

-- Table: checkin

-- DROP TABLE checkin;

CREATE TABLE checkin
(
  checkin_id serial NOT NULL,
  latitude double precision,
  longitude double precision,
  "timestamp" timestamp with time zone,
  user_id integer,
  CONSTRAINT checkin_pk PRIMARY KEY (checkin_id ),
  CONSTRAINT user_id_fk FOREIGN KEY (user_id)
      REFERENCES "user" (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE checkin
  OWNER TO toursims;
  
-- Table: user_user

-- DROP TABLE user_user;

CREATE TABLE user_user
(
  user_a_id integer NOT NULL,
  user_b_id integer NOT NULL,
  CONSTRAINT user_a_id_fk FOREIGN KEY (user_a_id)
      REFERENCES "user" (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_b_id_fk FOREIGN KEY (user_b_id)
      REFERENCES "user" (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE user_user
  OWNER TO toursims;
  
-- Table: user_message

-- DROP TABLE user_message;

CREATE TABLE user_message
(
  user_id integer NOT NULL,
  message_id integer NOT NULL,
  is_writer boolean,
  CONSTRAINT user_message_pk PRIMARY KEY (user_id , message_id ),
  CONSTRAINT message_id_fk FOREIGN KEY (message_id)
      REFERENCES message (message_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_id_fk FOREIGN KEY (user_id)
      REFERENCES "user" (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE user_message
  OWNER TO toursims;
 
-- psql -h localhost -p 5432 -d toursims -U postgres -f ./CreateTablesCityCoursePOI.sql 
 
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
 
-- psql -h localhost -p 5432 -d toursims -U postgres -f ./CreateTablesStepTrial.sql 
 
-- Table: step

-- DROP TABLE step;

CREATE TABLE step
(
  step_id serial NOT NULL,
  name character varying,
  distance integer,
  duration integer,
  poi_id integer,
  CONSTRAINT step_pk PRIMARY KEY (step_id ),
  CONSTRAINT poi_id_fk FOREIGN KEY (poi_id)
      REFERENCES poi (poi_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE step
  OWNER TO toursims;

-- Table: subject

-- DROP TABLE subject;

CREATE TABLE subject
(
  subject_id integer NOT NULL,
  name character varying,
  CONSTRAINT subject_pk PRIMARY KEY (subject_id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE subject
  OWNER TO toursims;

-- Table: trial

-- DROP TABLE trial;

CREATE TABLE trial
(
  trial_id serial NOT NULL,
  title character varying,
  description character varying,
  answer character varying,
  difficulty integer,
  subject_id integer,
  CONSTRAINT trial_pk PRIMARY KEY (trial_id ),
  CONSTRAINT subject_id_fk FOREIGN KEY (subject_id)
      REFERENCES subject (subject_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE trial
  OWNER TO toursims;

-- Table: course_step

-- DROP TABLE course_step;

CREATE TABLE course_step
(
  course_id integer NOT NULL,
  step_id integer NOT NULL,
  "order" integer,
  CONSTRAINT course_step_pk PRIMARY KEY (course_id , step_id ),
  CONSTRAINT course_id_fk FOREIGN KEY (course_id)
      REFERENCES course (course_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT step_id_fk FOREIGN KEY (step_id)
      REFERENCES step (step_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE course_step
  OWNER TO toursims;

-- Table: poi_step_trial

-- DROP TABLE poi_step_trial;

CREATE TABLE poi_step_trial
(
  step_id integer,
  trial_id integer,
  poi_id integer,
  CONSTRAINT poi_id_fk FOREIGN KEY (poi_id)
      REFERENCES poi (poi_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT step_id_fk FOREIGN KEY (step_id)
      REFERENCES step (step_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT trial_id_fk FOREIGN KEY (trial_id)
      REFERENCES trial (trial_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE poi_step_trial
  OWNER TO toursims;
  
 
