<?php 
	$indexMarker = $_GET['var'];
?>
<!DOCTYPE html>
<html>
	<head>
		<script type="text/javascript">
		var nbQuestion = 1;

			function addQuestion(){
				nbQuestion++;
				var main = document.getElementById('main');
				var newdiv = document.createElement('div');
				var divIdName = 'question'+nbQuestion+'div';
				newdiv.setAttribute('id',divIdName);
				newdiv.innerHTML = "<p>Type"
									+ "<input type=\"radio\" name='type"+nbQuestion+"' id='type"+nbQuestion+"' checked />Exact"
				+ "<input type=\"radio\" name='type"+nbQuestion+"' onChange=\"addButton('"+ nbQuestion+"')\"/>Multiple choice </p>"
				+ "Question: <span><input type=\"text\" name='question"+nbQuestion+"' id='question"+nbQuestion+"' /></span><br />"
				+ "Answer: <span><input type=\"text\" name='answer"+nbQuestion+"' id='answer"+nbQuestion+"' /></span><br />"
				+ "</div>"
				+ "<p>";
				main.appendChild(newdiv);
			}
				
			function saveQuestion(){
				var i = 0;
				var questionAndAnswer= "";
				var parentdiv = opener.document.getElementById("question<?php echo$indexMarker;?>");
				for (i=1;i<nbQuestion+1;i++){
					var newdiv = document.createElement('div');
					var divIdName = 'question'+nbQuestion+'div';
					var type = "";
					var buttonExact = "type"+i;
					   	if (document.getElementById(buttonExact).checked) {
						   	type="Exact";
				    	}else {
					    	type = "Multiple choice";
				    	}
 					newdiv.setAttribute('id',divIdName);
 					var newdivHTML = "<table>Question "+ i +"<ul><li>Type: <input type=\"text\" value = \"" + type + "\" id='marker<?php echo $indexMarker;?>type"+i+"'/></li>" 
					+ "<li>Question : " + "<input type=\"text\" value = \"" + document.getElementById("question"+i).value + "\" name='question"+i+"' id='marker<?php echo $indexMarker;?>question"+i+"'/></li>";
					if (type == "Multiple choice"){
						newdivHTML = newdivHTML + "<li>Right answer : " + "<input type=\"text\" value = \"" + document.getElementById("answer"+i).value + "\" name='answer"+i+"' id='marker<?php echo $indexMarker;?>answer"+i+"' /></li>"
						+ "<li>Other answer : " + "<input type=\"text\" value = \"" + document.getElementById("answer"+i+"choice0").value + "\" name='answer"+i+"' id='marker<?php echo $indexMarker;?>answer"+i+"choice0' /></li>"
						+ "<li>Other answer : " + "<input type=\"text\" value = \"" + document.getElementById("answer"+i+"choice1").value + "\" name='answer"+i+"' id='marker<?php echo $indexMarker;?>answer"+i+"choice1' /></li>"
						+ "<li>Other answer : " + "<input type=\"text\" value = \"" + document.getElementById("answer"+i+"choice2").value + "\" name='answer"+i+"' id='marker<?php echo $indexMarker;?>answer"+i+"choice2' /></li></ul></table>";
					}else{
						newdivHTML = newdivHTML + "<li>Answer : " + "<input type=\"text\" value = \"" + document.getElementById("answer"+i).value + "\" name='answer"+i+"' id='marker<?php echo $indexMarker;?>answer"+i+"' /></li></ul></table>";
					}
					newdiv.innerHTML = newdivHTML;
					parentdiv.appendChild(newdiv);				
				}
				var nbQuestionInput = window.opener.document.getElementById("nbQuestion<?php echo $indexMarker;?>");
				nbQuestionInput.value = nbQuestion;
				self.close();
			}

			function closeWindow(){
				self.close();
			}
			
			function addButton(indexQuestion){
				var questiondiv = document.getElementById('question'+indexQuestion+'div');
				var answerinput = document.getElementById('answer'+indexQuestion);
				answerinput.value = "Right answer";
				for (i=0;i<3;i++){
					var inputnode= document.createElement('input');
					inputnode.setAttribute('type','text');
					inputnode.setAttribute('name','answer'+indexQuestion+'choice'+i);					
					inputnode.setAttribute('id','answer'+indexQuestion+'choice'+i);
					inputnode.setAttribute('value','answer '+(i+1));
					inputnode.setAttribute('onfocus',"javascript:if(this.value==this.defaultValue){this.value='';}");
					questiondiv.appendChild(inputnode);
				}
			}
		</script>
		
	</head>
	<body>
		<div id="main">
			<div name ="question1div" id="question1div">
			<p>Type
			<input type="radio" name ="type1" id="type1" checked/>Exact
			<input type="radio" name ="type1" onChange="addButton('1')"/>Multiple choice </p>
			Question: <span><input type="text" name="question1" id="question1" /></span><br />
			Answer: <span><input type="text" value='' name="answer1" id="answer1" onfocus="javascript:this.value='';" /></span><br />
			</div>
		</div>
		<div id ="barButton">
			<input type='button' value='Add another question' onclick='addQuestion()'/>
			<input type='button' value='OK' onclick='saveQuestion()'/>
			<input type='button' value='Cancel' onclick='closeWindow()'/>
		</div>
	</body>
</html>
