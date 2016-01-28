<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%@page import="globaz.draco.application.DSApplication"%>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "CDS0003";
rememberSearchCriterias = true;%>
<%@page import="globaz.draco.translation.*"%>
<%@page import="globaz.draco.db.declaration.*"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%
globaz.draco.db.declaration.DSDeclarationViewBean viewBean = (globaz.draco.db.declaration.DSDeclarationViewBean)session.getAttribute ("viewBeanRC");
actionNew = actionNew + "&idDeclaration=" + viewBean.getIdDeclaration();
bButtonNew = objSession.hasRight("draco.declaration.ligneDeclaration.afficher","ADD");

String gedFolderType = "";
String gedServiceName = "";
try {
	globaz.globall.api.BIApplication osiApp = globaz.globall.api.GlobazSystem.getApplication(DSApplication.DEFAULT_APPLICATION_DRACO);
	gedFolderType = osiApp.getProperty("ged.folder.type", "");
	gedServiceName = osiApp.getProperty("ged.servicename.id", "");
} catch (Exception e){
	// Le reste de la page doit tout de même fonctionner
}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
usrAction = "draco.declaration.ligneDeclaration.lister";
bFind = true;
detailLink = servlet+"?userAction=draco.declaration.ligneDeclaration.afficher&_method=add&idDeclaration=<%=viewBean.getIdDeclaration()%>";
</SCRIPT>
<ct:menuChange displayId="options" menuId="DS-OptionsDeclaration" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="idDeclaration" checkAdd="no" value="<%=viewBean.getIdDeclaration()%>"/>
	<ct:menuSetAllParams key="selectedId" checkAdd="no" value="<%=viewBean.getIdDeclaration()%>"/>
</ct:menuChange>
	
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Saisie 
        des lignes des déclarations de salaires<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD>Affili&eacute;</TD>
            <TD>&nbsp;</TD>
            <TD><input type="text" name="numAffilie" value="<%=viewBean.getAffiliation().getAffilieNumero()%>" class="disabled" size="15" readonly tabindex="-1"></TD>
            <TD>&nbsp;</TD>
            <TD colspan="6"> 
            <input type="text" name="nom" value="<%=viewBean.getAffiliation().getTiers().getNom()%>" size="47" tabindex="-1" class="disabled" readonly>
            <input type="hidden" name="forIdDeclaration" value="<%=viewBean.getIdDeclaration()%>">
            </TD>
            <TD>&nbsp;</TD>
            <TD>Date de r&eacute;ception </TD>
            <TD>&nbsp;</TD>
            <TD>
              <input type="text" name="dateRetourEf" size="10" value="<%=viewBean.getDateRetourEff()%>" tabindex="-1" class="dateDisabled" readonly >
            </TD>
            <TD>&nbsp;</TD>
            <TD>Genre</TD>
            <TD>&nbsp;</TD>
            <TD> 
              <input type="text" name="typeAff" size="20" value="<%if (viewBean.getAffiliation() != null) {%><%=CodeSystem.getLibelle(session, viewBean.getAffiliation().getTypeAffiliation())%><%}%>" tabindex="-1" class="disabled" readonly>
            </TD>
          </TR>
          
          
          <TR> 
            <TD>Affilié d&egrave;s</TD>
            <TD>&nbsp;</TD>
            <TD> 
              <input type="text" name="affilieDes" size="10"  value="<%if (viewBean.getAffiliation() != null) {%><%=viewBean.getAffiliation().getDateDebut()%><%}%>" tabindex="-1" class="dateDisabled" readonly >
            </TD>
            <TD>&nbsp;</TD>
            <TD>Radi&eacute;s d&egrave;s</TD>
            <TD>&nbsp;</TD>
            <TD> 
              <input type="text" name="radieDes" size="10" value="<%if (viewBean.getAffiliation() != null) {%><%=viewBean.getAffiliation().getDateFin()%><%}%>" maxlength="4" tabindex="-1" class="dateDisabled" readonly >
            </TD>
            <TD>&nbsp;</TD>
            <TD >D&eacute;claration</TD>
            <TD> 
              <input name="typeDeclaration" value="<%=CodeSystem.getLibelle(session,viewBean.getTypeDeclaration())%>" readonly class="disabled" size="25" tabindex="-1">
            </TD>
            <TD>&nbsp;</TD>
            <TD>Ann&eacute;e</TD>
            <TD>&nbsp;</TD>
            <TD> 
            <%		String annee="";
					if (!JadeStringUtil.equals(viewBean.getAnnee(),"0",true)) {
						annee = viewBean.getAnnee();
					}
            %>		
              <INPUT name="annee" size="4" value="<%=annee%>" type="text" class="disabled" readonly tabindex="-1">
            </TD>
            <TD>&nbsp;</TD>
            <TD>Etat</TD>
            <TD>&nbsp;</TD>
              <TD><INPUT type="text" name="etatEcran" value="<%=CodeSystem.getLibelle(session, viewBean.getEtat())%>" class="disabled" readonly tabindex="-1">
          </TR>
          <tr>
          <TD valign="top"  width="100">
														<%
										             		String gedAffilieNumero = viewBean.getAffiliation().getAffilieNumero();
										             		String gedNumAvs = viewBean.getAffiliation().getTiers().getNumAvsActuel();
										             		String gedIdTiers = viewBean.getIdTiers();
										             		String gedIdRole = "";
										             	%>
														
														<%@ include file="/theme/gedCall.jspf" %>
													</TD>
          </tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>