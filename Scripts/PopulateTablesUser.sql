-- Table: sso

INSERT INTO sso (sso_id, name) VALUES (1, 'Google');

-- Table: preferences

INSERT INTO preferences (preferences_id, share_position, is_guide) VALUES (1, false, false);

-- Table: "user"

INSERT INTO "user" (user_id, name, sso_id, preferences_id) VALUES (1, 'Administrator', 1, 1);

-- Table: checkin

--

-- Table: user_user

--
