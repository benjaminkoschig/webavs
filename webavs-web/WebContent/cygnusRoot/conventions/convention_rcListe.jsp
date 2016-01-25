<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	//Les labels de cette page commence par le préfix "JSP_RF_??"
	RFRechercheConventionListViewBean viewBean = (RFRechercheConventionListViewBean) request.getAttribute("viewBean");	
	size = viewBean.getSize();

	detailLink = "cygnus?userAction="+IRFActions.ACTION_CONVENTION+ ".afficher&selectedId=";
	
	menuName = "cygnus-menuprincipal";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.api.demandes.IRFDemande"%>
<%@page import="globaz.cygnus.vb.conventions.RFRechercheConventionListViewBean"%>
<%@page import="globaz.cygnus.vb.conventions.RFRechercheConventionViewBean"%>

<%@page import="globaz.cygnus.utils.RFUtils"%><script language="JavaScript">

</script>
   		
		<TH colspan="2"><ct:FWLabel key="JSP_RF_CONV_LIBELLE"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_RC_CONV_NUMERO"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_RC_CONV_DETAIL_REQUERANT"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_RC_CONV_TYPE"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_RC_CONV_SOUS_TYPE"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_RC_CONV_FOURNISSEUR"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_RC_CONV_SOUS_GEST"/></TH>
   		
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			
		RFRechercheConventionViewBean courant = (RFRechercheConventionViewBean) viewBean.get(i);
		
		String urlForMenuPopUp = detailLink + courant.getIdConvention() +
		   "&idConvention=" + courant.getIdConvention() +
		   "&libelle=" + courant.getTextLibelle() +
		   "&isActif=" + courant.getIsActif() +
		   "&idGestionnaire=" + courant.getVisaGestionnaire() +
		   "&dateCreation=" + courant.getDateCreation();
		
		String detailUrl = "parent.location.href='" + urlForMenuPopUp +"'";
				
		%>		
		
		<TD class="mtd" nowrap width="20px">
	     	<ct:menuPopup menu="cygnus-optionsconventions" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=urlForMenuPopUp%>">
		 	</ct:menuPopup>
     	</TD>		
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getTextLibelle() %></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIdConvention() %></TD>     	
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=JadeStringUtil.isEmpty(courant.getNss())?" - ":courant.getDetailAssure() %></TD>     	
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><a style="color:black;text-decoration:none;" href='#' title='<%=viewBean.getSession().getCodeLibelle(courant.getIdTypeSoin())%>' ><%=(courant.getForParConvention().equals(Boolean.TRUE)||JadeStringUtil.isEmpty(courant.getTypeSoin()))?"&nbsp;":courant.getTypeSoin() %></a></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><a style="color:black;text-decoration:none;" href='#' title='<%=viewBean.getSession().getCodeLibelle(courant.getIdSousTypeSoin())%>' ><%=(courant.getForParConvention().equals(Boolean.TRUE)||JadeStringUtil.isEmpty(courant.getSousTypeSoin()))?"&nbsp;":courant.getSousTypeSoin() %></a></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=(courant.getForParConvention().equals(Boolean.TRUE)||JadeStringUtil.isEmpty(courant.getFournisseur()))?"&nbsp;":courant.getFournisseur()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=(courant.getForParConvention().equals(Boolean.TRUE)||JadeStringUtil.isEmpty(courant.getVisaGestionnaire()))?"&nbsp;":courant.getVisaGestionnaire()%></TD>		
			
				
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>