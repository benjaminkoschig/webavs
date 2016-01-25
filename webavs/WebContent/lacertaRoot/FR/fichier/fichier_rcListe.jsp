<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
    LAFichierCentralListViewBean viewBean = (LAFichierCentralListViewBean)request.getAttribute ("viewBean");
    size = viewBean.size ();   
	menuName="tiers-detail";
	//detailLink ="pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=";
	detailLink ="lacerta?userAction=lacerta.fichier.afficher&selectedId=";
%>
<%@page import="db.LAFichierCentralListViewBean"%>
<%@page import="db.LAFichierCentralViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.naos.translation.CodeSystem"%>
<style>
	.date {font-size:7pt;}
	.marker {font-family : webdings; color: red}
</style>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
<!-- <Th width="">Identifiant</Th> -->
      <TH width="1%"></TH>     
      <TH  align="left" width="5%">NSS</TH>
      <TH  align="left" width="5%">N° Affilié</TH>
      <TH  align="left" width="7%">Genre</TH>
      <%globaz.pyxis.application.TIApplication app = (globaz.pyxis.application.TIApplication) globaz.globall.db.GlobazServer.getCurrentSystem().getApplication("PYXIS");
			 if (app.isIdTiersExterneVisible()) {%>
      <TH  align="left" width="5%">N° Interne</TH>
      <%}%>
      <TH  align="left" width="10%">Nom</TH>
      <TH  align="left" width="10%">Adresse</TH>
      <TH  align="left" width="5%">Type Caisse</TH>
      <TH  align="left" width="2%">N° Caisse</TH>
      <TH  align="left" width="5%">Période</TH>
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%   
	String subLineColor="";   
    %>
    
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%>
<%
	LAFichierCentralViewBean entity = (LAFichierCentralViewBean) viewBean.getEntity(i);
	if ((request.getParameter("colonneSelection")==null)||(!request.getParameter("colonneSelection").equals("yes"))) {
		// on ne peut pas aller au detail en mode selection
		actionDetail = "parent.location.href='"+detailLink+entity.getIdTiers()+"&idAffiliation="+entity.getIdAffiliation()+"'";
	}
%>	
	<TD valign="top" class="mtd" width="16">
	<%String url = request.getContextPath()+"/lacerta?userAction=lacerta.fichier.afficher&selectedId="+entity.getIdTiers()+"&idAffiliation="+entity.getIdAffiliation();%>
	<ct:menuPopup menu="tiers-detail" detailLabelId="Detail" detailLink="<%=url%>">
	 <ct:menuParam key="idTiers" value="<%=entity.getIdTiers()%>"/>
	 <ct:menuParam key="selectedId" value="<%=entity.getIdTiers()%>"/>
	</ct:menuPopup> 
	</td>
	

	<TD valign ="top" class="mtd" onClick="<%=actionDetail%>"><%=entity.getNumAvsActuel()%>&nbsp;</TD>
	<%//Ici on défini le champ pour le numAffilié %>
	<TD valign ="top" class="mtd" onClick="<%=actionDetail%>"><%=(JadeStringUtil.isEmpty(entity.getNumAffilieActuel()))?entity.getNumAffilieFC():entity.getNumAffilieActuel()%>&nbsp;</TD>
	<TD valign ="top" class="mtd" onClick="<%=actionDetail%>"><%=entity.getTypeAffiliationLibelle()%>&nbsp;</TD>
<%
			 if (app.isIdTiersExterneVisible()) {%>
	<TD valign ="top" class="mtd" onClick="<%=actionDetail%>"><%=entity.idTiersExterneFormate()%>&nbsp;</TD>
<%}%>
	<TD valign ="top" class="mtd" onClick="<%=actionDetail%>">
		<span  style="color:red"><%=(entity.isDead())?"( ":" "%></span>
		<%=entity.getDesignation1_tiers()%>&nbsp;<%=entity.getDesignation2_tiers()%>
		
		<span style="color:red"><%=(entity.isDead())?" )":" "%></span>
		<%=(entity.isDead())?"<br>":" "%>
		<span style="font-family:wingdings"><%=(entity.isDead())?"U":" "%></span>
		<%=(entity.isDead())?entity.getDateDeces():" "%>
		
		
		<br>
	</TD>


	<%
		String rueComplete = "";
		String rue = entity.getRue();
		String num = entity.getNumero();
		String cp = entity.getCasePostaleComp();

		if (!"".equals(rue + num)) {
			rueComplete = rue + " " + num + "<br>";
		}
		if (!"".equals(cp)) {
			cp += "<br>";
		}
		//pageContext.getOut().write(rueComplete+cp);

		rueComplete = globaz.globall.util.JAUtil.replaceString(rueComplete, "\"", "&quot;");
	%>
	<TD class="mtd" width="8%" onclick="<%=actionDetail%>" align="left"><%=rueComplete + cp%><%=entity.getNpa()%>&nbsp;<%=entity.getLocalite()%></TD>
	<TD valign ="top" class="mtd" onClick="<%=actionDetail%>"><%=entity.getTypeCaisseLibelle()%>&nbsp;</TD>
	<TD valign ="top" class="mtd" onClick="<%=actionDetail%>"><%=entity.getNumCaisse()%>&nbsp;</TD>
	<%if(entity.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_FICHIER_CENT)){%>
		<%if((JadeStringUtil.isEmpty(entity.getDateFinCaisse()))||(entity.getDateFinCaisse()=="0")){ %>
			<TD valign ="top" class="mtd" onClick="<%=actionDetail%>"><%=entity.getDateDebutCaisse()%>&nbsp;</TD>
		<%}else{ %>
			<TD valign ="top" class="mtd" onClick="<%=actionDetail%>"><%=entity.getDateDebutCaisse()+" - "+entity.getDateFinCaisse()%>&nbsp;</TD>
		<%} %>
	<%}else{ %>
	<%if((JadeStringUtil.isEmpty(entity.getDateFinAffiliation()))||(entity.getDateFinAffiliation()=="0")){ %>
			<TD valign ="top" class="mtd" onClick="<%=actionDetail%>"><%=entity.getDateDebutAffiliation()%>&nbsp;</TD>
		<%}else{ %>
			<TD valign ="top" class="mtd" onClick="<%=actionDetail%>"><%=entity.getDateDebutAffiliation()+" - "+entity.getDateFinAffiliation()%>&nbsp;</TD>
		<%} %>
	<%} %>
  
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<script>
	
	
	var table = document.getElementsByTagName("table")[0];
			var trs = table.getElementsByTagName("tr");
			for (i=trs.length-1;i>0;i--) {
				var tds = trs[i].getElementsByTagName("td");
				var line = "";
				
				<%
					if (!((request.getParameter("colonneSelection")!=null)&&(request.getParameter("colonneSelection").equals("yes")))) {
     			%>
					for (j = 0;j<tds.length;j++) {
				<% } else { %> 
					for (j = 1;j<tds.length;j++) {
				
				<%}%>
		
		
					li = tds[j].innerHTML;
					li = li.replace(" ","");
					if (li != "&nbsp;") {
					line+=li;
					}
				}
				
				if (line == ""){
					table.deleteRow(i);
				}
			}
			
</script>




<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>