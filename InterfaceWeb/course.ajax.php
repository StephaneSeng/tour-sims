<?php 
	$req=$_GET["req"];
	if ($req == "list") {
		$dir = "kml/"; 
		$handle = opendir($dir); 

		# Making an array containing the files in the current directory: 
		while ($file = readdir($handle)) 
		{ 
		    $files[] = $file; 
		} 
		closedir($handle); 

		#echo the files 
		foreach ($files as $file) { 
    		if($file != "." && $file != ".."){
				echo "<a href=$dir$file>$file</a>"."<br />"; 
			}
		} 
	}elseif ($req=="create") {
		$response = "<div class=\"background3\">"
		. "<div style=\"width: 42%; height:150px; margin: 40px; float: left; background-color:#ffffff;\">"
		. "<table><tr><td width= \"5%\">Name </td>     " 
		. "<td width= \"20%\"><input id=\"courseName\" name=\"courseName\" value=\"\" type=\"textbox\"></td>"
		. "<td width = \"5%\">Description</td>"
		. "<td width= \"20%\"><input id=\"courseDesc\" name=\"courseDesc\" value=\"\" type=\"textbox\"></td></tr>"
		. "<tr><td width= \"5%\">Time  </td>"     
		. "<td width= \"20%\"><input id=\"courseTime\" name=\"courseTime\" value=\"\" type=\"textbox\"></td>"
		. "<td width = \"5%\">City </td>"       
		. "<td width= \"20%\"><input id=\"courseCity\" name=\"courseCity\" value=\"\" type=\"textbox\"></td></tr>"
		. "<tr> <td width= \"5%\">Image URL</td>"
		. "<td width= \"20%\"><input id=\"courseImg\" name=\"courseImg\" value=\"\" type=\"textbox\" onchange=\"loadImg()\"></td>"
		. "<td><img src=\"\" alt=\"Picture\" height=\"220\" width=\"220\" id=\"picture\" border=\"1\" />" 
		. "<input type=\"hidden\" name=\"courseData\" id=\"courseData\">"
		. "</table>"
		. "</div>" 
		. "<div style=\"margin: 40px; float: left;background-color:#ffffff\">"
		. "<input value=\"Save\" onclick=\"saveCourse()\" type=\"button\"></br>"
		. "<input value=\"Clear\" onclick=\"clearOverlays()\" type=\"button\"></br>"
		. "<input value=\"Cancel\" onclick=\"cancelCreation()\" type=\"button\"></br>"
		. "</div>" 
		. "<div style=\"width: 20%; margin:40px; float: right;background-color:#ffffff\">"
		. "<input id=\"address\" value=\"Search your city\" type=\"textbox\" onfocus=\"value=''\">"
		. "<input value=\"Search\" onclick=\"searchAddress()\" type=\"button\">"
		. "</div>"
		. "</div>";
		echo $response;
	}elseif ($req=="save"){
		$courseData = $_GET['courseData'];
		$typeCourse = $_GET['typeCourse'];
		//echo $courseData;
		$data = explode(";;", $courseData);
		$i = 0;
		// Creates the Document.
		$dom = new DOMDocument('1.0', 'UTF-8');
	
		// Creates the root KML element and appends it to the root document.
		$node = $dom->createElementNS('http://www.opengis.net/kml/2.2', 'kml');
		$parNode = $dom->appendChild($node);
	
		// Creates a KML Document element and append it to the KML element.
		$dnode = $dom->createElement('Document');
		$docNode = $parNode->appendChild($dnode);
	
		$dName = $dom->createElement('name',$data[0]);
		$dExtData = $dom->createElement('ExtendedData');
		
		$dData = $dom->createElement('Data');
		$dAttribute = $dom->createAttribute('name');
		$dAttribute->value = 'city';
		$dValue = $dom->createElement('value',$data[1]);
		$dData->appendChild($dValue);
		$dData->appendChild($dAttribute);
		$dExtData->appendChild($dData);
		
		$dData = $dom->createElement('Data');
		$dAttribute = $dom->createAttribute('name');
		$dAttribute->value = 'picture';
		if ($data[4]=="") {
			$data[4]="No image";
		}
		$dValue = $dom->createElement('value',$data[4]);
		$dData->appendChild($dValue);
		$dData->appendChild($dAttribute);
		$dExtData->appendChild($dData);
		
		$dData = $dom->createElement('Data');
		$dAttribute = $dom->createAttribute('name');
		$dAttribute->value = 'text';
		$dValue = $dom->createElement('value',$data[2]);
		$dData->appendChild($dValue);
		$dData->appendChild($dAttribute);
		$dExtData->appendChild($dData);
		
		$dData = $dom->createElement('Data');
		$dAttribute = $dom->createAttribute('name');
		$dAttribute->value = 'rating';
		$dValue = $dom->createElement('value','0');
		$dData->appendChild($dValue);
		$dData->appendChild($dAttribute);
		$dExtData->appendChild($dData);

		$dData = $dom->createElement('Data');
		$dAttribute = $dom->createAttribute('name');
		$dAttribute->value = 'duree';
		$dValue = $dom->createElement('value',$data[3]);
		$dData->appendChild($dValue);
		$dData->appendChild($dAttribute);
		$dExtData->appendChild($dData);
		
		if ($typeCourse == 'game') {
			$dData = $dom->createElement('Data');
			$dAttribute = $dom->createAttribute('name');
			$dAttribute->value = 'type';
			$dValue = $dom->createElement('value','game');
			$dData->appendChild($dValue);
			$dData->appendChild($dAttribute);
			$dExtData->appendChild($dData);
			
		}
		
		$docNode->appendChild($dName);
		$docNode->appendChild($dExtData);
	
		for	($i = 5; ($i < count($data)-1); $i++) {
			$pNode = $dom->createElement('Placemark');
			$placeNode = $docNode->appendChild($pNode);
			$placeData = explode(";",$data[$i]);
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
			if (count($placeData)>5){
				$pQuestions = $dom->createElement('Questions');
				for ($j = 5;$j < count($placeData)-1;$j=$j+3){
					$pQuestion = $dom->createElement('Question');
					$pType = $dom->createAttribute('type');
					if ($placeData[$j]=='Exact') {
						$pType->value= 'exact';
					}else{
						$pType->value= 'multiple-choice';
					}
					$pQuestion->appendChild($pType);
					$pTitle = $dom->createElement('title',$placeData[$j+1]);
					$pQuestion->appendChild($pTitle);
					$pAnswers = $dom->createElement('Answers');
					if ($placeData[$j]=='Exact') {
						$pAnswer = $dom->createElement('Answer');
						$pValue = $dom->createElement('value',$placeData[$j+2]);
						$pAnswer->appendChild($pValue);
						$pAnswers->appendChild($pAnswer);
					}else{
						$allAnswers = explode("::",$placeData[$j+2]);
						for ($k = 0; $k < count($allAnswers); $k++) {
							$pAnswer = $dom->createElement('Answer');
							if ($k==0) {
								$pTrue = $dom->createElement('isTrue','true');
								$pAnswer->appendChild($pTrue);
							}else{
								$pTrue = $dom->createElement('isTrue','false');
								$pAnswer->appendChild($pTrue);
							}
							$pValue = $dom->createElement('value',$allAnswers[$k]);
							$pAnswer->appendChild($pValue);
							$pAnswers->appendChild($pAnswer);
						}
					}
					$pQuestion->appendChild($pAnswers);
					$pQuestions->appendChild($pQuestion);
				}
				$pPoint->appendChild($pQuestions);
			}
		}
		
		$fileName = str_replace(" ","_",$data[0]);
		$kmlOutput = $dom->save("kml/".$fileName.".kml");
		$listPOIhtml = " <h3>Your course</h3>"
						. "<ul>";
		$dir = "kml/"; 
		$handle = opendir($dir); 
		# Making an array containing the files in the current directory: 
		while ($file = readdir($handle)) 
		{ 
		    $files[] = $file; 
		} 
		closedir($handle); 
		
		function parserKML($fileKML) {
			// Create a new DOM model
			$doc = new DOMDocument();
			// Load a KML file
			$doc->load($fileKML);
  			
			$courseNames = $doc->getElementsByTagName("name");
			$courseName = $courseNames->item(0)->nodeValue;
			$htmlReturn = $courseName;
			
			$datas = $doc->getElementsByTagName("Data");
			foreach ($datas as $data){
				$dName = $data->getAttribute('name');
				if ($dName == 'city') {
					$value = $data->getElementsByTagName('value')->item(0)->nodeValue;
					$htmlReturn = $htmlReturn. '<ul> <li>City: '.$value.'</li>';
				}
				if ($dName == 'text') {
					$value = $data->getElementsByTagName('value')->item(0)->nodeValue;
					$htmlReturn = $htmlReturn. '<li>Description: '.$value.'</li>';
				}
				if ($dName == 'type') {
					$value = $data->getElementsByTagName('value')->item(0)->nodeValue;
					if ($value == 'game'){
						$htmlReturn = $htmlReturn. '<li>More : It\'s a game </li>';
					}
				}
			}
			$htmlReturn = $htmlReturn. '</ul>';
			return $htmlReturn;
		}
		
		#echo the files 
		foreach ($files as $file) { 
    		if($file != "." && $file != ".."){
					$list = $list."<li><span>".parserKML($dir.$file)."</span></li>"; 
   			}
		} 
		$listPOIhtml = $listPOIhtml .$list."</ul>";
		echo $listPOIhtml;
		$connection = pg_connect("host=localhost port=5432 dbname=toursims user=toursims password=smisruot") or die('Could not connect: '.pg_last_error());
		$query = "
		INSERT INTO course (name,description,difficulty,file,\"timestamp\",user_id,city_id) VALUES ('name','desc',1,'http://toursims.free.fr/kml/".$fileName.".kml',now(),1,1);";
		$result = pg_query($query) or die('Query failed: '.pg_last_error());
	}

?>