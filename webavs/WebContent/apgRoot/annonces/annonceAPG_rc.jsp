<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@page import="globaz.apg.db.annonces.APAnnonceAPGManager"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.apg.api.annonces.IAPAnnonce"%>
<%@page import="globaz.apg.pojo.APAnnonceDTO"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "PAP0021";

	rememberSearchCriterias = true;

	String error = (String)request.getAttribute("errorMsg");
	
	String totalSansRestitutuion = (String) request.getAttribute("totalSansRestitutuion");
	String totalRestitutuion = (String) request.getAttribute("totalRestitutuion");
	
	String forType = (String) request.getAttribute("forType");
	String moisAnnee = (String) request.getAttribute("forMoisAnneeComptable");
	String forEtat = (String) request.getAttribute(APAnnonceDTO.PARAMETER_KEY_ETAT);
	if(JadeStringUtil.isEmpty(forEtat)){
		forEtat = globaz.apg.api.annonces.IAPAnnonce.ETATS_NON_ENVOYE;
	}
	String forBusinessProcessId = (String) request.getAttribute("forBusinessProcessId");
	String forNssNNSS = (String) request.getAttribute("forNssNNSS");
	String partialforNss = (String) request.getAttribute("partialforNss");
	
	String orderBy = (String) request.getAttribute("orderBy");
	
	if (globaz.jade.client.util.JadeStringUtil.isEmpty(forType)) {
		forType = globaz.apg.db.annonces.APAnnonceAPGManager.FOR_TYPE_TOUS;
	}

	if(globaz.jade.client.util.JadeStringUtil.isEmpty(orderBy)){
		orderBy = globaz.apg.db.annonces.APAnnonceAPG.FIELDNAME_IDANNONCE;
	}
	//action new redéfini car le userAction dans la requête n'est pas forcément le bon
	actionNew = servletContext + mainServletPath
			+ "?userAction=apg.annonces.annonceAPG.choisirType&_method=add";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%-- tpl:put name="zoneScripts" --%>
<!--si APG -->
<%
	if ((String) globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,
			globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION) == globaz.prestation.api.IPRDemande.CS_TYPE_APG) {
%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<!--sinon, maternité -->
<%
	} else if ((String) globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,
			globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION) == globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE) {
%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%
	}
%>

<SCRIPT language="javascript">
	bFind = true;	
	usrAction = "apg.annonces.annonceAPG.lister";
	lastMoisAnneeValue = "<%=globaz.jade.client.util.JadeStringUtil.isEmpty(moisAnnee) ? "" : moisAnnee%>";
	lastType = "<%=forType%>";
	
	function forMoisAnneeComptableChange(moisAnnee){
		if(moisAnnee.value != "" && moisAnnee.value != "null"){
			if(moisAnnee.value != lastMoisAnneeValue && lastMoisAnneeValue != ""){
				//document.all('blockResultatCalculer').style.display = 'none';
				document.getElementById('resultatLabel').style.color = '#FFFFFF';
				document.getElementById('totalSansRestitutuion').style.color = '#FFFFFF';
				document.getElementById('totalRestitutuion').style.color = '#FFFFFF';
				document.getElementById('totalSansRestitutuionLabel').style.color = '#FFFFFF';
				document.getElementById('totalRestitutuionLabel').style.color = '#FFFFFF';
				lastMoisAnneeValue=moisAnnee.value;
			}else{
				document.all('blockBtnCalculer').style.display = 'block';
				lastMoisAnneeValue=moisAnnee.value;
			}
		}else{
			document.all('blockBtnCalculer').style.display = 'none';
			lastMoisAnneeValue="";
		}
	}
	
	function forTypeChange(type){
		if(type.value != lastType){
			document.getElementById('resultatLabel').style.color = '#FFFFFF';
			document.getElementById('totalSansRestitutuion').style.color = '#FFFFFF';
			document.getElementById('totalRestitutuion').style.color = '#FFFFFF';
			document.getElementById('totalSansRestitutuionLabel').style.color = '#FFFFFF';
			document.getElementById('totalRestitutuionLabel').style.color = '#FFFFFF';
			lastType = type.value;
		}
	}
	
	function calculer(){
		//document.all('blockResultatCalculer').style.display = 'block';
		document.all('isActionCalculer').value="true";
	    document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_ANNONCEAPG%>.chercher";
	    document.forms[0].target = "fr_main";
	    document.forms[0].submit();
	}
	
	function clearFields(){
		$('#forType').val("<%=globaz.apg.db.annonces.APAnnonceAPGManager.FOR_TYPE_TOUS%>");
		$('#forMoisAnneeComptable').val("");
		$('#forEtat').val("<%=globaz.apg.api.annonces.IAPAnnonce.ETATS_NON_ENVOYE%>");
		$('#forBusinessProcessId').val("");
		$(['name=forBusinessProcessId']).val("");
		$('#partialforNss').val("");
		$('#orderBy option[value="<%=globaz.apg.db.annonces.APAnnonceAPG.FIELDNAME_IDANNONCE%>"]').prop('selected', true);
	}
	
	$(document).ready (function(){
		
		$("#forEtat").val("<%=forEtat%>");
		$("#forType").val("<%=forType%>");
		
		var errorMsg = "<%= error %>";
		
		
		if(errorMsg !== "null"){
			document.all('blockBtnCalculer').style.display = 'block';
			$('#errorCalcul').css('width','300px');
			$('#errorCalcul').html(errorMsg);
		}
	});
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_ANNONCES"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_TYPE"/></TD>
							<TD>
								<SELECT id="forType" name="forType" onChange="forTypeChange(this)">
									<OPTION value="<%=globaz.apg.db.annonces.APAnnonceAPGManager.FOR_TYPE_TOUS%>" ><ct:FWLabel key="JSP_TOUS"/></OPTION>
									<OPTION value="<%=globaz.apg.db.annonces.APAnnonceAPGManager.FOR_TYPE_APG%>" ><ct:FWLabel key="JSP_APG"/></OPTION>
									<OPTION value="<%=globaz.apg.api.annonces.IAPAnnonce.CS_MATERNITE%>" ><ct:FWLabel key="JSP_MATERNITE"/></OPTION>
								</SELECT>
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="JSP_POUR_MOIS_ANNEE_COMPTABLE"/>
								<INPUT type="hidden" name="isActionCalculer" value="fasle">
							</TD>
							<TD>
								<input type="text" data-g-calendar="type:month"  id="forMoisAnneeComptable" name="forMoisAnneeComptable" value='<%=globaz.jade.client.util.JadeStringUtil.isEmpty(moisAnnee)?"":moisAnnee%>'>
								<SCRIPT language="javascript">
									fmac = document.getElementById("forMoisAnneeComptable");
 	  								fmac.onchange=function() {forMoisAnneeComptableChange(this);}
								</SCRIPT>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ETAT"/></TD>
							<TD>
								<SELECT id="forEtat" name="forEtat">
									<OPTION value="<%=globaz.apg.api.annonces.IAPAnnonce.CS_OUVERT%>" ><ct:FWLabel key="JSP_OUVERT"/></OPTION>
									<OPTION value="<%=globaz.apg.api.annonces.IAPAnnonce.CS_VALIDE%>" ><ct:FWLabel key="JSP_VALIDE"/></OPTION>
									<OPTION value="<%=globaz.apg.api.annonces.IAPAnnonce.CS_ERRONE%>" ><ct:FWLabel key="JSP_ERRONE"/></OPTION>
									<OPTION value="<%=globaz.apg.api.annonces.IAPAnnonce.CS_ENVOYE%>" ><ct:FWLabel key="JSP_ENVOYE"/></OPTION>
									<OPTION value="<%=globaz.apg.api.annonces.IAPAnnonce.ETATS_TOUS%>" ><ct:FWLabel key="JSP_TOUS"/></OPTION>
									<OPTION value="<%=globaz.apg.api.annonces.IAPAnnonce.ETATS_NON_ENVOYE%>" ><ct:FWLabel key="JSP_NON_ENVOYE"/></OPTION>
								</SELECT>
							</TD>
						</TR>
						<TR>
							<TD>BusinessProcessId</TD>
							<TD>
								<ct:inputText id="forBusinessProcessId" name="forBusinessProcessId"  defaultValue="<%=forBusinessProcessId%>"/>
							</TD>
						</TR>
						<TR>
							<TD>NSS</TD>
							<TD>
								<ct1:nssPopup avsMinNbrDigit="99" 
									nssMinNbrDigit="99" 
									name="forNss"
									value="<%= globaz.jade.client.util.JadeStringUtil.isEmpty(forNssNNSS)?partialforNss:forNssNNSS%>"
								/>
							</TD>
						</TR>
						<TR><TD colspan="2">&nbsp;</TD></TR>
						<TR>
							<TD><ct:FWLabel key="JSP_TRIER_PAR"/></TD>
							<TD>
								<SELECT id="orderBy" name="orderBy"> 
									<OPTION value="<%=globaz.apg.db.annonces.APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE%>" <%=globaz.apg.db.annonces.APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE.equals(orderBy)? "selected" : ""%>><ct:FWLabel key="JSP_MOIS_ANNEE_COMPTABLE"/></OPTION>
									<OPTION value="<%=globaz.apg.db.annonces.APAnnonceAPG.FIELDNAME_NUMEROASSURE%>" <%=globaz.apg.db.annonces.APAnnonceAPG.FIELDNAME_NUMEROASSURE.equals(orderBy)? "selected" : ""%> ><ct:FWLabel key="JSP_NSS_ABREGE"/></OPTION>
									<OPTION value="<%=globaz.apg.db.annonces.APAnnonceAPG.FIELDNAME_IDANNONCE%>" <%=(globaz.apg.db.annonces.APAnnonceAPG.FIELDNAME_IDANNONCE).equals(orderBy)? "selected" : ""%>>ID Annonce</OPTION>
								</SELECT>
							</TD>
							<TR>
								<TD>
									<input type="button" onclick="clearFields()" accesskey="C" value="<ct:FWLabel key="EFFACER"/>">[ALT+C]
								</TD>
								<TD colspan="5">
									&nbsp;
								</TD>										
							</TR>
							</TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<TABLE width="90%" align="left">
					<TBODY id="blockBtnCalculer" style="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(totalSansRestitutuion) ? "display: none;"
					: "display: block;"%>">
						<TR bgcolor="#FFFFFF">
							<TD>
								<INPUT type="button" name="btnCalculer" value="<ct:FWLabel key="JSP_CALCULER_TOTAUX"/>" onclick="calculer();">
								<span id="errorCalcul" style="color:red;"></span>
							</TD>
							<TD id="resultatLabel" style="color=<%=globaz.jade.client.util.JadeStringUtil.isEmpty(totalSansRestitutuion) ? "#FFFFFF" : "black"%>">
									<ct:FWLabel key="JSP_MONTANT_TOTAL_ALLOCATION"/> <%=globaz.apg.db.annonces.APAnnonceAPGManager.FOR_TYPE_TOUS.equals(forType) ? ""
					: (globaz.apg.db.annonces.APAnnonceAPGManager.FOR_TYPE_APG.equals(forType) ? "APG" : "Maternité")%> <ct:FWLabel key="JSP_NON_ERRONEES"/> <%=moisAnnee%> :
							</TD>
							<TD id="totalSansRestitutuion" align="right" style="color=<%=globaz.jade.client.util.JadeStringUtil.isEmpty(totalSansRestitutuion) ? "#FFFFFF" : "black"%>">
								<%=totalSansRestitutuion%>
							</TD>
							<TD id="totalSansRestitutuionLabel" style="color=<%=globaz.jade.client.util.JadeStringUtil.isEmpty(totalSansRestitutuion) ? "#FFFFFF" : "black"%>">
								<ct:FWLabel key="JSP_QUESTIONNAIRES_DUPLICATAS_RETROACTIFS"/>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD colspan="2">
								&nbsp;
							</TD>
							<TD id="totalRestitutuion" align="right" style="color=<%=globaz.jade.client.util.JadeStringUtil.isEmpty(totalSansRestitutuion) ? "#FFFFFF" : "black"%>">
								<%=totalRestitutuion%>
							</TD>
							<TD id="totalRestitutuionLabel" style="color=<%=globaz.jade.client.util.JadeStringUtil.isEmpty(totalSansRestitutuion) ? "#FFFFFF" : "black"%>">
								<ct:FWLabel key="JSP_RESTITUTIONS"/>
							</TD>
						</TR>
						
					</TBODY>
				</TABLE>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>