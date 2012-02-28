-- Table: step

-- DROP TABLE step;

CREATE TABLE step
(
  step_id integer NOT NULL,
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
  OWNER TO postgres;

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
  OWNER TO postgres;

-- Table: trial

-- DROP TABLE trial;

CREATE TABLE trial
(
  trial_id integer NOT NULL,
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
  OWNER TO postgres;

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
  OWNER TO postgres;

-- Table: poi_trial

-- DROP TABLE poi_trial;

CREATE TABLE poi_trial
(
  poi_id integer NOT NULL,
  trial_id integer NOT NULL,
  CONSTRAINT poi_trial_pk PRIMARY KEY (poi_id , trial_id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE poi_trial
  OWNER TO postgres;
