<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
idEcran="CCP0002";
bButtonFind = false;
actionNew = servletContext + mainServletPath + "?userAction=phenix.principale.decision.initCreer&_method=add";
bButtonNew = objSession.hasRight("phenix.principale.decision.initCreer", "ADD");
String gedFolderType = "";
String gedServiceName = "";
try {
	globaz.globall.api.BIApplication osiApp = globaz.globall.api.GlobazSystem.getApplication(globaz.phenix.application.CPApplication.DEFAULT_APPLICATION_PHENIX);
	gedFolderType = osiApp.getProperty("ged.folder.type", "");
	gedServiceName = osiApp.getProperty("ged.servicename.id", "");
} catch (Exception e){
	// Le reste de la page doit tout de même fonctionner
}

%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%><ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CP-OnlyDetail"/>
<SCRIPT>
// menu 
top.document.title = "Cotisation - Décision"
usrAction = "phenix.principale.decision.lister";
servlet = "phenix";
bFind=true;
</SCRIPT>
<%

							
globaz.phenix.db.principale.CPEnteteViewBean viewBean = (globaz.phenix.db.principale.CPEnteteViewBean)session.getAttribute ("viewBean");
java.lang.Boolean impressionFlux;
		try {
			impressionFlux =  ((globaz.phenix.application.CPApplication) viewBean.getSession().getApplication()).isAffichageDecisionFluxPDF();
		}catch (Exception e) {
			impressionFlux = Boolean.TRUE;
	}							
if(viewBean.getDocListe().size()>0){
	for(int i=0;i<viewBean.getDocListe().size();i++){
		%>
			<% if(impressionFlux.booleanValue()){ %>
			<script>
				window.open('<%=request.getContextPath()+"/phenix?userAction=phenix.document.decision.generer&doc="+(String)viewBean.getDocListe().elementAt(i)%>');								
			</script>			
			<% } else {%>
			<script>
				window.open("<%=request.getContextPath()+"/persistence/"+(String)viewBean.getDocListe().elementAt(i)%>");
			</script>	
			<% } %>
			
		<%
	}
}
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
<span class="postItIcon"><ct:FWNote sourceId="<%=viewBean.getIdTiers()%>" tableSource="globaz.phenix.db.principale.CPEnteteViewBean"/></span>Décisions des cotisations personnelles<%-- /tpl:put --%>				
<%@ include file="/theme/find/bodyStart2.jspf" %>

						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD nowrap width="128">
            <ct:ifhasright element="pyxis.tiers.tiers.diriger" crud="r">
            	<A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdTiers()%>" class="external_link">Tiers</A>
            </ct:ifhasright>
            </TD>
             <TD nowrap colspan="2"> 
              <INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getNom()%>" class="libelleLongDisabled" readonly> 
            </TD> 
            <TD width="10"></TD>
            <TD nowrap width="50" align="left">Affilié</TD>
            <TD nowrap > 
              <INPUT type="text" name="numAffilie" tabindex="-1" value="<%=viewBean.getAffiliation().getAffilieNumero()%>" class="libelleLongDisabled" readonly>
            </TD>
             <TD width="10"></TD>
            <TD nowrap width="128">
            	<%
            	String idCompteAnnexe=viewBean.getIdCompteAnnexe();
            	if(!JadeStringUtil.isEmpty(idCompteAnnexe)){
            	%>
            	<ct:ifhasright element="osiris.comptes.apercuComptes.afficher" crud="r">
             		<A href="<%=request.getContextPath()%>\osiris?userAction=osiris.comptes.apercuComptes.afficher&idCompteAnnexe=<%=idCompteAnnexe%>" class="external_link">Compte annexe</A>
             	</ct:ifhasright>
           		<%
           		if(viewBean.getIrrecouvrable().equals(Boolean.TRUE)){
            	%>
             		<IMG title="Irrecouvrable" src="<%=request.getContextPath()%>/images/avertissement.gif" border="0">
           		<%
           		}}
           		%>	 
            </TD>
          </TR>
          <TR> 
            <TD nowrap width="128">
            	<ct:ifhasright element="naos.affiliation.chercher" crud="r">
            	<A href="<%=request.getContextPath()%>\naos?userAction=naos.affiliation.chercher&idTiers=<%=viewBean.getIdTiers()%>" class="external_link">Affiliation</A>
            	</ct:ifhasright>
            </TD>
            <TD nowrap colspan="2"> 
              <INPUT type="text" name="localite" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getLocaliteLong()%>" readonly>
              </TD>
            <TD width="10"></TD>
            <TD nowrap width="50" align="left">NSS</TD>
            <TD nowrap> 
              <INPUT type="text" name="numAvs" tabindex="-1" value="<%=viewBean.getDescriptionNNSS()%>" class="libelleLongDisabled" readonly>
            </TD>
            <TD width="10"></TD>
            <TD nowrap width="128">
            	<%
            	String idCompteIndividuel=viewBean.getIdCompteIndividuel();
            	if(!JadeStringUtil.isEmpty(idCompteIndividuel)){
            	%>
            	<ct:ifhasright element="pavo.compte.ecriture.chercherEcriture" crud="r">
             	<A href="<%=request.getContextPath()%>\pavo?userAction=pavo.compte.ecriture.chercherEcriture&compteIndividuelId=<%=viewBean.getIdCompteIndividuel()%>" class="external_link">CI</A>
             	</ct:ifhasright>
            	<%
           		}
           		%>
            </TD>
          </TR>
		  <tr>
		  	<td colspan="6">
		  		<ct:ifhasright element="phenix.communications.communicationFiscaleAffichage.chercher" crud="r">
             		<A href="<%=request.getContextPath()%>\phenix?userAction=phenix.communications.communicationFiscaleAffichage.chercher&likeNumAffilie=<%=viewBean.getAffiliation().getAffilieNumero()%>" class="external_link">Gestion des demandes</A>
             	</ct:ifhasright>
             	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             	
             	<%
             		String gedAffilieNumero = viewBean.getAffiliation().getAffilieNumero();
             		String gedNumAvs = viewBean.getNumAvsActuel();
             		String gedIdTiers = viewBean.getIdTiers();
             		String gedIdRole = "";
             	%>
             	<%@ include file="/theme/gedCall.jspf" %>
             	
				
			  	<%if ("true".equals(globaz.globall.db.GlobazServer.getCurrentSystem().getApplication("NAOS").getProperty("isSuiviRevenuBilan"))) {%>
		     	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				
				<ct:ifhasright element="phenix.suivis" crud="cud">
				Rev. et bilan : 
			  		<a href="<%=servletContext%>/phenix?userAction=phenix.suivis.nouvelleAffiliation.chercher&affiliationId=<%=viewBean.getIdAffiliation()%>"> nouvelle aff.</a>
				&nbsp;
					<a href="<%=servletContext%>/phenix?userAction=phenix.suivis.radiationAffiliation.chercher&affiliationId=<%=viewBean.getIdAffiliation()%>">radiation </a>
				</ct:ifhasright>
				<%}%>
		  	</td>
		  </tr>
		  <%
		   	// display error message if needed
			if(session.getAttribute("errorMessage")!=null){
		  %>
          <TR>
            <TD nowrap colspan="6"><br><b><%=session.getAttribute("errorMessage")%></b></TD>
          </TR>		  
		  <%
			  // delete error message
				session.removeAttribute("errorMessage");
			}
		  %>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>