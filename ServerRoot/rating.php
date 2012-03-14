<?php

// Establish the connection to the database
$connection = pg_connect("host=localhost port=5432 dbname=toursims user=toursims password=") or die('Could not connect: '.pg_last_error());

// Define and perform the SQL query
switch ($_REQUEST['action']) {
	case "get_course_rating":
		// Retreive the ratings linked to the specified course
		// Test: http://toursims.free.fr/rating.php?action=get_course_rating&course_id=1
		$query = "
		SELECT
			AVG(r.rating) AS rating
		FROM
			course AS c
				LEFT OUTER JOIN
					rating AS r
				ON
					c.course_id = r.course_id
		WHERE
			r.course_id = ".$_REQUEST['course_id'].";
		";
		$result = pg_query($query) or die('Query failed: '.pg_last_error());
		
		break;
	case 'create_course_rating':
		// Create a new rating for the specified course
		// Test: http://toursims.free.fr/rating.php?action=create_course_rating&rating=5&user_id=1&course_id=1
		// Check if the user has already given a rating about this course
		$query = "
		SELECT
			r.rating
		FROM
			rating AS r
		WHERE
			r.user_id = ".$_REQUEST['user_id'].";
		";
		$result = pg_query($query) or die('Query failed: '.pg_last_error());
		
		// Create the rating object if it does not yet exist, else update it
		if (pg_num_rows($result) == 0) {
			$query = "
			INSERT INTO rating (rating, user_id, course_id) VALUES (".$_REQUEST['rating'].", ".$_REQUEST['user_id'].", ".$_REQUEST['course_id'].");
			";
			$result = pg_query($query) or die('Query failed: '.pg_last_error());
		} else {
			$query = "
			UPDATE
				rating
			SET
				rating = ".$_REQUEST['rating']."
			WHERE
				course_id = ".$_REQUEST['course_id']."
				AND user_id = ".$_REQUEST['user_id'].";
			";
			$result = pg_query($query) or die('Query failed: '.pg_last_error());
		}
		
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