<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CGE0003";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@page import="globaz.campus.vb.etudiants.GEEtudiantsViewBean"%>
<%@page import="globaz.campus.vb.etudiants.GEEtudiantsAjoutViewBean"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<SCRIPT>
	usrAction = "campus.etudiants.etudiants.lister";
	bFind = true;
</SCRIPT>
	<ct:menuChange displayId="menu" menuId="GEMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="GEMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Recherche des étudiants
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR height="30">
            				<TD width="140">N° d'immatriculation</TD>
            				<TD width="250">
								<INPUT name="forNumImmatriculation" type="text" size="21" 
								value='<%=request.getParameter("forNumImmatriculation")!=null?request.getParameter("forNumImmatriculation"):""%>'/>
							</TD>
	            			<TD width="70">Nom</TD>
            				<TD width="250">
								<INPUT type="text" name="forNomLike" value='<%=request.getParameter("forNomLike")!=null?request.getParameter("forNomLike"):""%>' size="21">
	     					</TD>
          				</TR>
          				<TR height="30">
            				<TD>NSS</TD>
            				<TD>
            				<nss:nssPopup name="forNumAvsLike" cssclass="libelle"
	            			avsAutoNbrDigit="11" nssAutoNbrDigit="10"  avsMinNbrDigit="5" nssMinNbrDigit="8" 
	            			value='<%=request.getParameter("forNumAvsLike")!=null?request.getParameter("forNumAvsLike"):""%>'/>
							</TD>
							<TD>Prénom</TD>
	           				<TD>
								<INPUT type="text" name="forPrenomLike" 
								value='<%=request.getParameter("forPrenomLike")!=null?request.getParameter("forPrenomLike"):""%>' size="21">
	     					</TD>
          				</TR>
          				<TR height="30">
          					<TD width="60">Ecole</TD>
            				<TD colspan="3">
            					<ct:FWListSelectTag name="forIdTiersEcole" data="<%=GEEtudiantsViewBean.getIdsEtNomsEcole(session)%>" defaut=""/> 									
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