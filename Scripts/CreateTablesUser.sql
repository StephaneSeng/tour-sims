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
