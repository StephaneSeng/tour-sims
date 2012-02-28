<?php

// Establish the connection to the database
$connection = pg_connect("host=localhost port=5432 dbname=toursims user=postgres password=postgres") or die('Could not connect: '.pg_last_error());

// Perform the SQL query
$query = "
SELECT
	c.name,
	c.file,
	r.rating
FROM
	course AS c
LEFT OUTER JOIN
	rating AS r
ON
	c.course_id = r.course_id;
";
$result = pg_query($query) or die('Query failed: '.pg_last_error());

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