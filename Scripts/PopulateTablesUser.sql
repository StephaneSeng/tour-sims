-- Table: sso

INSERT INTO sso (sso_id, name) VALUES (1, 'Google');

-- Table: preferences

INSERT INTO preferences (share_position, is_guide) VALUES (false, false);

-- Table: "user"

INSERT INTO "user" (name, sso_id, preferences_id, sso_name) VALUES ('Administrator', 1, 1, 'administrator@toursims.com');

-- Table: checkin

--

-- Table: user_user

--
