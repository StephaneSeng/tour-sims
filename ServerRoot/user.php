<?php

// Establish the connection to the database
$connection = pg_connect("host=localhost port=5432 dbname=toursims user=toursims password=") or die('Could not connect: '.pg_last_error());

// Define and perform the SQL queries
switch ($_REQUEST['action']) {
	case "authenticate":
		// Check if the user has already been registred in our service
		// Test: http://toursims.free.fr/user.php?action=authenticate&name=St%C3%A9phane%20Seng&avatar=https%3A%2F%2Flh5.googleusercontent.com%2F-D6UO0_G4D2M%2FAAAAAAAAAAI%2FAAAAAAAAAAA%2FXHg1v57fjBU%2Fphoto.jpg%3Fsz%3D50&sso_id=1&sso_name=107988237770371698580
		$query = "
		SELECT
			u.user_id
		FROM
			\"user\" AS u,
			sso AS s
		WHERE
			u.sso_id = s.sso_id
			AND u.sso_name = '".$_REQUEST['sso_name']."'
			AND s.sso_id = ".$_REQUEST['sso_id'].";
		";
		$result = pg_query($query) or die('Query failed: '.pg_last_error());

		if (pg_num_rows($result) == 0) {
			// This is a new user, we have to create him
			$query = "
			INSERT INTO preferences (share_position, is_guide) VALUES (true, true) RETURNING preferences_id;
			";
			$result = pg_query($query) or die('Query failed: '.pg_last_error());
			
			$result = pg_fetch_row($result);
			$last_insert_id = $result[0];
			
			$query = "
			INSERT INTO \"user\" (name, avatar, preferences_id, sso_id, sso_name) VALUES ('".$_REQUEST['name']."', '".$_REQUEST['avatar']."', '".$last_insert_id."', '".$_REQUEST['sso_id']."', '".$_REQUEST['sso_name']."');
			";
			$result = pg_query($query) or die('Query failed: '.pg_last_error());
			
			// Retreive the newly existing user informations
			$query = "
			SELECT
				u.user_id,
				u.name,
				u.avatar,
				p.share_position,
				p.is_guide
			FROM
				\"user\" AS u,
				preferences AS p
			WHERE
				u.preferences_id = p.preferences_id
				AND u.sso_name = '".$_REQUEST['sso_name']."';
			";
			$result = pg_query($query) or die('Query failed: '.pg_last_error());
		} else {
			// Retreive the existing user informations
			$query = "
			SELECT
				u.user_id,
				u.name,
				u.avatar,
				p.share_position,
				p.is_guide
			FROM
				\"user\" AS u,
				preferences AS p
			WHERE
				u.preferences_id = p.preferences_id
				AND u.sso_name = '".$_REQUEST['sso_name']."';
			";
			$result = pg_query($query) or die('Query failed: '.pg_last_error());
		}
		
		break;
	case "checkin":
		// Create a checkin for the current user
		// Test: http://toursims.free.fr/user.php?action=checkin&latitude=48.870278&longitude=2.316389&timestamp=2007-05-06%2020%3A00%3A00&user_id=1
		$query = "
		INSERT INTO checkin (latitude, longitude, \"timestamp\", user_id) VALUES (".$_REQUEST['latitude'].", ".$_REQUEST['longitude'].", '".$_REQUEST['timestamp']."', ".$_REQUEST['user_id'].");
		";
		$result = pg_query($query) or die('Query failed: '.pg_last_error());
		
		break;
	case "get_nearby_checkins":
		// Retreive the checkins made by the other users near the current location
		// Test: http://toursims.free.fr/user.php?action=get_nearby_checkins&latitude=48.870278&longitude=2.316389&user_id=2
		$query = "
		SELECT
			u.user_id,
			u.name,
			u.avatar,
			c.latitude,
			c.longitude,
			c.\"timestamp\",
			((ACOS(SIN(".$_REQUEST['latitude']."*PI()/180)*SIN(latitude*PI()/180) + COS(".$_REQUEST['latitude']."*PI()/180)*COS(latitude*PI()/180)*COS((".$_REQUEST['longitude']."-longitude)*PI()/180))*180/PI())*60*1.1515) AS distance
		FROM
			\"user\" AS u,
			preferences AS p,
			checkin AS c
		WHERE
			u.preferences_id = p.preferences_id
			AND u.user_id = c.user_id
			AND p.share_position = true
			AND u.user_id != ".$_REQUEST['user_id']."
			AND c.\"timestamp\" = (
				SELECT
					MAX(c.\"timestamp\")
				FROM
					checkin AS c
				WHERE
					u.user_id = c.user_id)
		ORDER BY distance ASC;
		";
		$result = pg_query($query) or die('Query failed: '.pg_last_error());
		
		break;
	case "get_user_checkins":
		// Retreive the checkins made by one user
		// Test: http://toursims.free.fr/user.php?action=get_user_checkins&user_id=1
		$query = "
		SELECT
			c.latitude,
			c.longitude,
			c.\"timestamp\"
		FROM
			\"user\" AS u,
			preferences AS p,
			checkin AS c
		WHERE
			u.preferences_id = p.preferences_id
			AND u.user_id = c.user_id
			AND p.share_position = true
			AND u.user_id != ".$_REQUEST['user_id'].";
		";
		$result = pg_query($query) or die('Query failed: '.pg_last_error());
		
		break;
	case "get_contacts":
		// List all the contacts linked to the specified user
		// Test: http://toursims.free.fr/user.php?action=get_contacts&user_id=1
		$query = "
		-- The current user is user_a
		SELECT
			uu.user_b_id AS user_id,
			u.name,
			u.avatar
		FROM
			user_user AS uu,
			\"user\" AS u
		WHERE
			uu.user_b_id = u.user_id
			AND uu.user_a_id = ".$_REQUEST['user_id'].";
		";
		$result = pg_query($query) or die('Query failed: '.pg_last_error());
		
		break;
	case "add_contact":
		// Add a contact to the specified user
		// Test: http://toursims.free.fr/user.php?action=add_contact&user_id=1&contact_id=2
		$query = "
		INSERT INTO user_user (user_a_id, user_b_id) VALUES (".$_REQUEST['user_id'].", ".$_REQUEST['contact_id'].");
		";
		$result = pg_query($query) or die('Query failed: '.pg_last_error());
		
		break;
	case "get_profile":
		// Retreive the shared informations about the specified user
		// Test: http://toursims.free.fr/user.php?action=get_profile&user_id=1
		$query = "
		SELECT
			u.user_id,
			u.name,
			u.avatar,
			p.share_position,
			p.is_guide
		FROM
			\"user\" AS u,
			preferences AS p
		WHERE
			u.preferences_id = p.preferences_id
			AND u.user_id = ".$_REQUEST['user_id'].";
		";
		$result = pg_query($query) or die('Query failed: '.pg_last_error());
		
		break;
	default:
		// The requested action is invalid
		die('Invalid action');
		
		break;
}

// Display the results in JSON
include_once('JSON.php');
$json = new Services_JSON();
header('Content-Type: text/javascript');
$rows = array();
while($r = pg_fetch_assoc($result)) {
    $rows[] = $r;
}
print $json->encode($rows);

// Free the resultset
pg_free_result($result);

// Close the connection
pg_close($connection);

?>