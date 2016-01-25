<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CGE0005";
	//subTableHeight = 0;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.campus.vb.etudiants.GEEtudiantsViewBean"%>
<SCRIPT>
	usrAction = "campus.lots.lots.lister";
	bFind = true;
</SCRIPT>
	<ct:menuChange displayId="menu" menuId="GEMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="GEMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Recherche des lots
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
            				<TD nowrap width="130">N° du lot</TD>
            				<TD nowrap width="200">
								<INPUT type="text" name="forIdLot" value="" size="14">
	     					</TD>
	     					<TD nowrap width="90">Libellé du lot</TD>
            				<TD nowrap width="200">
								<INPUT type="text" name="forLibelleTraitementLike" value="" size="14">
						    </TD>
						    <TD nowrap width="90">Etat du lot</TD>
            				<TD nowrap width="200">
								<ct:FWCodeSelectTag name="forCsEtatLot"
							    	defaut=""
						      		codeType="GEETAT_LOT"
						      		wantBlank="true"
						      	/>
						    </TD>
          				</TR>
						<TR>
            				<TD nowrap width="128" >Date de réception</TD>
            				<TD>
								<ct:FWCalendarTag name="fromDateReceptionLot" 
									value=""
									doClientValidation="CALENDAR"
							 	/>
							</TD>
							<TD nowrap width="90">Année</TD>
            				<TD nowrap width="200">
								<INPUT type="text" name="forAnnee" value="" size="4">
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