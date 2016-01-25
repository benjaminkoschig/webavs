<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.tucana.db.bouclement.TUDetailListViewBean viewBean = (globaz.tucana.db.bouclement.TUDetailListViewBean) request.getAttribute("viewBean");
    size = viewBean.getSize();
	detailLink = "tucana?userAction=tucana.bouclement.detail.afficher&idBouclement="+request.getParameter("idBouclement")+"&idDetail=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
		<TH>&nbsp;</TH>
		<TH><ct:FWLabel key="RUBRIQUE" /></TH>
		<TH><ct:FWLabel key="LIBELLE" /></TH>
		<TH><ct:FWLabel key="CANTON" /></TH>
		<TH><ct:FWLabel key="MONTANT_NOMBRE" /></TH>
		<TH><ct:FWLabel key="ORIGINE" /></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
			<%
			globaz.tucana.db.bouclement.TUDetailViewBean line = (globaz.tucana.db.bouclement.TUDetailViewBean) viewBean.getEntity(i);
			actionDetail = targetLocation  + "='" + detailLink + line.getIdDetail() +"'";	
			
			%>
			<TD class="mtd" width="">		
				<%
					String detLink = detailLink + line.getIdDetail();
				%>
				<ct:menuPopup menu="OVTUDetail"
					target="top.fr_main" detailLink="<%=detLink%>" detailLabelId="MNU_DETAIL" labelId="MNU_OPTIONS">
					<ct:menuParam key="idBouclement" value="<%=line.getIdBouclement()%>"/>
				</ct:menuPopup>
			</TD>
			<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getCsRubrique())?"&nbsp;":viewBean.getSession().getCode(line.getCsRubrique())%></TD>
			<TD title="<%=line.getCsRubrique()%>" class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getCsRubrique())?"&nbsp;":viewBean.getSession().getCodeLibelle(line.getCsRubrique())%></TD>
			<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getCanton())?"&nbsp;":line.getCanton()%></TD>
			<TD class="mtd" align="right" onclick="<%=actionDetail%>"><%=line.getNombreMontant()%></TD>
			<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getCsTypeRubrique())?"&nbsp;":viewBean.getSession().getCodeLibelle(line.getCsTypeRubrique())%></TD>

		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>