<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pyxis.constantes.IConstantes"%>
<%@page import="globaz.globall.db.GlobazServer"%>
<TR>
	<TD>
	<script language="Javascript">
		<%
			String checkUpi = GlobazServer.getCurrentSystem().getApplication("APG").getProperty("rapg.checkUpi");
			if (!JadeStringUtil.isEmpty(checkUpi) && "true".equals(checkUpi)) {
				%>
				var isCheckUpiActive = true;
				<%
			} else {
				%>
				var isCheckUpiActive = false;
				<%		
			}
		%>
	
	
		  rulesToBreak = "";
		  
		  function checkNss(nss) {
			  if (nss.length == 16 && isCheckUpiActive) {
				  var options = {
						serviceClassName: 'ch.globaz.apg.business.services.upi.UPIService',
						serviceMethodName:'getPerson',
						parametres:nss,
						wantInitThreadContext: true,
						callBack: function (data) {
							if (document.getElementById("nomAffiche").disabled) {
								var hasDifference = false;
								var nomPyxis = document.getElementById("nomAffiche").value;
								var prenomPyxis = document.getElementById("prenomAffiche").value;
								var sexePyxis = document.getElementById("csSexeAffiche").value;
								var dateNaissancePyxis = document.getElementById("dateNaissanceAffiche").value;
								if (data.nom.toUpperCase() != nomPyxis.toUpperCase()) {
									$("#nomPyxis").css("border", "3px solid red");
									hasDifference = true;
								}
								if (data.prenom.toUpperCase().search(prenomPyxis.toUpperCase())<0) {
									$("#prenomPyxis").css("border", "3px solid red");
									hasDifference = true;
								}
								if (data.sexe != sexePyxis) {
									$("#sexePyxis").css("border", "3px solid red");
									hasDifference = true;
								}
								if (data.dateNaissance != dateNaissancePyxis) {
									$("#dateNaissancePyxis").css("border", "3px solid red");
									hasDifference = true;
								}
								if (hasDifference) {
									$("#nomPyxis").html(nomPyxis);
									$("#nomUpi").html(data.nom);
									$("#prenomPyxis").html(prenomPyxis);
									$("#prenomUpi").html(data.prenom);
									if (sexePyxis == "<%=IConstantes.CS_PERSONNE_SEXE_FEMME%>") {
										$("#sexePyxis").html("F");
									} else if (sexePyxis == "<%=IConstantes.CS_PERSONNE_SEXE_HOMME%>") {
										$("#sexePyxis").html("H");
									} else {
										$("#sexePyxis").html("-");
									}
									if (data.sexe == "<%=IConstantes.CS_PERSONNE_SEXE_FEMME%>") {
										$("#sexeUpi").html("F");
									} else if (data.sexe == "<%=IConstantes.CS_PERSONNE_SEXE_HOMME%>") {
										$("#sexeUpi").html("H");
									} else {
										$("#sexeUpi").html("-");
									}
									$("#dateNaissancePyxis").html(dateNaissancePyxis);
									$("#dateNaissanceUpi").html(data.dateNaissance);
									$("#dialog-check-upi").dialog({
										modal: true,
										width: 600,
										height: 500,
										buttons: {
											"Corriger dans Web@Tiers": function() {
												window.location.href="pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=" + document.getElementById("idAssure").value;
											},
											Ok: function() {
												$( this ).dialog( "close" );	
											}
										}
									});
								}
							} else {
								document.getElementById("nomAffiche").value = data.nom; 
								document.getElementById("nomAffiche").disabled = false;
								document.getElementById("prenomAffiche").value = data.prenom; 
								document.getElementById("prenomAffiche").disabled = false;
								document.getElementById("dateNaissanceAffiche").value = data.dateNaissance; 
								document.getElementById("dateNaissanceAffiche").disabled = false;
								document.getElementById("csEtatCivilAffiche").disabled = false;
								document.getElementById("csSexeAffiche").value = data.sexe; 
								document.getElementById("csSexeAffiche").disabled = false;
							}
						},
						callBackError: function (data) {
							alert("<ct:FWLabel key='APG_BOX_CHECK_UPI_ERROR'/> : " + data);
						}
					}
					
					globazNotation.readwidget.options = options;
					globazNotation.readwidget.read();
					
			  }
			  return false;
		  }
		  
 		  	$(function() {
 			  	$( "#dialog:ui-dialog" ).dialog( "destroy" );
				
 				$( "#dialog-plausibilites" ).hide();
 				$( "#dialog-check-upi" ).hide();
 			});
	
	</script>
	
	<div id="dialog-check-upi" title='<ct:FWLabel key="APG_BOX_CHECK_UPI"/>'>
		<table width="100%" cellpadding="2" cellspacing="10">
			<tr>
				<th width="150"><ct:FWLabel key='APG_CHECKUPI_CHAMP'/></th>
				<th><ct:FWLabel key='APG_CHECKUPI_WEBTIERS'/></th>
				<th><ct:FWLabel key='APG_CHECKUPI_UPI'/></th>
			</tr>
			<tr id="ligneNom">
				<td><ct:FWLabel key='APG_CHECKUPI_NOM'/></td>
				<td id="nomPyxis"></td>
				<td id="nomUpi"></td>
			</tr>
			<tr id="lignePrenom">
				<td><ct:FWLabel key='APG_CHECKUPI_PRENOM'/></td>
				<td id="prenomPyxis"></td>
				<td id="prenomUpi"></td>
			</tr>
			<tr id="ligneSexe">
				<td><ct:FWLabel key='APG_CHECKUPI_SEXE'/></td>
				<td id="sexePyxis"></td>
				<td id="sexeUpi"></td>
			</tr>
			<tr id="ligneDateNaissance">
				<td><ct:FWLabel key='APG_CHECKUPI_DATENAISSANCE'/></td>
				<td id="dateNaissancePyxis"></td>
				<td id="dateNaissanceUpi"></td>
			</tr>
		</table>
	</div>
	
	<div id="dialog-plausibilites" title='<ct:FWLabel key="APG_BOX_PLAUSIBILITES_TITRE"/>'>
		<table id="errorTable">
			<thead>
				<tr>
					<th><ct:FWLabel key="APG_ERR_CODE"/></th>
					<th width="350"><ct:FWLabel key="APG_MESSAGE"/></th>
					<th><ct:FWLabel key="APG_QUITTANCER"/></th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<p>* <ct:FWLabel key="APG_ANNONCE_REVIENDRA"/></p>
	</div>
	<input type="hidden" name="rulesToBreak" id="rulesToBreak" value=""/>
	
	</TD>
<TR>

	