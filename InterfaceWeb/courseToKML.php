
    <?php
    $courseData = $_POST['lat'];
	//echo $courseData;
	$data = split(";;", $courseData);
	$i = 0;
	// Creates the Document.
	$dom = new DOMDocument('1.0', 'UTF-8');

	// Creates the root KML element and appends it to the root document.
	$node = $dom->createElementNS('http://earth.google.com/kml/2.1', 'kml');
	$parNode = $dom->appendChild($node);

	// Creates a KML Document element and append it to the KML element.
	$dnode = $dom->createElement('Document');
	$docNode = $parNode->appendChild($dnode);

	$dName = $dom->createElement('name',$data[0]);
	$dPresentation = $dom->createElement('presentation',$data[1]);
	$docNode->appendChild($dName);
	$docNode->appendChild($dPresentation);

	for	($i = 2; ($i < count($data)-1); $i++) {
		$pNode = $dom->createElement('Placemark');
		$placeNode = $docNode->appendChild($pNode);
		$placeData = split(";",$data[$i]);
		$pName = $dom->createElement('name',$placeData[0]);
		$placeNode->appendChild($pName);
		$pDesc = $dom->createElement('description',$placeData[1]);
		$placeNode->appendChild($pDesc);
		$pDir = $dom->createElement('direction',$placeData[2]);
		$placeNode->appendChild($pDir);
		$pDir = $dom->createElement('LookAt');
		$pLook = $placeNode->appendChild($pDir);
		$pLng = $dom->createElement('longitude',$placeData[3]);
		$pLook->appendChild($pLng);
		$pLat = $dom->createElement('latitude',$placeData[4]);
		$pLook->appendChild($pLat);
		$pDir = $dom->createElement('Point');
		$pPoint = $placeNode->appendChild($pDir);
		$pAltMode = $dom->createElement('altitudeMode','clampToGround');
		$pPoint->appendChild($pAltMode);
		$pLat = $dom->createElement('coordinates',$placeData[3].','.$placeData[4]);
		$pPoint->appendChild($pLat);
	}
	

	$kmlOutput = $dom->save("course.kml");
	
	echo 'Course created';

    ?>
