<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.perseus.vb.lot.PFGenererListeOrdresVersementViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la pr?fix "JSP_PF_LOV"

	
	idEcran="PPF0721";
	
	PFGenererListeOrdresVersementViewBean viewBean = (PFGenererListeOrdresVersementViewBean)session.getAttribute("viewBean");
	
	//userActionValue =IPCActions.ACTION_LOT_ORDRE_VERSEMENT_GENERER_LISTE  + ".executer";
	
	String rootPath = servletContext+(mainServletPath+"Root");
	
	String idLot = request.getParameter("idLot");
	if(JadeStringUtil.isEmpty(idLot)){
		idLot = request.getParameter("selectedId");
	}
	
	String descriptionLot = request.getParameter("descriptionLot");
	String csTypeLot = request.getParameter("csTypeLot");
	String csEtatLot = request.getParameter("csEtatLot");
	String provenance = request.getParameter("provenance");
%>
<%-- /tpl:put --%>

<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>


<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=rootPath %>/scripts/jadeBaseFormulaire.js"></script>

<SCRIPT language="javascript">
$(function(){
	$("#btnCtrlJade").hide();
})
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PF_TITRE_LISTE_ORDRE_VERS"/> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD colspan="2" align="center">
								<LABEL for="descriptionLot">
									<ct:FWLabel key="JSP_LOV_LOT_DESCR"/>
								</LABEL> <%=viewBean.getLot().getSimpleLot().getId()%>&nbsp;/&nbsp;<%=viewBean.getLot().getSimpleLot().getDescription()%>
							</TD>
						</TR>	
						<tr>
							<td  colspan="2">
							&nbsp;
							</td>
						</tr>
						<tr>
						<%if(objSession.hasRight("perseus", FWSecureConstants.ADD)){ %>
							<td colspan="2" align="center">
								<a data-g-download="docType:xls,
													parametres:<%=idLot%>,
								                    serviceClassName:ch.globaz.perseus.business.services.doc.excel.ListeDeControleService,
								                    serviceMethodName:createListeOrdreDeVersementAndSave,
								                    docName:listeOrdreDeVersement"
								/>
								<%}%>
								<!--    perseus?userAction=  <%--  <%=IPCActions.ACTION_DOWNLOAD%>.download&typeDoc=listeOrdreDeVersement&id=<%=idLot%>-->	
							--</td>
						</tr>
						
					<!-- 	<TR>
							<TD><LABEL for="eMailAddress"><ct:FWLabel key="JSP_LOV_D_EMAIL"/></LABEL></TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=controller.getSession().getUserEMail()%>" class="libelleLong"></TD>
						</TR> -->						
		
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>