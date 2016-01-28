<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%

globaz.lyra.vb.parametres.LYApercuParametresListViewBean viewBean = (globaz.lyra.vb.parametres.LYApercuParametresListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize();

detailLink = baseLink + "afficher&selectedId=";

menuName = "ly-menuprincipal";
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    	<TH></TH>
    		<TH><ct:FWLabel key="JSP_PARAMETRES_ID"/></TH>
    		<TH><ct:FWLabel key="JSP_NOMPARAMETRE"/></TH>
    		<TH><ct:FWLabel key="JSP_TYPEPARAMETRE"/></TH>
    		<TH><ct:FWLabel key="JSP_VALEURPARDEFAUT"/></TH>
    		<TH><ct:FWLabel key="JSP_LIBELLE_DE"/></TH>
    		<TH><ct:FWLabel key="JSP_LIBELLE_FR"/></TH>
    		<TH><ct:FWLabel key="JSP_LIBELLE_IT"/></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			globaz.lyra.vb.parametres.LYApercuParametresViewBean courant = (globaz.lyra.vb.parametres.LYApercuParametresViewBean) viewBean.get(i);
			String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdParametres() + "'";
		%>
		
		<TD class="mtd" width="">&nbsp;</TD>
		
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getIdParametres()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getNomParametre()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getLibelleTypeParametre()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getLibelleDefaultValue()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getLibelleParametreDE()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getLibelleParametreFR()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getLibelleParametreIT()%></TD>
		
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>