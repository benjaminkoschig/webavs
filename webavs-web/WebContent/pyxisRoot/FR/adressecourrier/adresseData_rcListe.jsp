 
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
    globaz.pyxis.vb.adressecourrier.TIAdresseDataListViewBean viewBean = (globaz.pyxis.vb.adressecourrier.TIAdresseDataListViewBean )request.getAttribute ("viewBean");
    size = viewBean.getSize ();
    detailLink ="pyxis?userAction=pyxis.adressecourrier.adresse.afficher&selectedId=";
    session.setAttribute("listViewBean",viewBean);

%>

<style>
	.date {font-size:7pt;}
	.marker {font-family : webdings; color: red}
</style>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
    <Th width="16">&nbsp;</Th>
<%
// si critere = rue npa, on n'affiche pas le nom
if (!"512011".equals(viewBean.getReqCritere()))  {%>
    <Th >Tiers</Th>
<%}%>
	<Th >Destinataire</Th>
    <Th >Rue</Th>
    <Th >N°</Th>
    <Th >Case postale</Th>
    <Th >Localité</Th>
    <!--
    <Th >Domaine/Type</Th>
    -->
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%> 
<%

	globaz.pyxis.db.adressecourrier.TIAdresseData entity = (globaz.pyxis.db.adressecourrier.TIAdresseData) viewBean.getEntity(i);

     actionDetail = "parent.location.href='"+detailLink+entity.getIdAdresseUnique()+"'";
%>
    <TD class="mtd" width="16" >
    
    
    <%String url = request.getContextPath()+"/pyxis?userAction=pyxis.adressecourrier.adresse.afficher&selectedId="+entity.getIdAdresseUnique();%>
	<ct:menuPopup menu="TIMenuVide" detailLabelId="Detail" detailLink="<%=url%>" />
    
    
    
    </TD>
<%
// si critere = rue npa, on n'affiche pas le nom
if (!"512011".equals(viewBean.getReqCritere()))  {%>
    <TD class="mtd" onClick="<%=actionDetail%>"><%=entity.getDesignation1_tiers()+" "+entity.getDesignation2_tiers()%>&nbsp;</TD>
<%}%>
    <TD class="mtd" onClick="<%=actionDetail%>"><b><%=entity.getDesignation1_adr()+" "+entity.getDesignation2_adr()%></b>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>"><%=entity.getRue()%>&nbsp;</TD>
    <TD class="mtd" align="right" onClick="<%=actionDetail%>"><%=entity.getNumero()%>&nbsp;</TD>
    <TD class="mtd" align="right" onClick="<%=actionDetail%>"><%=entity.getCasePostale()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>">
    	<%=entity.getPaysIso()%>&nbsp;-
    	<%=entity.getNpa()%>&nbsp;
    	<%=entity.getLocalite()%>&nbsp;</TD>
   
	   	


<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  

<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>