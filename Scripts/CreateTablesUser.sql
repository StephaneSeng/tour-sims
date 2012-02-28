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
  OWNER TO postgres;

-- Table: preferences

-- DROP TABLE preferences;

CREATE TABLE preferences
(
  preferences_id integer NOT NULL,
  share_position boolean,
  is_guide boolean,
  CONSTRAINT preferences_pk PRIMARY KEY (preferences_id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE preferences
  OWNER TO postgres;

-- Table: "user"

-- DROP TABLE "user";

CREATE TABLE "user"
(
  user_id integer NOT NULL,
  name character varying,
  sso_id integer,
  preferences_id integer,
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
  OWNER TO postgres;

-- Table: checkin

-- DROP TABLE checkin;

CREATE TABLE checkin
(
  checkin_id integer NOT NULL,
  latitude double precision,
  longitude double precision,
  date date,
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
  OWNER TO postgres;
  
-- Table: user_user

-- DROP TABLE user_user;

CREATE TABLE user_user
(
  user_a_id integer NOT NULL,
  user_b_id integer NOT NULL,
  CONSTRAINT user_user_pk PRIMARY KEY (user_a_id , user_b_id ),
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
  OWNER TO postgres;
