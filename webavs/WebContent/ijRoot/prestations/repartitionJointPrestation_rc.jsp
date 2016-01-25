<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--

INFO !!!!
les labels de cette page sont prefixes avec 'LABEL_JSP_REP_R'

--%>
<%
idEcran="PIJ0016";
globaz.ij.vb.prestations.IJRepartitionJointPrestationViewBean viewBean = (globaz.ij.vb.prestations.IJRepartitionJointPrestationViewBean) request.getAttribute("viewBean");
IFrameListHeight = "100";
IFrameDetailHeight = "320";
actionNew+="&idPrestation=" + viewBean.getIdPrestation();

globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

bButtonNew = bButtonNew && !(IIJPrononce.CS_ALLOC_ASSIST.equals(viewBean.getCsTypeIJ()) || IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(viewBean.getCsTypeIJ())) && 
			 objSession.hasRight(globaz.ij.servlet.IIJActions.ACTION_REPARTITION_PAIEMENTS +".ajoute","ADD");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.ij.api.prononces.IIJPrononce"%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT>
	bFind = true;
	usrAction = "<%=globaz.ij.servlet.IIJActions.ACTION_REPARTITION_PAIEMENTS%>.lister";
	detailLink = servlet + "?userAction=<%=globaz.ij.servlet.IIJActions.ACTION_REPARTITION_PAIEMENTS%>.afficher&idPrononce=<%=viewBean.getIdPrononce()%>&csTypeIJ=<%=viewBean.getCsTypeIJ()%>&idBaseIndemnisation=<%=viewBean.getIdBaseIndemnisation()%>&_method=add";
	
	function onClickNew() {
		// desactive le grisage des boutons
	}
	
	function prestationSuivante() {
		document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_REPARTITION_PAIEMENTS%>.actionPrestationSuivante";
		document.forms[0].target = "fr_main";
		document.forms[0].submit();
	}
	
	function prestationPrecedante() {
		document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_REPARTITION_PAIEMENTS%>.actionPrestationPrecedante";
		document.forms[0].target = "fr_main";
		document.forms[0].submit();
	}
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_REP_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD>
							<TABLE border="0" width="100%">
								<TR><INPUT type="hidden" name="forIdPrestation" value="<%=viewBean.getIdPrestation()%>">
									<TD><input type="hidden" value="<%=viewBean.getNoAVSAssure()%>" name= "noAVS"><input type="hidden" value="<%=viewBean.getIdTiers()%>" name= "idTiers"> <b><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></b></TD>
									<TD colspan="5"><INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly></TD>
								</TR>
								<TR>	
									<TD><ct:FWLabel key="JSP_REP_R_DATE_PRONONCE"/></TD>
									<TD>
										<INPUT type="text" name="datePrononce" value="<%=viewBean.getDatePrononce()%>" class="dateDisabled" readonly>
									</TD>
									<TD colspan="4">&nbsp;</TD>
								</TR>
								<TR><TD colspan="6"><HR></TD></TR>
								<TR>
									<TD colspan="6"><H6><ct:FWLabel key="JSP_REP_R_PRESTATION"/></H6></TD>
								</TR>
								<TR>
									<TD><ct:FWLabel key="JSP_REP_R_NO_BASEINDEMNISATION"/></TD>
									<TD><INPUT type="text" name="noBaseIndemnisation" value="<%=viewBean.getIdBaseIndemnisation()%>" class="montantDisabled" readonly></TD>
									<TD><ct:FWLabel key="JSP_DU"/></TD>
									<TD><INPUT type="text" name="dateDebutBaseIndemnisation" value="<%=viewBean.getDateDebutBaseIndemnisation()%>" class="date disabled" readonly></TD>
									<TD><ct:FWLabel key="JSP_AU"/></TD>
									<TD><INPUT type="text" name="dateFinBaseIndemnisation" value="<%=viewBean.getDateFinBaseIndemnisation()%>" class="date disabled" readonly></TD>
								</TR>
								<TR>
									<TD><ct:FWLabel key="JSP_REP_R_MONTANT_GLOBAL"/></TD>
									<TD><INPUT type="text" name="montantBrutPrestation" value="<%=viewBean.getMontantBrutPrestation()%>" class="montantDisabled" readonly></TD>
									<TD colspan="4">
										<A href="<%=servletContext + mainServletPath + "?userAction=" + globaz.ij.servlet.IIJActions.ACTION_PRESTATION_JOINT_LOT_PRONONCE + ".chercher&forNoBaseIndemnisation=" + viewBean.getIdBaseIndemnisation()%>">
											<ct:FWLabel key="JSP_REP_R_DETAIL_PRESTATIONS"/>
										</A>
									</TD>
								</TR>
							</TABLE>
						</TD></TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>