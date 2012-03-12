<?php

// Establish the connection to the database
$connection = pg_connect("host=localhost port=5432 dbname=toursims user=toursims password=") or die('Could not connect: '.pg_last_error());

// Define and perform the SQL query
switch ($_REQUEST['action']) {
	case "get_messages":
		// List all the latest messages linked to the specified user
		// The user will be able to fetch the whole thread of messages later
		// Test: http://toursims.free.fr/message.php?action=get_messages&user_id=1
		$query = "
		-- Last message of each thread
		SELECT
			u.user_id,
			u.name,
			u.avatar,
			m.message_id,
			m.text,
			m.latitude,
			m.longitude,
			m.\"timestamp\",
			m.reply_message_id,
			uw.user_id AS writer_id,
			uw.name AS writer_name,
			uw.avatar AS writer_avatar,
			rmc.reply_message_count
		FROM
			\"user\" AS u,
			user_message AS um,
			message AS m,
			(SELECT
				m.reply_message_id,
				MAX(m.\"timestamp\") AS \"timestamp\"
			FROM
				message AS m
			WHERE
				m.reply_message_id IS NOT NULL
			GROUP BY
				m.reply_message_id) AS mm,
			\"user\" AS uw,
			user_message AS umw,
			(SELECT
				COUNT(*) + 1 AS reply_message_count
			FROM
				message AS m
			WHERE
				m.reply_message_id = m.reply_message_id) AS rmc
		WHERE
			u.user_id = um.user_id
			AND um.message_id = m.message_id
			AND m.reply_message_id = mm.reply_message_id
			AND m.\"timestamp\" = mm.\"timestamp\"
			AND umw.message_id = m.message_id
			AND umw.user_id = uw.user_id
			AND umw.is_writer = TRUE
			AND u.user_id = ".$_REQUEST['user_id']."

		UNION

		-- Messages which are the only one in their respective thread
		SELECT
			u.user_id,
			u.name,
			u.avatar,
			m.message_id,
			m.text,
			m.latitude,
			m.longitude,
			m.\"timestamp\",
			m.reply_message_id,
			uw.user_id AS writer_id,
			uw.name AS writer_name,
			uw.avatar AS writer_avatar,
			1 AS reply_message_count
		FROM
			\"user\" AS u,
			user_message AS um,
			message AS m,
			\"user\" AS uw,
			user_message AS umw
		WHERE
			u.user_id = um.user_id
			AND um.message_id = m.message_id
			AND m.reply_message_id IS NULL
			AND m.message_id NOT IN (
				SELECT
					m.reply_message_id
				FROM
					message AS m
				WHERE
					m.reply_message_id IS NOT NULL
				GROUP BY
					m.reply_message_id
			)
			AND umw.message_id = m.message_id
			AND umw.user_id = uw.user_id
			AND umw.is_writer = TRUE
			AND u.user_id = ".$_REQUEST['user_id']."
		ORDER BY \"timestamp\" DESC;
		";
		$result = pg_query($query) or die('Query failed: '.pg_last_error());
		
		break;
	case 'create_message':
		// Create a new message for the specified user
		// Test: http://localhost/message.php?action=create_message&text=HelloWorld&latitude=1&longitude=2&timestamp=12-12-2012&rdv_latitude=0&rdv_longitude=0&rdv_timestamp=12-12-2013&reply_message_id=null&writer_id=1&receiver_id=2
	
		// Create the message object
		$query = "
		INSERT INTO message (text, latitude, longitude, \"timestamp\", rdv_latitude, rdv_longitude, rdv_timestamp, reply_message_id) VALUES ('".$_REQUEST['text']."', ".$_REQUEST['latitude'].", ".$_REQUEST['longitude'].", '".$_REQUEST['timestamp']."', ".$_REQUEST['rdv_latitude'].", ".$_REQUEST['rdv_longitude'].", '".$_REQUEST['rdv_timestamp']."', ".$_REQUEST['reply_message_id'].") RETURNING message_id;
		";
		$result = pg_query($query) or die('Query failed: '.pg_last_error());
		
		$result = pg_fetch_row($result);
		$last_insert_id = $result[0];
		
		// Assign it newly created message to the current user (who is the writer)
		$query = "
		INSERT INTO user_message (user_id, message_id, is_writer) VALUES (".$_REQUEST['writer_id'].", ".$last_insert_id.", true);
		";
		$result = pg_query($query) or die('Query failed: '.pg_last_error());
		
		// Assign it to all the recievers
		$receiver_id_array = explode(",", $_REQUEST['receiver_id']);
		foreach($receiver_id_array as $receiver_id) {
			$query = "
			INSERT INTO user_message (user_id, message_id, is_writer) VALUES (".$receiver_id.", ".$last_insert_id.", false);
			";
			$result = pg_query($query) or die('Query failed: '.pg_last_error());
		}
	
		break;
	case "get_reply_messages":
		// Select all the messages from one given thread
		// Test : http://localhost/message.php?action=get_reply_messages&root_message_id=1
		$query = "
		-- Select all the messages from one given thread
		SELECT
			0 AS user_id,
			'0' AS name,
			0 AS avatar,
			m.message_id,
			m.text,
			m.latitude,
			m.longitude,
			m.\"timestamp\",
			m.reply_message_id,
			uw.user_id AS writer_id,
			uw.name AS writer_name,
			uw.avatar AS writer_avatar,
			0 AS reply_message_count
		FROM
			message AS m,
			\"user\" AS uw,
			user_message AS umw
		WHERE
			m.reply_message_id = ".$_REQUEST['root_message_id']."
			AND umw.message_id = m.message_id
			AND umw.user_id = uw.user_id
			AND umw.is_writer = TRUE

		UNION

		SELECT
			0 AS user_id,
			'0' AS name,
			0 AS avatar,
			m.message_id,
			m.text,
			m.latitude,
			m.longitude,
			m.\"timestamp\",
			m.reply_message_id,
			uw.user_id AS writer_id,
			uw.name AS writer_name,
			uw.avatar AS writer_avatar,
			0 AS reply_message_count
		FROM
			message AS m,
			\"user\" AS uw,
			user_message AS umw
		WHERE
			m.message_id = ".$_REQUEST['root_message_id']."
			AND umw.message_id = m.message_id
			AND umw.user_id = uw.user_id
			AND umw.is_writer = TRUE

		ORDER BY \"timestamp\" DESC;
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