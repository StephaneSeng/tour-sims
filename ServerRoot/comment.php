<?php

// Establish the connection to the database
$connection = pg_connect("host=localhost port=5432 dbname=toursims user=toursims password=") or die('Could not connect: '.pg_last_error());

// Define and perform the SQL query
switch ($_REQUEST['action']) {
	case "get_course_comments":
		// List all the comments linked to the specified course
		// Test: http://toursims.free.fr/comment.php?action=get_course_comments&course_id=1
		$query = "
		SELECT
			c.comment_id,
			c.text,
			c.\"timestamp\",
			c.user_id,
			u.name AS user_name,
			u.avatar AS user_avatar
		FROM
			comment AS c,
			\"user\" AS u
		WHERE
			c.user_id = u.user_id
			AND c.course_id = ".$_REQUEST['course_id'].";
		";
		$result = pg_query($query) or die('Query failed: '.pg_last_error());
		
		break;
	case 'create_course_comment':
		// Create a new comment for the specified course
		// Test: http://toursims.free.fr/comment.php?action=create_course_comment&text=HelloWorld&timestamp=12-12-2012&user_id=1&course_id=1
	
		// Create the comment object
		$query = "
		INSERT INTO comment (text, \"timestamp\", user_id, course_id) VALUES ('".$_REQUEST['text']."', now(), ".$_REQUEST['user_id'].", ".$_REQUEST['course_id'].");
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