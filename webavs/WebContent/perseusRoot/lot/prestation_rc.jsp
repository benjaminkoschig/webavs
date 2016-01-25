<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>


<%@page import="globaz.corvus.api.lots.IRELot"%><script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
// Les labels de cette page commence par la préfix "JSP_PF_PRE_R"

idEcran="PPF0752";
rememberSearchCriterias = true;
bButtonNew = false;

String selectedId = request.getParameter("selectedId");
String csTypeLot = request.getParameter("csTypeLot");
String csEtatLot = request.getParameter("csEtatLot");
String provenance = request.getParameter("provenance");
String descriptionLot = request.getParameter("descriptionLot");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="perseus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="perseus-optionsempty"/>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "perseus.lot.prestation.lister";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PF_PRE_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_PF_PRE_R_LOT"/></TD>
							<TD colspan="3">
								<INPUT type="text" name="lotDescription" value="<%=descriptionLot%>" class="libelleLongDisabled" readonly="readonly">
								<INPUT type="hidden" name="prestationSearch.forIdLot" value="<%=selectedId%>">
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PF_PRE_R_ETAT"/></TD>
							<TD>
								<INPUT type="text" name="csEtatLotLibelle" value="<%=objSession.getCodeLibelle(csEtatLot)%>" class="disabled" readonly="readonly">
								<INPUT type="hidden" name="csEtatLot" value="<%=csEtatLot%>">
							</TD>
							<TD><ct:FWLabel key="JSP_PF_PRE_R_TYPE"/></TD>
							<TD>
								<INPUT type="text" name="csTypeLotLibelle" value="<%=objSession.getCodeLibelle(csTypeLot)%>" class="disabled" readonly="readonly">
								<INPUT type="hidden" name="csTypeLot" value="<%=csTypeLot%>">
							</TD>
						</TR>

						<TR><TD>&nbsp;</TD></TR>
						<TR>
							<TD><LABEL for="likeNumeroAVS"><ct:FWLabel key="JSP_PF_PRE_R_NSS"/></LABEL>&nbsp;</TD>
							<TD>
								<ct1:nssPopup avsMinNbrDigit="99" 
										  nssMinNbrDigit="99" 
										  newnss="" 
										  name="prestationSearch.likeNss"/>
							</TD>
							<TD><LABEL for="forCsSexe"><ct:FWLabel key="JSP_PF_PRE_R_SEXE"/></LABEL>&nbsp;</TD>
							<TD colspan="3"><ct:FWCodeSelectTag name="prestationSearch.forSexe" codeType="PYSEXE" defaut="" wantBlank="true"/></TD>	
						</TR>
						<TR>
							<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_PF_PRE_R_NOM"/></LABEL>&nbsp;</TD>
							<TD><INPUT type="text" name="prestationSearch.likeNom" value=""></TD>
							<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_PF_PRE_R_PRENOM"/></LABEL>&nbsp;</TD>
							<TD><INPUT type="text" name="prestationSearch.likePrenom" value=""></TD>
							<TD><LABEL for="forDateNaissance"><ct:FWLabel key="JSP_PF_PRE_R_DATENAISSANCE"/></LABEL>&nbsp;</TD>
							<TD><input type="text" name="prestationSearch.forDateNaissance" value="" data-g-calendar="mandatory:false"/></TD>									
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