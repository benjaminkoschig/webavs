<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
    rememberSearchCriterias = true;
	idEcran ="CAF0033";
	actionNew =  servletContext + mainServletPath + "?userAction=" + request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".afficher&_method=add";
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);
	AFControleEmployeurViewBean bean = new AFControleEmployeurViewBean();
	
	String idAff;
	idAff = request.getParameter("_idAff");	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.naos.db.controleEmployeur.AFControleEmployeurViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT>
	usrAction = "naos.controleEmployeur.controleEmployeur.lister";
</SCRIPT>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Arbeitgeberkontrolle
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%
							String numAffilie="";
							if (JadeStringUtil.isBlank(idAff)){
								numAffilie = bean.getNumAffilie(request.getParameter("affiliationId"), objSession);
							}else{
								numAffilie = idAff;
							}
						%>
						
						<TR>
							<TD nowrap width="140" height="31">Abr.-Nr.<input type="hidden" name="idAFF" value="<%=idAff%>"/></TD>
							<TD nowrap width="220">
								<ct:FWPopupList name="forNumAffilie" 
									value="<%=numAffilie%>" 
									className="libelle" 
									jspName="<%=jspLocation%>" 
									autoNbrDigit="<%=autoDigiAff%>" 
									size="15"
									minNbrDigit="3"
								/>
								<IMG
									src="<%=servletContext%>/images/down.gif"
									alt="presser sur la touche 'flèche bas' pour effectuer une recherche"
									title="presser sur la touche 'flèche bas' pour effectuer une recherche"
									onclick="if (document.forms[0].elements('forAffiliationId').value != '') forAffilieNumeroPopupTag.validate();">
							</TD>
							<TD nowrap width="140" height="31">Jahr</TD>
							<TD nowrap width="220"> 
								<INPUT name="forAnnee" type="text" size="10">
							</TD>
							<TD nowrap width="140" height="31">Berichtnummer</TD>
							<TD nowrap width="220"> 
								<INPUT name="likeNouveauNumRapport" type="text" size="12">
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