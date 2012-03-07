-- Table: sso

INSERT INTO sso (sso_id, name) VALUES (1, 'Google');

-- Table: preferences

INSERT INTO preferences (share_position, is_guide) VALUES (true, true);

-- Table: message

INSERT INTO message (text, latitude, longitude, "timestamp", rdv_latitude, rdv_longitude, rdv_timestamp, reply_message_id) VALUES ('Hello World!', 45.781389, 4.872222, now(), 48.854444, 2.320833, now(), null);

-- Table: "user"

INSERT INTO "user" (name, avatar, preferences_id, sso_id, sso_name) VALUES ('Administrator', 'http://code.google.com/p/tour-sims/logo?cct=1330333365', 1, 1, 'administrator@toursims.com');

-- Table: checkin

INSERT INTO checkin (latitude, longitude, "timestamp", user_id) VALUES (45.767628, 4.83499, now(), 1);

-- Table: user_user

--

-- Table: user_message

INSERT INTO user_message (user_id, message_id, is_writer) VALUES (1, 1, true);
