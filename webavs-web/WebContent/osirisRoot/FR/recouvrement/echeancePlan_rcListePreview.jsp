<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.osiris.db.recouvrement.*"%>
<%
	CAEcheancePlanListViewBean viewBean = new CAEcheancePlanListViewBean();
	CAPlanRecouvrementViewBean pr = (CAPlanRecouvrementViewBean)request.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
	java.util.Vector vec = pr.getTempEcheances();
	//size = viewBean.size();
	size = vec.size();
	detailLink = "osiris?userAction=osiris.recouvrement.echeancePlan.afficher&selectedId=";
	float total=0;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<th>Date exigibilité</th>
	<th>Montant</th>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
		globaz.osiris.db.access.recouvrement.CAEcheancePlan line = (globaz.osiris.db.access.recouvrement.CAEcheancePlan) vec.elementAt(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdEcheancePlan() + "'";
	%>

	<td class="mtd" nowrap="nowrap" ><%=line.getDateExigibilite()%>&nbsp;</td>
	<td class="mtd" nowrap="nowrap" style="text-align: right;"><%=globaz.globall.util.JANumberFormatter.format(line.getMontant())%>&nbsp;CHF</td>
	<%total+= Float.parseFloat(line.getMontant());%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<tr>
		<td colspan=2><hr></td>
	</tr>
	<tr>
		<td class="mtd" nowrap="nowrap" >
			Nombre d'échéances : <%=vec.size()%>
		</td>
		<td class=mtd nowrap="nowrap" align=right>
			Montant total : <%=globaz.globall.util.JANumberFormatter.format(total)%> CHF
		</td>
	</tr>
	<tr>
		<td class="mtd" nowrap="nowrap" colspan="2" align="center">
			<%if(objSession.hasRight("osiris.recouvrement.echeancePlan.calculerSave", FWSecureConstants.UPDATE)) { %>
				<input type="button" value="Accepter le plan" onClick="parent.location.href='<%=servletContext + mainServletPath %>?userAction=osiris.recouvrement.echeancePlan.calculerSave&selectedId=<%=request.getParameter("selectedId")%>&mode=save';">
			<%}%>
		<td>
	</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>