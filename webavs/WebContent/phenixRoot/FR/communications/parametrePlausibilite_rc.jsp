<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
	idEcran="CCP1009";
	String idPlausibilite = request.getParameter("idPlausibilite");
	if (idPlausibilite == null) {
		idPlausibilite = request.getParameter("selectedId");
	}
	actionNew =  servletContext + mainServletPath + "?userAction=" + "phenix.communications.parametrePlausibilite.afficher&_method=add&idPlausibilite="+idPlausibilite;
	globaz.phenix.db.communications.CPReglePlausibiliteViewBean viewBean = new globaz.phenix.db.communications.CPReglePlausibiliteViewBean();
	viewBean.getReglePlausibilite(idPlausibilite, session);
	IFrameListHeight = "200";
	IFrameDetailHeight = "320";

%>

<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT>
// menu 
top.document.title = "Configuration des paramètres de plausibilité"
detailLink = servlet+"?userAction=phenix.communications.parametrePlausibilite.afficher&_method=add&idPlausibilite=<%=idPlausibilite%>";
usrAction = "phenix.communications.parametrePlausibilite.lister";
bFind=true;
</SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Configuration des paramètres de plausibilité<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
		    <TR> 
              <TD nowrap width="170" align="left" >Règle de plausibilité</TD>
              <TD nowrap width="547" align="left" > 
                <INPUT name="nomPlausibilite" class="libelleLongDisabled" readonly value="<%=viewBean.getIdPlausibilite() + " - " + viewBean.getDescription_fr()%>">
              </TD>
            <TD width="65" height="20">
            	<input type="hidden" name="forIdPlausibilite" value="<%=viewBean.getIdPlausibilite()%>"/>
            	<input type="hidden" name="idPlausibilite" value="<%=viewBean.getIdPlausibilite()%>"/>
            </TD>
 	       </TR>
						
		
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>