<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- 

INFO !!!!
Les labels de cette page sont prefixes avec 'LABEL_JSP_COT_R'

--%>
<%
idEcran="PIJ0009";

globaz.ij.vb.prestations.IJCotisationViewBean viewBean = (globaz.ij.vb.prestations.IJCotisationViewBean) request.getAttribute("viewBean");

IFrameDetailHeight = "260";

bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT>
	bFind = true;
	usrAction = "<%=globaz.ij.servlet.IIJActions.ACTION_COTISATIONS%>.lister";
	detailLink = servlet + "?userAction=<%=globaz.ij.servlet.IIJActions.ACTION_COTISATIONS%>.afficher&_method=add";
	
	function onClickNew() {
		// desactive le grisage des boutons
	}
	
	function retourARepartition() {
		document.location.href = servlet + '?userAction=<%=globaz.ij.servlet.IIJActions.ACTION_REPARTITION_PAIEMENTS%>.chercher&idPrestation=<%=viewBean.getIdPrestation()%>';
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_COT_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD>
							<INPUT type="hidden" name="forIdRepartitionPaiements" value="<%=viewBean.getIdRepartitionPaiement()%>">
						
							<TABLE border="0">
								<TR>
									<TD><b><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></b></TD>
									<TD colspan="5"><INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly></TD>
								</TR>
								<TR>
									<TD><ct:FWLabel key="JSP_COT_R_NO_BASE_INDEMNISATION"/></TD>
									<TD><INPUT type="text" name="noBaseIndemnisation" value="<%=viewBean.getIdBaseIndemnisation()%>" class="libelleLongDisabled" readonly></TD>
									<TD><ct:FWLabel key="JSP_DU"/></TD>
									<TD><INPUT type="text" name="dateDebutBaseIndemnisation"  value="<%=viewBean.getDateDebutBaseIndemnisation()%>" class="dateDisabled" readonly></TD>
									<TD><ct:FWLabel key="JSP_AU"/></TD>
									<TD><INPUT type="text" name="dateFinBaseIndemnisation" value="<%=viewBean.getDateFinBaseIndemnisation()%>" class="dateDisabled" readonly></TD>
								</TR>
								<TR>
									<TD colspan="6"><HR></TD>
								</TR>
								<TR>
									<TD colspan="6"><H6><ct:FWLabel key="JSP_COT_R_BENEFICIAIRE"/></H6></TD>
								</TR>
								<TR>
									<TD><ct:FWLabel key="JSP_COT_R_NOM_BENEFICIAIRE"/></TD>
									<TD><INPUT type="text" name="nomBeneficiaireRepartition" value="<%=viewBean.getNomBeneficiaireRepartition()%>" class="disabled" readonly></TD>
									<TD><ct:FWLabel key="JSP_COT_R_MONTANT_PRESTATION"/></TD>
									<TD colspan="3"><INPUT type="text" name="montantBrutRepartition" value="<%=viewBean.getMontantBrutRepartition()%>" class="montantDisabled" readonly></TD>
								</TR>
							</TABLE>
						</TD></TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<P align="right"><INPUT type="button" value="<ct:FWLabel key="JSP_RETOUR_A_REPARTITIONS"/>" onclick="retourARepartition()"></P>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>