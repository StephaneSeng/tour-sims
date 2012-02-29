<?php

// Establish the connection to the database
$connection = pg_connect("host=localhost port=5432 dbname=toursims user=postgres password=postgres") or die('Could not connect: '.pg_last_error());

// Define and perform the SQL query
switch ($_REQUEST['action']) {
	case "get":
		// List all the cities known in the database
		// The results are ordered by their names
		$query = "
		SELECT
			c.name AS name,
			m.text AS image,
			c.city_id
		FROM
			city AS c
				LEFT OUTER JOIN
					city_course_poi_metadata AS ccpm
				ON
					c.city_id = ccpm.city_id
				LEFT OUTER JOIN
					metadata AS m
				ON
					ccpm.metadata_id = m.metadata_id
				LEFT OUTER JOIN
					metadata_tag AS mt
				ON
					m.metadata_tag_id = mt.metadata_tag_id
		WHERE
			mt.name = 'IMAGE'
			OR mt.name IS NULL
		ORDER BY name ASC;
		";
		$result = pg_query($query) or die('Query failed: '.pg_last_error());
		
		break;
	case "_get":
		// List all the cities known in the database ordered by their distance with the specified position
		// Distance forumla from: http://zcentric.com/2010/03/11/calculate-distance-in-mysql-with-latitude-and-longitude/
		$query = "
		SELECT
			c.name AS name,
			m.text AS image,
			((ACOS(SIN(".$_REQUEST['latitude']."*PI()/180)*SIN(latitude*PI()/180) + COS(".$_REQUEST['latitude']."*PI()/180)*COS(latitude*PI()/180)*COS((".$_REQUEST['longitude']."-longitude)*PI()/180))*180/PI())*60*1.1515) AS distance
		FROM
			city AS c
				LEFT OUTER JOIN
					city_course_poi_metadata AS ccpm
				ON
					c.city_id = ccpm.city_id
				LEFT OUTER JOIN
					metadata AS m
				ON
					ccpm.metadata_id = m.metadata_id
				LEFT OUTER JOIN
					metadata_tag AS mt
				ON
					m.metadata_tag_id = mt.metadata_tag_id
		WHERE
			mt.name = 'IMAGE'
			OR mt.name IS NULL
		ORDER BY distance ASC;
		";
		$result = pg_query($query) or die('Query failed: '.pg_last_error());
	
		break;
	default:
		// The requested action is invalid
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