<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta content="text/html; charset=ISO-8859-1" http-equiv="content-type">
		<meta content="initial-scale=1.0, user-scalable=no" name="viewport">
		<style type="text/css">
			html { height: 100% }
			body { height: 100%; margin: 0; padding: 0 }
			#map_canvas { height: 100% }
		</style>
		<link rel="stylesheet" href="css/style.css" type="text/css" charset="utf-8" />	
		<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=AIzaSyBPu7IEDMsGOR9ShaWCLCCIt1YqDLIQ8zk&sensor=true"> </script>
		<script type="text/javascript">
		var geocoder;
		var map;
		var markerArray = [];
		var directionDisplay;
		var directionService = new google.maps.DirectionsService();
		var infowindowArray = [];
		var name;
		var names = [];
		var descriptions = [];
		var directions = [];
		var questions = [];
		function initialize() {
			geocoder = new google.maps.Geocoder();
			directionDisplay = new google.maps.DirectionsRenderer();
			directionDisplay.suppressMarkers = true;
			var myOptions = {
				center: new google.maps.LatLng(45.774043,4.835659),
				zoom: 13,
				mapTypeId: google.maps.MapTypeId.ROADMAP
			};
			map = new google.maps.Map(document.getElementById("map_canvas"),myOptions);
			google.maps.event.addListener(map, 'dragend', function() {
				getCity();
			});
			directionDisplay.setMap(map);
			getCity();
		}

		function searchAddress() {
			var address = document.getElementById("address").value;
			geocoder.geocode( { 'address': address}, function(results, status) {
				if (status == google.maps.GeocoderStatus.OK) {
					map.setCenter(results[0].geometry.location);
					getCity();
				} else {
					alert("Address not found");
				}
			});
		}
		function placeMarker(location) {
			var marker = new google.maps.Marker({
				position: location, 
				map: map, 
				draggable :true,
				clickable :true, 
			});
			markerArray.push(marker);
			//if(markerArray.length >1)
				//drawRoute();
			google.maps.event.addListener(marker, "dragend", function() {
				//if(markerArray.length >1)
					//drawRoute();
			});
			var html = "<table>" +
            "<tr><td>Name:</td> <td><input type='text' id='name"+markerArray.indexOf(marker)+"' value ='' /></td> </tr>" +
            "<tr><td>Description:</td> <td><input type='text' id='description"+markerArray.indexOf(marker)+"'/></td> </tr>" +
            "<tr><td>Direction:</td> <td><input type='text' id='direction"+markerArray.indexOf(marker)+"'/></td> </tr></table>" +
            "<div id='question"+markerArray.indexOf(marker)+"'></div><input type ='hidden' name ='nbQuestion"+markerArray.indexOf(marker)+"' id='nbQuestion"+markerArray.indexOf(marker)+"' value='0'><input type='button' value='Add question' id='question"+markerArray.indexOf(marker)+"' onclick='addQuestion("+ markerArray.indexOf(marker) + ")'/>" +
            "<input type='button' value='Save' onclick='saveMarkerInfo("+ markerArray.indexOf(marker) + ")'/>" ;
			var infowindow = new google.maps.InfoWindow({
				  content: html
				});
			infowindowArray.push(infowindow);
			names.push('');
			descriptions.push('');
			directions.push('');
		    google.maps.event.addListener(marker, "click", function() {
		    	  infowindow.open(map, marker);
		    });

		
			google.maps.event.addListener(marker, "rightclick", function() {
				marker.setMap(null);
				markerArray[markerArray.indexOf(marker)]=null;
				//if(markerArray.length >1)
					//drawRoute();
				names[markerArray.indexOf(marker)]='';
				descriptions[markerArray.indexOf(marker)]='';
				directions[markerArray.indexOf(marker)]='';
				infowindowArray[markerArray.indexOf(marker)]=null;
				loadListPOI();
			});
			names[markerArray.indexOf(marker)] = document.getElementById('name').value;
			return marker;
		}
		
		function drawRoute() {
			if(directionDisplay != null) {
				directionDisplay.setMap(map);
			}
			var i = 0;
			var waypts =[];
			var start = markerArray[0].getPosition();
			var end = markerArray[markerArray.length-1].getPosition();
			for(i=1;i<markerArray.length-1;i++){
				waypts.push({location: markerArray[i].getPosition()});
			}
			var request = {
				origin:start,
				destination:end,
				waypoints:waypts,
				optimizeWaypoints:true,
				avoidTolls:true,
				travelMode: google.maps.DirectionsTravelMode.WALKING
			};
			directionService.route(request, function(response, status) {
				if (status == google.maps.DirectionsStatus.OK) {
					//directionDisplay.setDirections(response);
				}
			});
		}

		function saveCourse() {
			var data = "";
			var typeCourse="normal";
			data += escape(document.getElementById("courseName").value);
		    data += ";;";
			data += document.getElementById("courseCity").value;
		    data += ";;";
			data += document.getElementById("courseDesc").value;
		    data += ";;";
			data += document.getElementById("courseTime").value;
		    data += ";;";
			data += document.getElementById("courseImg").value;
		    data += ";;";
			for (i = 0; (i < markerArray.length); i++) {
				if (markerArray[i] != null){
				    data += names[i];
				    data += ";";
				    data += descriptions[i];
				    data += ";";
				    data += directions[i];
				    data += ";";
					data += markerArray[i].getPosition().lng();
					data += ";";
					data += markerArray[i].getPosition().lat();
				 	if(questions.length >0){
				 		typeCourse = "game";
				      	for(j=0;(j<questions.length);j++){
				      		if (questions[j].marker == i){
								data += ";";
								data +=questions[j].type;
								data += ";";
								data +=questions[j].quest;
							    data += ";";
								data +=questions[j].ans;
				      		}
				  		}
				  	}
				    data += ";;";
				}
			 }
			var xmlhttp, listPOIhtml;
			if (window.XMLHttpRequest)
			  {// code for IE7+, Firefox, Chrome, Opera, Safari
			  xmlhttp=new XMLHttpRequest();
			  }
			else
			  {// code for IE6, IE5
			  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			  }
			xmlhttp.onreadystatechange=function()
			  {
			  if (xmlhttp.readyState==4 && xmlhttp.status==200)
			    {				    
			    	document.getElementById("infoCourse").innerHTML="<div class=\"background2\">"
    					+ "<button onclick=\"createNewCourse()\">Create new course</button>"
    					+ "</div>";
			    	document.getElementById("listPanel").innerHTML= xmlhttp.responseText;
			    }
			  }
			xmlhttp.open("GET","course.ajax.php?req=save&courseData="+data+"&typeCourse="+typeCourse,true);
			xmlhttp.send();
			clearOverlays();
		}
			
		function clearOverlays() {
			directionDisplay.setMap(null);
			directionDisplay.setPanel(null);
			for (i in markerArray){
				if (markerArray[i] != null)
					markerArray[i].setMap(null);
			}
			markerArray.splice(0);
			names = [];
			directions = [];
			descriptions=[];
			infowindowArray = [];
		    document.getElementById('listPanel').innerHTML="";
		}

		function question() {
		    this.marker=null;
		    this.type=null;
		    this.quest=null;
		    this.ans=null;
	    }
	    
		function saveMarkerInfo(indexMarker){
		    var nbQuestion = document.getElementById('nbQuestion'+indexMarker).value;
		    var i = 0;
		    for (i=0; i<nbQuestion;i++){
			    aQuestion = new question();
			    aQuestion.marker = indexMarker;
			    var index = i +1;
			    aQuestion.type = document.getElementById('marker'+indexMarker+'type'+index).value;
			    aQuestion.quest = document.getElementById('marker'+indexMarker+'question'+index).value;
			    aQuestion.ans = document.getElementById('marker'+indexMarker+'answer'+index).value;
			    if(aQuestion.type!='Exact'){
				    aQuestion.ans = aQuestion.ans + "::" + document.getElementById('marker'+indexMarker+'answer'+index+'choice0').value
				    + "::" + document.getElementById('marker'+indexMarker+'answer'+index+'choice1').value
				    + "::" + document.getElementById('marker'+indexMarker+'answer'+index+'choice2').value;
			    }
			    questions.push(aQuestion);
		    }

			var elementIdName = "name"+indexMarker;
			var elementIdDesc = "description"+indexMarker;
			var elementIdDir = "direction"+indexMarker;
			name = document.getElementById(elementIdName).value;
			var desc = document.getElementById(elementIdDesc).value;
			var direction = document.getElementById(elementIdDir).value;
			if (names.length == 0) {
				names.push(name);
				descriptions.push(desc);
				directions.push(direction);
			}else if (indexMarker < names.length){
				names[indexMarker]= name;
				descriptions[indexMarker]=desc;
				directions[indexMarker]=direction;
			}else {
				names.push(name);
				descriptions.push(desc);
				directions.push(direction);
			}
			google.maps.event.clearListeners(markerArray[indexMarker],'click');
			var html = "<table>" +
            "<tr><td>Name:</td> <td><input type='text' id='name"+indexMarker+"' value ='"+ names[indexMarker] +"' /></td> </tr>" +
            "<tr><td>Description:</td> <td><input type='text' id='description"+indexMarker+"' value ='"+ descriptions[indexMarker] +"'/></td> </tr>" +
            "<tr><td>Direction:</td> <td><input type='text' id='direction"+indexMarker+"' value ='"+ directions[indexMarker] +"'/></td> </tr>" +
            "<tr><td><input type='button' value='Save' onclick='saveMarkerInfo("+ indexMarker + ")'/></td><td></td></tr>" ;
			var infowindow = infowindowArray[indexMarker];
			infowindow.setContent(html);
		    google.maps.event.addListener(markerArray[indexMarker], "click", function() {
		    	  infowindow.open(map, markerArray[indexMarker]);
		    });
			loadListPOI();
		}

		function loadListPOI()
		{
		    txt="<ul>";
		    for (i=0;i<names.length;i++)
		      {
				if (markerArray[i] != null){
			      xx="Name: "+names[i]+"</br>Description: " + descriptions[i];
			      if(questions.length >0){
				      for(j=0;j<questions.length;j++){
					      if (questions[j].marker == i){
						      	xx = xx + "</br>Question <ul>";
								xx = xx + "<li>Type: " + questions[j].type + "</li>";
								xx = xx + "<li>Q: " + questions[j].quest + "</li>";
								if (questions[j].type == 'Exact'){
									xx = xx + "<li>A: " + questions[j].ans + "</li>";
								}else{
									var answers = questions[j].ans.split('::');
									xx = xx + "<li>Right A: " + answers[0] + "</li>";
									for (k=1;k<answers.length;k++){
										xx = xx + "<li>A: " + answers[k] + "</li>";
									}
								}
								xx = xx + "</ul>";
					      }
					  }
				  }
			      
			        {
			        try
			          {
			          txt=txt + "<li>" + xx + "</li>";
			          }
			        catch (er)
			          {
			          txt=txt + "<li>&nbsp;</li>";
			          }
			        }
				}
		    }
		    txt=txt + "</ul>";
		    document.getElementById('listPanel').innerHTML=txt;
		}
		
		function getCity() {
		    var lat = map.getCenter().lat();
		    var lng = map.getCenter().lng();
		    var latlng = new google.maps.LatLng(lat, lng);
		    var i =0;
		    geocoder.geocode({'latLng': latlng}, function(results, status) {
		      if (status == google.maps.GeocoderStatus.OK) {
		       		var address = results[0].address_components;
		       		for (i=0;i<address.length-1;i++){
			       		if (address[i].types[0] == "locality")
			          	document.getElementById('courseCity').value = address[i].long_name;
		       		}
		      } else {
		        alert("Geocoder failed due to: " + status);
		      }
		    });
		}

		function loadImg(){
			document.getElementById('picture').src = document.getElementById('courseImg').value
		}

		function createNewCourse(){
			var xmlhttp;
			if (window.XMLHttpRequest)
			  {// code for IE7+, Firefox, Chrome, Opera, Safari
			  xmlhttp=new XMLHttpRequest();
			  }
			else
			  {// code for IE6, IE5
			  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			  }
			xmlhttp.onreadystatechange=function()
			  {
			  if (xmlhttp.readyState==4 && xmlhttp.status==200)
			    {				    
			    	document.getElementById("infoCourse").innerHTML=xmlhttp.responseText;
			    	document.getElementById("listPanel").innerHTML="";
			    }
			  }
			xmlhttp.open("GET","course.ajax.php?req=create",true);
			xmlhttp.send();
			google.maps.event.addListener(map, 'click', function(event) {
				var marker = placeMarker(event.latLng);
			});
		}

		function cancelCreation(){
			clearOverlays();
	    	document.getElementById("infoCourse").innerHTML="<div class=\"background2\">"
	    					+ "<button onclick=\"createNewCourse()\">Create new course</button>"
	    					+ "</div>";
			<?php 
			$listCourseHtml = " <h3>Your course</h3>"
				. "<ul>";
			$dir = "kml/"; 
			$handle = opendir($dir); 
			# Making an array containing the files in the current directory:
			$files = array(); 
			while ($file = readdir($handle)) 
			{ 
			    $files[] = $file; 
			} 
			closedir($handle); 
			
			#echo the files 
			foreach ($files as $file) { 
				if($file != "." && $file != ".."){
					$list = $list."<li><span>".parserKML($dir.$file)."</span></li>"; 
				}
			} 
			$listCourseHtml = $listCourseHtml .$list."</ul>";
			?>	
	    	document.getElementById("listPanel").innerHTML= "<?php echo $listCourseHtml;?>";
			google.maps.event.clearListeners(map,'click');
		}

		function addQuestion(indexMarker){
			newwindow=window.open('question.php?var='+indexMarker,'question','height=300,width=500');
			if (window.focus) {
				newwindow.focus();
			}
		}
		</script>
		<title>Tour'SIMS</title>
	</head>
	
	<body onload="initialize()">
	<div id="header">
		<a href="index.html" id="logo"><img src="images/logo.png" alt="LOGO" /></a>
		<div id="navigation">
			<ul>
				<li class="first"><a href="index.html">Home</a></li>
				<li class="selected"><a href="createCourse.php">Your course</a></li>
				<li><a href="contact.html">Contact</a></li>
			</ul>
		</div>
		<div id="search">
			<form action="" method="">
				<input type="text" value="Search" class="txtfield" onblur="javascript:if(this.value==''){this.value=this.defaultValue;}" onfocus="javascript:if(this.value==this.defaultValue){this.value='';}" />
				<input type="submit" value="" class="button" />
			</form>
		</div>
	</div> <!-- /#header -->
	<div id="contents" >
		<div id="infoCourse">
			<div class="background2">
				<button onclick="createNewCourse()">Create new course</button>
			</div>	
		</div>
	</div>
		<div id="detailsPanel" style="width: 100%; height: 80%; float: left; background-color:#ffffff">
			<div style="margin: 1%; width:30%; float: left;background-color:#ffffff" id="listPanel">
			<h3>Your course</h3>
				<ul>
				<?php
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
				
		
				$dir = "kml/"; 
					$handle = opendir($dir); 
					$files = array(); 

					# Making an array containing the files in the current directory: 
					while ($file = readdir($handle)) 
					{ 
					    $files[] = $file; 
					} 
					closedir($handle); 
			
					#echo the files 
					foreach ($files as $file) { 
			    		if($file != "." && $file != ".."){
							echo "<li>";echo parserKML($dir.$file);echo"</li>";

						}
					} 
				?>
				</ul>
			</div>
			<div style="margin: 0; width: 62%; background-color:#ffffff" id="map_canvas">
			</div>
		</div>
	<div id="footer">
		<ul class="contacts">
			<h3>Contact Us</h3>
			<li><span>Email</span><p>: toursims@email.com</p></li>
			<li><span>Address</span><p>: INSA Lyon</p></li>
			<li><span>Phone</span><p>: 06 00 00 00 00</p></li>
		</ul>
		<div id="newsletter">
			<p><b>Sign-up for Newsletter</b>
			</p>
			<form action="" method="">
				<input type="text" value="Name" class="txtfield" onblur="javascript:if(this.value==''){this.value=this.defaultValue;}" onfocus="javascript:if(this.value==this.defaultValue){this.value='';}" />
				<input type="text" value="Enter Email Address" class="txtfield" onblur="javascript:if(this.value==''){this.value=this.defaultValue;}" onfocus="javascript:if(this.value==this.defaultValue){this.value='';}" />
				<input type="submit" value="" class="button" />
			</form>
		</div>
		<span class="footnote">&copy; Copyright &copy; 2012. All rights reserved</span>
	</div> <!-- /#footer -->
		
	</body>
</html>
