<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CGE0001";
	actionNew  += "&idLot=" + ((request.getParameter("idLot")!=null)?request.getParameter("idLot"):"") ;
	if(JadeStringUtil.isBlank(request.getParameter("idLot"))){
		bButtonNew = false;
	}
	rememberSearchCriterias=true;
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	String numAffilie = request.getParameter("numAffilie");
	int autoDigiAff = globaz.phenix.util.CPUtil.getAutoDigitAff(session);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<SCRIPT>
	usrAction = "campus.annonces.annoncesImputations.lister";
	<%if (!JadeStringUtil.isBlankOrZero(request.getParameter("idLot"))) { %>
		bFind = true;
	<%}else{%>
		bFind = false;
	<%}%>

	function updateNumAffilie(data){
		document.getElementById('forNumAffilie').value = data;
	}

	function testImputationAnnonce(){
		if (document.getElementById('checkAnnonces').checked){
			//Annonces uniquement
			document.getElementById('forAnnoncesUniquement').value=true;			
		}else {
			document.getElementById('forAnnoncesUniquement').value=false;
		}
		if (document.getElementById('checkImputations').checked){
			//Imputation uniquement
			document.getElementById('forImputationsUniquement').value=true;			
		}else {
			document.getElementById('forImputationsUniquement').value=false;
		}
		if (document.getElementById('checkAnnonces').checked && document.getElementById('checkImputations').checked){
			//Annonces et imputations
			document.getElementById('forAnnoncesUniquement').value=false;
			document.getElementById('forImputationsUniquement').value=false;
		} else if (!document.getElementById('checkAnnonces').checked && !document.getElementById('checkImputations').checked){
			//Annonces et imputations
			document.getElementById('forAnnoncesUniquement').value=true;
			document.getElementById('forImputationsUniquement').value=true;
		}
	}
	
</SCRIPT>
	<ct:menuChange displayId="menu" menuId="GEMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="GEMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Recherche des annonces
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD width="100">N° Lot</TD>
							<%if (request.getParameter("idLot")== null) {%>
            				<TD width="200">
								<INPUT type="text" name="forIdLot" value="" size="17">
	     					</TD>
	     					<%} else { %>
            				<TD width="200">
								<INPUT type="text" name="forIdLot" readonly="readonly"  
								value="<%=request.getParameter("idLot")%>" size="17">
	     					</TD>
	     					<%}%>
            				<TD width="200">N° Immatriculation transmis</TD>
            				<TD width="200">
								<INPUT type="text" name="forNumImmatriculationTransmis" value="" size="15">
	     					</TD>
	     					<TD width="80">Nom</TD>
            				<TD width="200">
								<INPUT type="text" name="forNomLike" value="" size="21">
	     					</TD>
          				</TR>
          				<TR>
	          				<TD>Etat annonce</TD>
	            			<TD>
								<ct:FWCodeSelectTag name="forCsEtatAnnonce"
								defaut="" codeType="GEETAT_ANN" wantBlank="true"/>
							</TD>
	            			<TD>NSS</TD>
	            			<TD>
	            				<nss:nssPopup name="forNumAvs" cssclass="libelle"
		            			avsAutoNbrDigit="11" nssAutoNbrDigit="10"  avsMinNbrDigit="5" nssMinNbrDigit="8" />
		            		</TD>
		            		<TD>Prénom</TD>
	           				<TD>
								<INPUT type="text" name="forPrenomLike" value="" size="21">
								<INPUT type="hidden" name="forAnnoncesUniquement"
								value="<%=new Boolean (true)%>">
	     					</TD>
          				</TR>
          				<TR>
							<%if (request.getParameter("idLot")== null) {%>
							<TD width="100">Annonces</TD>
            				<TD width="200">
								<INPUT type="checkbox" name="checkAnnonces" checked="checked" onclick="testImputationAnnonce();">
	     					</TD>
	     					<TD width="200">Imputations</TD>
            				<TD width="200">
								<INPUT type="checkbox" name="checkImputations" onclick="testImputationAnnonce();">
	     					</TD>
	     					<%}%>
          					<TD width="100">N° affilié</TD>
            				<TD width="200">
	          					<INPUT type="text" name="forNumAffilie" value="" size="21">
          					</TD>
          					<TD>
	     						<INPUT type="hidden" name="forAnnoncesUniquement" value="true">
	     						<INPUT type="hidden" name="forImputationsUniquement" value="false">
	     					</TD>
          				</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>

			<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>