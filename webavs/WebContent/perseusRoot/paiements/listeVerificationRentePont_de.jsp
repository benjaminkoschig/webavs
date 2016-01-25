<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.perseus.vb.paiements.PFListeVerificationRentePontViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

	<%
		
		PFListeVerificationRentePontViewBean viewBean = (PFListeVerificationRentePontViewBean) session.getAttribute("viewBean"); 
		idEcran="PPF0831";
		autoShowErrorPopup = true;
	
		bButtonDelete = false;
		bButtonUpdate = false;
		
	%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="MENU_PF_LISTE_VERIFICATION_RP"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
						
						<tr>
						<%if(objSession.hasRight("perseus", FWSecureConstants.ADD)){ %>
							<td colspan="2" align="center">
								<a data-g-download="docType:xls,
													parametres:<%=viewBean.getMoisCourant() %>,
								                    serviceClassName:ch.globaz.perseus.business.services.doc.excel.ListeDeControleService,
								                    serviceMethodName:createListeVerificationRentePontAndSave,
								                    docName:listeVerificationRentePont"
								/>
							</td>
							<%} %>
						</tr>
						
						<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
