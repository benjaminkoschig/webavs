<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	COParamTaxesListViewBean viewBean = (COParamTaxesListViewBean)session.getAttribute("listViewBean");
	session.setAttribute("listViewBeanTaxe", viewBean);
	COParamTaxesViewBean _paramTaxes = null;
	size = viewBean.size();
	detailLink ="aquila?userAction=aquila.batch.trancheTaxe.chercher&selectedId=";
	BSession s = viewBean.getSession();
	HashMap map = new HashMap();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>

<%@page import="globaz.aquila.db.batch.COParamTaxesViewBean"%>
<%@page import="globaz.aquila.db.batch.COParamTaxesListViewBean"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="java.util.HashMap"%>

	<th></th>
	<th>Id Taxe</th>
	<th>Etape</th>
	<th>Type de taxes</th>
	<th>Libell&eacute;</th>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>
<%
	_paramTaxes = (COParamTaxesViewBean) viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+String.valueOf(i)+"&idTaxe="+_paramTaxes.getIdTaxe()+"&typeTaxeEtape="+_paramTaxes.getTypeTaxeEtape()+
					"&Etape="+_paramTaxes.getEtape()+"&Libelle="+_paramTaxes.getLibelle()+"'";
%>
	<td class="mtd" width="16">
	<ct:menuPopup menu="CO-ParamTaxe" label="<%=optionsPopupLabel%>" target="top.fr_main">
		<ct:menuParam key="selectedId" value="<%=String.valueOf(i)%>"/>
		<ct:menuParam key="idTaxe" value="<%=_paramTaxes.getIdTaxe()%>"/>
		<ct:menuParam key="idEtape" value="<%=_paramTaxes.getIdEtape()%>"/>
		<ct:menuParam key="imputerTaxes" value="<%=_paramTaxes.getImputerTaxes().toString()%>"/>
		<ct:menuParam key="idRubrique" value="<%=_paramTaxes.getIdRubrique()%>"/>
		<ct:menuParam key="montantFixe" value="<%=_paramTaxes.getMontantFixe()%>"/>
		<ct:menuParam key="baseTaxe" value="<%=_paramTaxes.getBaseTaxe()%>"/>
		<ct:menuParam key="idTraduction" value="<%=_paramTaxes.getIdTraduction()%>"/>
		<ct:menuParam key="typeTaxe" value="<%=_paramTaxes.getTypeTaxe()%>"/>
		<ct:menuParam key="typeTaxeEtape" value="<%=_paramTaxes.getTypeTaxeEtape()%>"/>
		<ct:menuParam key="Etape" value="<%=_paramTaxes.getEtape()%>"/>
		<ct:menuParam key="Libelle" value="<%=_paramTaxes.getLibelle()%>"/>
		<ct:menuParam key="idSequence" value="<%=_paramTaxes.getIdSequence()%>"/>
	</ct:menuPopup>
	</td>
	<td class="mtd" width="20" align="center" onClick="<%=actionDetail%>"><%=_paramTaxes.getIdTaxe()%></td>
    <td class="mtd" width="30%" onClick="<%=actionDetail%>"><%=s.getCodeLibelle(_paramTaxes.getEtape())%></td>
    <td class="mtd" width="30%" onClick="<%=actionDetail%>"><%=s.getCodeLibelle(_paramTaxes.getTypeTaxeEtape())%></td>
    <td class="mtd" width="*" onClick="<%=actionDetail%>"><%=_paramTaxes.getLibelle()%></td>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>