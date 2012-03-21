<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>Tour'SIMS'</title>
	<link rel="stylesheet" href="css/style.css" type="text/css" charset="utf-8" />	
	<!--[if lte IE 7]>
		<link rel="stylesheet" href="css/ie.css" type="text/css" charset="utf-8" />	
	<![endif]-->
</head>

<body>
	<div id="header">
		<a href="index.php" id="logo"><img src="images/logo.png" alt="LOGO" /></a>
		<div id="navigation">
			<ul>
				<li class="first selected"><a href="index.php">Home</a></li>
				<li><a href="createCourse.php">Your course</a></li>
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

	<div id="contents2">
		<div class="body">
			<div id="sidebar">
				<h3>New course</h3>
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
							$htmlReturn = $htmlReturn. '<span>City: '.$value;
						}
						if ($dName == 'text') {
							$value = $data->getElementsByTagName('value')->item(0)->nodeValue;
							$htmlReturn = $htmlReturn. ' </br>Description: '.$value.'</span>';
						}
					}
					
					return $htmlReturn;
				}
				
				$dir = "kml/"; 
				$handle = opendir($dir); 
				$files = array(); 
				$filestime = array();
				
				# Making an array containing the files in the current directory: 
				while ($file = readdir($handle)) 
				{ 
         			 $files[] = $file;
				} 
				closedir($handle); 
				$files = array_splice($files,2);
				array_multisort(
				    array_map( 'filemtime', $files ),
				    SORT_NUMERIC,
				    SORT_ASC,
				    $files
				);
				$newsetfiles = array_splice($files,0,4);
				#echo the files 
				foreach ($newsetfiles as $file) { 
		    		if($file != "." && $file != ".."){
							echo "<li>";echo parserKML($dir.$file);echo"</li>";
					}
				} 
						
				?>
				</ul>
			</div>
			<div id="main">
				<span>....</span>
				<ul>
					<li>
						<a href=""><img src="images/globe.jpg" alt="Img" /><h3>Worldwide map</h3></a>
						<p>You can use Tour'SIMS all over the world</p>
					</li>
					<li>
						<a href=""><img src="images/tools.jpg" alt="Img" /><h3>Personalization</h3></a>
						<p>Create your own course and enjoy it</p>
					</li>
					<li>
						<a href=""><img src="images/check.jpg" alt="Img" /><h3>Funny riddle</h3></a>
						<p></p>
					</li>
					<li>
						<a href=""><img src="images/graph.jpg" alt="Img" /><h3>Have fun with others around you</h3></a>
						<p>More people, more fun. Send message to others tourist around you</p>
					</li>
				</ul>
				<p>And more ... </p>
			</div>
		</div>
	</div> <!-- /#contents -->
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