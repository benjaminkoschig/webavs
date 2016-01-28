<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
    globaz.pyxis.db.tiers.TICompositionTiersListViewBean viewBean = (globaz.pyxis.db.tiers.TICompositionTiersListViewBean )request.getAttribute ("viewBean");
    size = viewBean.size ();
    detailLink ="pyxis?userAction=pyxis.tiers.compositionTiers.afficher&idTiers=";

%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
  


<!-- <Th width="">Identifiant</Th> -->
      <Th nowrap width="16">&nbsp;</Th>
      <TH  align="left">Terzi parent</TH>
      <TH  align="left">Terzi enfant</TH>
      
      <TH align="left">Tipo</TH>
      <TH align="left">Inizio  </TH>
      <TH align="left">Fine  </TH>
    
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%>
<%
	globaz.pyxis.db.tiers.TICompositionTiers entity = (globaz.pyxis.db.tiers.TICompositionTiers)viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+
	
	entity.getIdTiersParent()+"&selectedId="+
	entity.getIdComposition()+"'";

%>
       
    <td class="mtd" width="16" >
	<%String url = request.getContextPath()+"/pyxis?userAction=pyxis.tiers.compositionTiers.afficher&idTiers="+entity.getIdTiersParent()+"&selectedId="+entity.getIdComposition();%>
	<ct:menuPopup menu="TIMenuLienTiers" detailLabelId="Detail" detailLink="<%=url%>">
	 <ct:menuParam key="idTiers" value="<%=entity.getIdTiersParent()%>"/>
	 <ct:menuParam key="idTiersEnfant" value="<%=entity.getIdTiersEnfant()%>"/>
	 <ct:menuParam key="typeLien" value="<%=entity.getTypeLien()%>"/>
	 
	</ct:menuPopup> 
	
	<!--
	<ct:menuPopup menu="TIMenuVide" detailLabelId="Detail" detailLink="<%=url%>">
	</ct:menuPopup> 
	-->
	</td>
       
	<TD class="mtd" onClick="<%=actionDetail%>" width="30%"><%=entity.getNomTiersParent()%>&nbsp;</TD>
	
	<TD class="mtd" onClick="<%=actionDetail%>" width="30%"><%=entity.getNomTiersEnfant()%>&nbsp;</TD>
	
	<TD class="mtd" onClick="<%=actionDetail%>" width="30%"><%=viewBean.getSession().getCodeLibelle(entity.getTypeLien())%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="30%"><%=entity.getDebutRelation()%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="30%"><%=entity.getFinRelation()%>&nbsp;</TD>
   
  
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>