<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<% String servletContext ="../../../";%>
<html lang="fr">
	<head>
		<meta name="User-Lang" content="FR"/> 
		<meta name="Context_URL" content="<%=request.getContextPath()%>"/> 
		<meta name="formAction" content="/webavs/pegasus"/>   
		<meta name="TypePage" content="AJAX"/> 
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		
		<title>Demo notation</title>
		
		<script type="text/javascript" src="../../../scripts/jquery.js"> </script>
		<script type="text/javascript" src="../../../scripts/jquery-ui.js"> </script>
		<script type="text/javascript" src="../../../scripts/ValidationGroups.js"> </script>
		<script type="text/javascript" src="../../../scripts/widget/globazwidget.js"> </script>
		<script type="text/javascript">
			//var MAIN_URL='';
			
				fitreNotation = {
						init: function () {
							
						},
						filtre: function (notation) {
							var s_notation = $.trim(notation);
							var $fieldset =	$("fieldset");
							if(s_notation.length){
								var $elNotation = $("#"+s_notation).parent();
								var $notations = $(".formTableLess");
								$notations.empty();
								 $fieldset.hide();
								$notations.append($elNotation);
								$elNotation.css({margin:"15% auto",
						 			 "float":"none",
						 			 "background-color": "white",
						 			  width: "600px",
						 			  padding:"2%"});
								
								$elNotation.show();
							} else {
								 $fieldset.show()
							}
						}
				}
			$(function(){
				
			
				var $select = $('input,select');
				var notation = $.getUrlVar("notation");
				if(notation){
					fitreNotation.filtre(notation);
				}
				
				$("#locker").button().click( function() {
					$select.attr('disabled',true);
					$select.change();
				    $(".mainContainerAjax").trigger(eventConstant.AJAX_DISABLE_ENABLED_INPUT);
				});

				$("#deLocker").button().click( function() {
					$select.attr('disabled',false);
					$select.change();
				    $(".mainContainerAjax").trigger(eventConstant.AJAX_DISABLE_ENABLED_INPUT);
				});

				$('#btnCan, #btnNew ,#btnVal ,#btnUpd ,#btnDel').button();

				$("#ajaxLoadData").button().click( function() {
					$('.mainContainerAjax').triggerHandler(eventConstant.AJAX_LOAD_DATA);
				});

				$("#ajaxShowDetailRefresh").button().click( function() {
					$('.mainContainerAjax').triggerHandler(eventConstant.AJAX_DETAIIL_REFRESH);
				});

				$("#ajaxShowDetail").button().click( function() {
					$('.mainContainerAjax').triggerHandler(eventConstant.AJAX_STOP_SHOW_DETAIL);
				});

				$("#ajaxStopEdition").button().click( function() {
					$('.mainContainerAjax').triggerHandler(eventConstant.AJAX_STOP_EDITION);
				});

				$("#ajaxValidateEditon").button().click( function() {
					$('.mainContainerAjax').triggerHandler(eventConstant.AJAX_VALIDATE_EDITION);
				});

				$("#ajaxUpdateComplete").button().click( function() { 
					$('.mainContainerAjax').triggerHandler(eventConstant.AJAX_UPDATE_COMPLETE); 
				});

				window.setTimeout(function(){ $('html').triggerHandler(eventConstant.AJAX_INIT_DONE);},10);

				$('#validateAndDisplayError').button().click(function(){
					notationManager.validateAndDisplayError();
				});
				
				$(".mainContainerAjax").css({
					'background-color': '#FFFFFF',
					'border': '0'
				});
				$("#filtre").change(function () {
					fitreNotation.filtre(this.value);
				});
			});
		</script> 
		<%@ include file="../notationLibJs.jspf" %>
		<script type="text/javascript" src="../../../scripts/jsnotation/core/specificationMoteur/specific_ajax.js"> </script>

		<link type="text/css" href="../test/testNotation.css" rel="stylesheet" />
		<link type="text/css" href="../../../theme/jquery/jquery-ui.css" rel="stylesheet" />
		<link type="text/css" href="../../../theme/master.css" rel="stylesheet" />
		<link type="text/css" href="../../../theme/ajax/templateZoneAjax.css" rel="stylesheet" />  
	</head>
	<body>
		<div>Filtre<input type="text" id="filtre" /> </div>
		<form action="#">
			<div id="testInput" style=""> 
				<div class="mainContainerAjax">
					<div id="event">
						<div>
							<h3>Verrouillage:</h3> 
							<span id='locker'> Verrouiller </span>
							<span id='deLocker'> D&eacute;verrouiller </span>
						</div>
						<div>
							<h3>Validation:</h3>
							<span id="validateAndDisplayError">Valider et afficher les erreurs</span>
						</div>
						<div id="btnCtrlJade"> 
							<h3>&Eacute;v&eacute;nement li&eacute;s aux boutons&nbsp;:&nbsp;</h3> 
							<span name='btnCan' id='btnCan'> Cancel </span>
							<span name='btnNew' id='btnNew'> Add </span>
							<span name='btnVal' id='btnVal'> Valider </span>
							<span name='btnUpd' id='btnUpd'> Modifier </span>
							<span name='btnDel' id='btnDel'> Supprimer </span>
						</div>
						<div id="btnCtrlJade">  
							<h3>Autre &eacute;v&eacute;nement:</h3> 
							<span id='ajaxLoadData'> ajaxLoadData </span>
							<span id='ajaxShowDetailRefresh'> ajaxShowDetailRefresh </span>
							<span id='ajaxShowDetail'> ajaxShowDetail </span>
							<span id='ajaxStopEdition'> ajaxStopEdition </span>
							<span id='ajaxValidateEditon'> ajaxValidateEditon </span>
							<span id='ajaxUpdateComplete'> ajaxUpdateComplete </span>
						</div>
					</div>
					<%@ include file="../fragementNotation.jspf" %>
				</div>
			</div>  
		</form>
	</body>
</html>