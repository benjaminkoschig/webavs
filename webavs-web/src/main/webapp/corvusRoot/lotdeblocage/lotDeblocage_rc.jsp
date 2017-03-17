<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@page import="globaz.corvus.vb.lotdeblocage.RELotDeblocageViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
// Les labels de cette page commence par la préfix "JSP_PRE_R"

idEcran="PRE0104";
rememberSearchCriterias = true;
bButtonNew = false;

String selectedId = request.getParameter("selectedId");
String csTypeLot = request.getParameter("csTypeLot");
String csEtatLot = request.getParameter("csEtatLot");
String provenance = request.getParameter("provenance");
String descriptionLot = request.getParameter("descriptionLot");


RELotDeblocageViewBean viewBean = new RELotDeblocageViewBean();
viewBean.setSession(objSession);
viewBean.setIdLot(selectedId);

%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionslot" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=selectedId%>"/>
	<ct:menuSetAllParams key="csTypeLot" value="<%=csTypeLot%>"/>
	<ct:menuSetAllParams key="csEtatLot" value="<%=csEtatLot%>"/>
	<ct:menuSetAllParams key="provenance" value="<%=provenance%>"/>
	<ct:menuSetAllParams key="descriptionLot" value="<%=descriptionLot%>"/>
	<ct:menuActivateNode nodeId="prestation"  active="no"/>
	<%if (globaz.corvus.api.lots.IRELot.CS_ETAT_LOT_VALIDE.equals(csEtatLot)) {%>
		<ct:menuActivateNode nodeId="comptabiliser" active="no"/>
	<%} else{%>
		<ct:menuActivateNode nodeId="comptabiliser" active="yes"/>
	<%}%>
</ct:menuChange>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "corvus.lotdeblocage.lotDeblocage.lister";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PRE_R_LOT_DEBLOCAGE_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%if(provenance.equals(globaz.corvus.vb.prestations.REPrestationsJointTiersViewBean.FROM_ECRAN_LOTS)){%>
						<TR>
							<TD><ct:FWLabel key="JSP_PRE_R_LOT"/></TD>
							<TD colspan="3">
								<INPUT type="text" name="lotDescription" value="<%=viewBean.getLotDescription()%>" class="libelleLongDisabled" readonly="readonly">
								<INPUT type="hidden" name="forIdLot" value="<%=selectedId%>">
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PRE_R_ETAT"/></TD>
							<TD>
								<INPUT type="text" name="csEtatLotLibelle" value="<%=viewBean.getCsEtatLotLibelle()%>" class="disabled" readonly="readonly">
								<INPUT type="hidden" name="csEtatLot" value="<%=csEtatLot%>">
							</TD>
							<TD><ct:FWLabel key="JSP_PRE_R_TYPE"/></TD>
							<TD>
								<INPUT type="text" name="csTypeLotLibelle" value="<%=viewBean.getCsTypeLotLibelle()%>" class="disabled" readonly="readonly">
								<INPUT type="hidden" name="csTypeLot" value="<%=csTypeLot%>">
							</TD>
						</TR>
						<%}else{%>
						<TR>
							<TD><ct:FWLabel key="JSP_PRE_R_DECISION"/></TD>
							<TD colspan="3">
								<INPUT type="text" name="decisionDescription" value="<%=selectedId%>" class="libelleLongDisabled" readonly="readonly">
								<INPUT type="hidden" name="forIdDecision" value="<%=selectedId%>">
							</TD>
						</TR>
						<%}%>
						<TR><TD>&nbsp;</TD></TR>
						<TR>
							<TD><LABEL for="likeNumeroAVS"><ct:FWLabel key="JSP_DRE_R_NSS"/></LABEL>&nbsp;</TD>
							<TD>
								<ct1:nssPopup avsMinNbrDigit="99" 
										  nssMinNbrDigit="99" 
										  newnss="" 
										  name="likeNumeroAVS"/>
							
							</TD>
							<TD><LABEL for="forCsSexe"><ct:FWLabel key="JSP_DRE_R_SEXE"/></LABEL>&nbsp;</TD>
							<TD colspan="3"><ct:FWCodeSelectTag name="forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true"/></TD>	
						</TR>
						<TR>
							<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_DRE_R_NOM"/></LABEL>&nbsp;</TD>
							<TD><INPUT type="text" name="likeNom" value=""></TD>
							<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_DRE_R_PRENOM"/></LABEL>&nbsp;</TD>
							<TD><INPUT type="text" name="likePrenom" value=""></TD>
							<TD>
								<LABEL for="forDateNaissance">
									<ct:FWLabel key="JSP_DRE_R_DATENAISSANCE"/>
								</LABEL>
								&nbsp;
								<input	id="forDateNaissance"
										name="forDateNaissance"
										data-g-calendar="yearRange:¦1900:<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>¦"
										value="" />
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