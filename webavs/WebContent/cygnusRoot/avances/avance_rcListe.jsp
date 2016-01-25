<%@page language="java" errorPage="/errorPage.jsp" %>

<%@page import="globaz.cygnus.vb.avances.RFAvanceListViewBean"%>
<%@page import="globaz.cygnus.vb.avances.RFAvanceViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/list/header.jspf" %>

<%
	// Les labels de cette page commence par le préfix "JSP_LOT_L"	
	RFAvanceListViewBean viewBean = (RFAvanceListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "cygnus?userAction=cygnus.avances.avance.afficher&selectedId=";
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
%>
<%@ include file="/theme/list/javascripts.jspf" %>
					<th>
						&nbsp;
					</th>
					<th>
						<ct:FWLabel key="JSP_AVANCE_L_REQUERANT_INFO" />
					</th>
					<th>
						<ct:FWLabel key="JSP_AVANCE_L_DOMAINE" />
					</th>
					<th>
						<ct:FWLabel key="JSP_AVANCE_L_1ER_ACOMPTE" />
					</th>
					<th>
						<ct:FWLabel key="JSP_AVANCE_L_ACOMPTE_MENSUEL" />
					</th>
					<th>
						<ct:FWLabel key="JSP_AVANCE_L_PERIODE" />
					</th>
					<th>
						<ct:FWLabel key="JSP_AVANCE_L_ETAT" />
					</th>  
					<th>
						<ct:FWLabel key="JSP_AVANCE_L_NO" />
					</th>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%
	RFAvanceViewBean line = (RFAvanceViewBean) viewBean.getEntity(i);
	actionDetail = targetLocation  + "='" + detailLink + line.getIdAvance() + "'";
%>
					<td class="mtd" width="" nowrap>
						<ct:menuPopup	menu="cygnus-optionsavances" 
										detailLabelId="MENU_OPTION_DETAIL" 
										detailLink="<%=detailLink + line.getIdAvance()%>">
						</ct:menuPopup>
					</td>
					<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
						<%=line.getDetailRequerant()%>
						&nbsp;
					</td>
					<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
						<%=objSession.getCodeLibelle(line.getCsDomaineAvance())%>
						&nbsp;
					</td>
					<td align="right" class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
						<%=line.getMontant1erAcompte()%>
						&nbsp;
					</td>
					<td align="right" class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
						<%=line.getMontantMensuel()%>
						&nbsp;
					</td>
<%
	String periode = "";
	if (!JadeStringUtil.isBlankOrZero(line.getDateDebutAcompte())) {
		periode = line.getDateDebutAcompte();
	}
	periode += " - ";
	periode += line.getDateFinAcompte();
%>					<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
						<%=periode%>
						&nbsp;
					</td>
					<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
						<%=line.getCsEtatAcomptesLibelle()%>
						&nbsp;
					</td>
					<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
						<%=line.getIdAvance()%>
						&nbsp;
					</td>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%@ include file="/theme/list/tableEnd.jspf" %>