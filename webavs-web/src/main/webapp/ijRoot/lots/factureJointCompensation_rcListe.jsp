<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%

globaz.ij.vb.lots.IJFactureJointCompensationListViewBean viewBean = (globaz.ij.vb.lots.IJFactureJointCompensationListViewBean) request.getAttribute("viewBean");
globaz.prestation.tools.PRIterateurHierarchique iterH = viewBean.iterateurHierarchique();

size = viewBean.getSize ();
detailLink = servletContext + mainServletPath + "?userAction=" + globaz.ij.servlet.IIJActions.ACTION_FACTURES_LOT + ".afficher&selectedId=";	
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>
<SCRIPT language="JavaScript">
	function afficherCacher(id) {
		if (document.all("groupe_" + id).style.display == "none") {
			document.all("groupe_" + id).style.display = "block";
			document.all("bouton_" + id).value = "-";
		} else {
			document.all("groupe_" + id).style.display = "none";
			document.all("bouton_" + id).value = "+";
		}
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH></TH>
    <TH><ct:FWLabel key="JSP_NO_AFFILIE"/></TH>
    <TH><ct:FWLabel key="JSP_BENEFICIAIRES"/></TH>
    <TH><ct:FWLabel key="JSP_MONTANT_TOTAL"/></TH>
    <TH><ct:FWLabel key="JSP_DETTES"/></TH>
    <TH><ct:FWLabel key="JSP_MONTANT_A_COMPENSER"/></TH>
    <TH><ct:FWLabel key="JSP_NO_FACTURE"/></TH>
    <TH><ct:FWLabel key="JSP_COMPENSER"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
<%
		globaz.ij.vb.lots.IJFactureJointCompensationViewBean courant = null;
		try {
			courant = (globaz.ij.vb.lots.IJFactureJointCompensationViewBean) iterH.next();
		} catch (Exception e) {
			break;
		}

	String detailUrl = "parent.fr_detail.location.href='" + detailLink;
	
	if (courant.getIsLigneCompensation().booleanValue()) {
		detailUrl += courant.getIdCompensation() + "&_method=add";
	} else {
		detailUrl += courant.getIdFactureACompenser();
	}
	
	detailUrl += "&idLot=" + courant.getIdLot();
	detailUrl += "&idTiers=" + courant.getIdTiersCompensation() + "'";
	
	if (iterH.isPositionPlusPetite()) {
		%></TBODY><%
	} else if (iterH.isPositionPlusGrande()) {
		%><TBODY id="groupe_<%=courant.getIdParent()%>" style="display: none;"><%
	} 
%>    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<TD class="mtd"<% if (!courant.getIsLigneCompensation().booleanValue()) { %> onclick="<%=detailUrl%>"<% } %>d>
			<% for (int idPosition = 1; idPosition < iterH.getPosition(); ++idPosition) { %>--<% } %>
			<% if (iterH.isPere()) { %>
			<INPUT type="button" id="bouton_<%=courant.getIdMajeur()%>" value="+" onclick="afficherCacher(<%=courant.getIdMajeur()%>)">
			<% }else{ %>
			&nbsp;
			<% }%>
		</TD>
		<TD class="mtd" nowrap align="right" onclick="<%=detailUrl%>"> <%=courant.getNumAffilie()%>&nbsp;</TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getNomPrenomTiers()%>&nbsp;</TD>
		<TD class="mtd" nowrap align="right" onclick="<%=detailUrl%>"><%=courant.getMontantTotal()%>&nbsp;</TD>
		<TD class="mtd" nowrap align="right" onclick="<%=detailUrl%>"><%=courant.getIsLigneCompensation().booleanValue()?courant.getDettes():""%>&nbsp;</TD>
		<TD class="mtd" nowrap align="right" onclick="<%=detailUrl%>"><%=courant.getMontant()%>&nbsp;</TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getNoFacture()%>&nbsp;</TD>
		<TD class="mtd" nowrap align="center" onclick="<%=detailUrl%>"><IMG alt="" src="<%=request.getContextPath()+courant.getIsCompenserImageSrc()%>">&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>