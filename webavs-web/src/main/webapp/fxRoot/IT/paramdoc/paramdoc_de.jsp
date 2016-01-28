<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"  import="globaz.globall.http.*" contentType="text/html;charset=iso-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<%
bButtonCancel = false;
bButtonValidate = false;
bButtonDelete = false;
bButtonUpdate = false;
bButtonNew = false;
idEcran = "___XXXX";
globaz.fx.vb.paramdoc.FXParamDocViewBean viewBean = (globaz.fx.vb.paramdoc.FXParamDocViewBean)request.getAttribute("viewBean");

%>
<%-- /tpl:put --%> 
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
		<style>
			.c { 
				border-right : solid 1px silver; 
				padding-right : 10;
			}
			.c2 { 
				padding-left : 10;
				display : none;
			}
			.border {
				
			}
		</style>
<script type="text/javascript" src="/scripts/jquery.js"></script>
<SCRIPT language="JavaScript">
top.document.title = "Test"
<!-- hide this script from non-javascript-enabled browsers
var idDocEditingList = new Array();  // map pour savoir si un doc est adapte pour editing

function add() {
}
function upd() {
}
function validate() {
}
function cancel() {
}
function del() {
}
function init(){
	// construction d'une liste javascript des documents (idDoc) adaptes  pour Editing
	<%
		int index = 0;
		for (java.util.Iterator it = viewBean.getDocDescriptionList().iterator();it.hasNext();) { 
			String[] desc = (String[]) it.next();
			if (desc[2] != null) { //  desc[3] marcherait également
		%>
		idDocEditingList [<%=(index++)%>] = ["<%=desc[0]%>","<%=desc[1]%>","<%=desc[2]%>","<%=desc[3]%>","<%=desc[4]%>","<%=desc[5]%>"]
		<% } // fin du if  
		} // fin du for
	%>
	//
	// Gestion de la recherche dans la liste des documents
	//
	$('#iDocFilter').keyup( function() {

			var v = this.value
			var options = ""
			$('#sHiddenSelect')
				.children()
				.filter(function() {return this.text.toUpperCase().indexOf(v.toUpperCase())>-1})
				.each ( function() { options+= "<option value="+this.value+">"+this.text+"</option>"})
			$('#sDocList').html(options);
		
	} );
}

function movDoc(moveToLabel) {
	var values = $("#sDocList").val() || [];
	$.ajax({ 
		url: "<%=request.getContextPath()%>/fx?userAction=fx.paramdoc.main.moveDoc",  
		data : {
			docs : values.join(","),
			label : moveToLabel
			},
		type: "POST",
		success: function(msg){
        	$("#zMessages").text(msg.substring(3))
        	if (msg.substring(0,3)=="OK-") {
            	refreshDocList();	
            }
      	}
  	});
}

function refreshDocList() {
	$("#sDocList").html("");
	$("#sHiddenSelect").html("");
	$.ajax({ 
		url: "<%=request.getContextPath()%>/fx?userAction=fx.paramdoc.main.refreshDocList",  
		type: "POST",
		success: function(msg){
			$("#iDocFilter").val("");
        	$("#sDocList").html(msg);
        	$("#sHiddenSelect").html(msg);
        	
      	}
  	});
}
function addConfig(configLabel) {
	
	$.ajax({ 
		url: "<%=request.getContextPath()%>/fx?userAction=fx.paramdoc.main.addConfig", 
		data : {label : configLabel},
		type: "POST",
		success: function(msg){
        	$("#zMessages").text(msg.substring(3))
        	if (msg.substring(0,3)=="OK-") {
        		$("#iConfigLabel").val("");
            	$('#sConfig').append("<option value="+configLabel+">"+configLabel+"</option>");
            	$('#sMoveDoc').append("<option value="+configLabel+">"+configLabel+"</option>");
            	$('#sMoveDoc').val(configLabel)
            	
            }
      	}
  	});
}
function renameConfig(currentLabel, newLabel) {
	$.ajax({ 
		url: "<%=request.getContextPath()%>/fx?userAction=fx.paramdoc.main.renameConfig", 
		data : {label : currentLabel,
				newLabel: newLabel },
		type: "POST",
		success: function(msg){
        	$("#zMessages").text(msg.substring(3))
        	if (msg.substring(0,3)=="OK-") {
        		$('#sConfig option[value='+currentLabel+']').remove();
        		$('#sMoveDoc option[value='+currentLabel+']').remove();
        		$('#sConfig').append("<option value="+newLabel+">"+newLabel+"</option>");
            	$('#sMoveDoc').append("<option value="+newLabel+">"+newLabel+"</option>");
            	$('#sMoveDoc').val(newLabel);
            	refreshDocList()
            }
      	}
  	});	
}

function deleteConfig(configLabel) {
	$.ajax({ 
		url: "<%=request.getContextPath()%>/fx?userAction=fx.paramdoc.main.deleteConfig", 
		data : {label : configLabel },
		type: "POST",
		success: function(msg){
        	$("#zMessages").text(msg.substring(3))
        	if (msg.substring(0,3)=="OK-") {
        		$('#sConfig option[value="'+configLabel+'"]').remove();
        		$('#sMoveDoc option[value="'+configLabel+'"]').remove();
        		loadConfig("MASTER")
            }
      	}
  	});	
}
function loadConfig(configLabel) {
	$.ajax({ 
		url: "<%=request.getContextPath()%>/fx?userAction=fx.paramdoc.main.loadConfig", 
		data : {label : configLabel },
		type: "POST",
		success: function(msg){
			if (msg.substring(0,3)=="OK-") {
        		$("#zParam").html(msg.substring(3));
        		$("#sConfig").val(configLabel);
			} else {
				$("#tConfigText").val("");
				$("#zMessages").html(msg.substring(3));	
      		}
		}
  	});	
}

function loadConfigStructure(configLabel) {
	$.ajax({ 
		url: "<%=request.getContextPath()%>/fx?userAction=fx.paramdoc.main.loadConfigStructure", 
		data : {label : configLabel },
		type: "POST",
		success: function(msg){
			if (msg.substring(0,3)=="OK-") {
				$("#zParam").html(msg.substring(3));
        		$("#sConfig").val(configLabel);
			} else {
				$("#tConfigText").val("");
				$("#zMessages").html(msg.substring(3));	
      		}
		}
  	});	
}

function saveConfigStructure(configLabel) {
	$('#btSaveConfig').attr('disabled', 'disabled');
	$.ajax({ 
		url: "<%=request.getContextPath()%>/fx?userAction=fx.paramdoc.main.saveConfigStructure", 
		contentType : "application/x-www-form-urlencoded; charset=ISO-8859-1",
		data : {
				label : configLabel,
				configText : $('#tConfigText').val()
		},
		type: "POST",
		success: function(msg){
        	$("#zMessages").html(msg.substring(3));
        	if (msg.substring(0,3)=="OK-") {
        		loadConfig(configLabel); // reload la config seulement si il n'y a pas d'erreur
        	}
      	},
      	complete: function() {
      		$('#btSaveConfig').removeAttr('disabled');

        }
  	});	
}

function saveConfig(configLabel) {
	$('#btSaveConfig').attr('disabled', 'disabled');

	var values =[];
	$("input[name='ival']").each( function() { values.push( this.id+"="+this.value)})
	$.ajax({ 
		url: "<%=request.getContextPath()%>/fx?userAction=fx.paramdoc.main.saveConfig", 
		contentType : "application/x-www-form-urlencoded; charset=ISO-8859-1",
		data : {
				label : configLabel,
				configText : values.join("¬") // les valeurs de tout les inputs ayant pour nom ival sont envoyer
											  // sous la forme 779,0=tutu¬780,0=titi
											  // ¬ est le séparateur, 
											  // 779,0=tutu => paramid = 779, idrange = 0, valeur = tutu
		},
		type: "POST",
		success: function(msg){
        	$("#zMessages").html(msg.substring(3));
        	if (msg.substring(0,3)=="OK-") {
        		loadConfig(configLabel); // reload la config seulement si il n'y a pas d'erreur
        	}
      	},
      	complete: function() {
      		$('#btSaveConfig').removeAttr('disabled');

        }
  	});	
}



//index : index dans la liste interne editing (idDocEditingList)
function saveFormatInfo(index) {
	var idDoc = idDocEditingList[index][0]
	var isXml = document.getElementById("isXmlOut").checked
	var isFormated = document.getElementById("isFormatedOut").checked
	
	$.ajax({ 
		url: "<%=request.getContextPath()%>/fx?userAction=fx.paramdoc.main.saveFormatInfo", 
		data : {
			idDoc : idDoc,
			isXml : isXml,
			isFormated : isFormated
		},
		type: "POST",
		success: function(msg){
     	$("#zMessages").html(msg.substring(3));
     	if (msg.substring(0,3)=="OK-") {
     		// mise a jour du tableau interne
     		if (isXml) {
     			idDocEditingList[index][2] = "checked"
     		} else {
     			idDocEditingList[index][2] = ""
     		}
     		if (isFormated) {
     			idDocEditingList[index][3] = "checked"
     		} else {
     			idDocEditingList[index][3] = ""
     		}
     	}
   	},
   	complete: function() {
   		$('#btSaveConfig').removeAttr('disabled');
     }
	});	
	
}



function updateOutput() {
	var values = $("#sDocList").val() || [];
	var editingReady = false
	var index = 0
	for (index; index<idDocEditingList.length; index++) {
		if (values[0] == idDocEditingList[index][0]) {
			editingReady = true;
			break;
		}
	}
	if (editingReady) {
		document.getElementById("output").innerHTML = 
			"<b>"+idDocEditingList[index][4]+" / "+idDocEditingList[index][5]+"</b><br><br>"
			+"<fieldset><legend>Format  </legend>"
			+"<input type='checkbox' id='isXmlOut' "+idDocEditingList[index][2]+" > XML "
			+"<br>"
			+"<input type='checkbox' id='isFormatedOut' "+idDocEditingList[index][3]+" > PDF"
			+"<br><br>"
			+"<input type='button' value='Enregistrer' onclick='saveFormatInfo("+index+")'>"
			+"</fieldset>"
	} else {
		document.getElementById("output").innerHTML = ""
	}
}

// stop hiding -->


</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Test<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
						<tr><td>

<input type="button" value="Groupement des documents" onclick="javascript:$('#tdDef').hide();$('#tdList').show();">						
<input type="button" value="Paramètres des groupes" onclick="javascript:$('#tdDef').show();$('#tdList').hide();">
<table height="100%" width="100%" border=1>
	<tr valign='top' >
		<!-- 
		
			Page 1
		
		 -->
		<td id="tdList" class="c">
			<h3>Page 1 - Définition des groupes de document</h3>
			Recherche : <input id="iDocFilter" type="text" style='width:10cm'>
			<table>
			<tr>
			<td>
			<div id="zDoc" class="border">
				<select onchange="updateOutput()" multiple="multiple" id="sDocList" size=30 style="">
						<%
						
						for (java.util.Iterator it = viewBean.getDocDescriptionList().iterator();it.hasNext();) { 
							
							String[] desc = (String[]) it.next();
						%>
						
						<option value="<%=desc[0]%>"><%=desc[1]%></option>
						<%} %>
				</select>
				<select id="sHiddenSelect" size=40 style="display:none">
						<%for (java.util.Iterator it = viewBean.getDocDescriptionList().iterator();it.hasNext();) { 
							String[] desc = (String[]) it.next();
						%>
						<option value="<%=desc[0]%>"><%=desc[1]%></option>
						<%} %>
				</select>
			</div>
			</td>
			<td valign="top">
				<div id="output"></div>
			</td>
			</tr>
			</table>
			<ct:ifhasright element="fx.paramdoc.main" crud="cud">
				<hr>
				<select id="sMoveDoc" style="width:10cm">
					<%for (java.util.Iterator it = viewBean.getConfigList().iterator();it.hasNext();) { 
						String conf = (String) it.next();
					%>
						<option value="<%=conf%>"><%=conf%></option>
					<%} %>
				</select>
				
				<input type="button" value="Affecter" onclick="javascript:movDoc($('#sMoveDoc').val())">
				
				&nbsp;|&nbsp;<input type = text id="iNewLabel">  <input type="button" value="Renomer" onclick="javascript:renameConfig($('#sMoveDoc').val(),$('#iNewLabel').val())" >
				&nbsp;|&nbsp;<input type="button" value="Supprimer" onclick="javascript:deleteConfig($('#sMoveDoc').val())" disabled>
				<hr>
				Créer le groupe : <input id="iConfigLabel" style="width:10cm" type="text"> <input type="button" value="Créer" onclick="javascript:addConfig($('#iConfigLabel').val())">
			</ct:ifhasright>
		</td>
		
		<!-- 
		
			Page 2
		
		 -->
		
		<td id="tdDef" class="c2" >
			<h3>Page 2 - Définition des paramètres des groupes </h3>
			<table><tr>
			<td valign="top" >
				<span id="zConfig" class="border">
				<select size=15 id="sConfig" onchange="javascript:loadConfig(this.value)">
					<%for (java.util.Iterator it = viewBean.getConfigList().iterator();it.hasNext();) { 
						String conf = (String) it.next();
					%>
						<option value="<%=conf%>"><%=conf%></option>
					<%} %>
				</select>
				<script>
					$("#sConfig").val("MASTER");
				</script>
				</span>
			</td>
			<td valign="top" width="100%" style="border-left : solid 1px black;padding-left:0.3cm">
			<div id="zParam" class="border">
				<%=viewBean.getConfigText()%>
				<!-- 
				<textarea rows=30 cols=60 id="tConfigText"  style="width:100%"><%=viewBean.getConfigText()%></textarea>
				 -->
			</div>
			</td>
			</tr></table>
			
		</td>
	</tr>
</table>
<div id="zMessages" class="border">Messages :</div>						
							
						</td></tr>				
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>