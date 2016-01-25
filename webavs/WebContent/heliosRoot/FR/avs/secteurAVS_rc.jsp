<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ page import="globaz.helios.db.interfaces.*,globaz.helios.db.comptes.*,globaz.helios.translation.*" %>
<%
		idEcran="GCF4002";
		globaz.helios.db.avs.CGSecteurAVSListViewBean viewBean = (globaz.helios.db.avs.CGSecteurAVSListViewBean) session.getAttribute("viewBean");
		actionNew+="&idMandat="+viewBean.getForIdMandat();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CG-mandat" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getForIdMandat()%>"/>
	<ct:menuSetAllParams key="forIdMandat" value="<%=viewBean.getForIdMandat()%>"/>
</ct:menuChange>

<SCRIPT>
	usrAction = "helios.avs.secteurAVS.lister";
	bFind = true;
</SCRIPT>		
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Secteurs AVS<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
			<tr>
				<td>Mandat<input type="hidden" name="forIdMandat" value="<%=viewBean.getForIdMandat()%>"></td>
				<td><input name='_libelle' class='libelleLongDisabled' readonly value='<%=viewBean.getMandat().getLibelle()%>'></td>
				<td>A partir de</td>
				<td><input type="text" name="fromSecteur" class="libelle"></td>
			</tr>
		<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>