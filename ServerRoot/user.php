<?php

// Establish the connection to the database
$connection = pg_connect("host=localhost port=5432 dbname=toursims user=postgres password=postgres") or die('Could not connect: '.pg_last_error());

// Define and perform the SQL queries
switch ($_REQUEST['action']) {
	case "authenticate":

		// Check if the user has already been registred in our service
		$query = "
		SELECT
			u.sso_name
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
			INSERT INTO preferences (share_position, is_guide) VALUES (false, false) RETURNING preferences_id;
			";
			$result = pg_query($query) or die('Query failed: '.pg_last_error());
			
			$result = pg_fetch_row($result);
			$last_insert_id = $result[0];
			
			$query = "
			INSERT INTO \"user\" (name, sso_id, preferences_id, sso_name) VALUES ('".$_REQUEST['name']."', '".$_REQUEST['sso_id']."', '".$last_insert_id."', '".$_REQUEST['sso_name']."');
			";
			$result = pg_query($query) or die('Query failed: '.pg_last_error());
		} else {
			// Retreive the existing user informations
			$query = "
			SELECT
				u.user_id,
				u.name,
				p.share_position,
				p.is_guide
			FROM
				\"user\" AS u,
				preferences AS p
			WHERE
				u.preferences_id = p.preferences_id;
			";
			$result = pg_query($query) or die('Query failed: '.pg_last_error());
		}
		break;
	default:
		die('Invalid action');
		break;
}

// Display the results in JSON
header('Content-Type: text/javascript');
$rows = array();
while($r = pg_fetch_assoc($result)) {
    $rows[] = $r;
}
print json_encode($rows);

// Free the resultset
pg_free_result($result);

// Close the connection
pg_close($connection);

?>