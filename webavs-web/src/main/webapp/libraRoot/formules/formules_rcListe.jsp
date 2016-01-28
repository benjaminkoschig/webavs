<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%

	globaz.libra.vb.formules.LIFormulesListViewBean viewBean = (globaz.libra.vb.formules.LIFormulesListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();

	detailLink = "libra?userAction="+globaz.libra.servlet.ILIActions.ACTION_FORMULES_DE + ".afficher&selectedId=";
	
	menuName = "li-menuprincipal";
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    
	    	<TH>&nbsp;</TH>
    		<TH><ct:FWLabel key="ECRAN_RCL_FORM_LIBELLE"/></TH> 
    		<TH><ct:FWLabel key="ECRAN_RCL_FORM_DOMAINE"/></TH> 
    		<TH><ct:FWLabel key="ECRAN_RCL_FORM_NOM_CLASSE"/></TH>
	 		
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			globaz.libra.vb.formules.LIFormulesViewBean courant = (globaz.libra.vb.formules.LIFormulesViewBean) viewBean.get(i);
			
			String detailUrl = "parent.location.href='" + detailLink + courant.getIdFormule()+"'";
	
		%>
		
		<TD class="mtd" width="15">
	     	<ct:menuPopup menu="libra-optionsformules">
	     			<ct:menuParam key="selectedId" value="<%=courant.getIdFormule()%>"/>  
		 	</ct:menuPopup>
     	</TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%= courant.getSession().getCodeLibelle(courant.getDefinitionFormule().getCsDocument()) %></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%= courant.getSession().getCodeLibelle(courant.getCsDomaine()) %></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%= courant.getNomClasse() %></TD>
     	
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>