<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
	globaz.pyxis.db.fusiontiers.TISelectTiersListViewBean viewBean = (globaz.pyxis.db.fusiontiers.TISelectTiersListViewBean) request.getAttribute ("viewBean");
    size = viewBean.size();
%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%><style>
	.date {font-size:7pt;}
	.marker {font-family : webdings; color: red}
</style>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
      <TH  align="left"><ct:FWLabel key="NSS_RENTIER"/></TH>
      <TH  align="left"><ct:FWLabel key="NUM_AFFILIE"/></TH>
      <%globaz.pyxis.application.TIApplication app = (globaz.pyxis.application.TIApplication) globaz.globall.db.GlobazServer.getCurrentSystem().getApplication("PYXIS");
			 if (app.isIdTiersExterneVisible()) {%>
      	<TH  align="left"><ct:FWLabel key="NUM_INTERNE"/></TH>
      <%}%>
      <TH  align="left"><ct:FWLabel key="NOM_LISTE_ANNONCES"/></TH>
      <TH  align="left">&nbsp;</TH>
      <TH  align="left"><ct:FWLabel key="LANGUE"/></TH>
      <TH  align="left"><ct:FWLabel key="ADRESSE"/></TH>
    <% 
    boolean oldCondition = true;
    boolean sameTiers = false;
    boolean sameAffiliation = false;
    int nbAffiliation = 0;
    
    int pos = -1;
    %>
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%
    
    String prevLine="";
    String actuLine="";
	    	String subLineColor="";
	    	if (i==0) { oldCondition = condition; }
			else {
				globaz.pyxis.db.fusiontiers.TISelectTiersViewbean prevEntity = (globaz.pyxis.db.fusiontiers.TISelectTiersViewbean)viewBean.getEntity(i-1);
				globaz.pyxis.db.fusiontiers.TISelectTiersViewbean actuEntity = (globaz.pyxis.db.fusiontiers.TISelectTiersViewbean)viewBean.getEntity(i);
				
				prevLine = prevEntity.getIdTiers()+prevEntity.getNumAffilieActuel()+prevEntity.getDebutActivite()+prevEntity.getFinActivite();
				actuLine = actuEntity.getIdTiers()+actuEntity.getNumAffilieActuel()+actuEntity.getDebutActivite()+actuEntity.getFinActivite();
				if(!prevLine.equals(actuLine)){
					sameAffiliation = false;
					nbAffiliation++;
				} else {
					sameAffiliation = true;
				} 
				
				prevLine = prevEntity.getIdTiers();
				actuLine = actuEntity.getIdTiers();
				if(!prevLine.equals(actuLine)){
					condition = !oldCondition;
					oldCondition = condition;
					sameTiers= false;
					nbAffiliation=0;
				} else {
					condition = oldCondition;
					sameTiers= true;
				} 
			
			}    
    %>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%>
<%
	globaz.pyxis.db.fusiontiers.TISelectTiersViewbean entity = (globaz.pyxis.db.fusiontiers.TISelectTiersViewbean) viewBean.getEntity(i);
	actionDetail = "document.body.innerHTML='';parent.location.href='" + globaz.fweb.taglib.FWChooserTag.getSelectLink(pageContext, i) + "'";	
%>
<%
	if  (!sameTiers) {
		pos ++;
%>

	<TD valign ="top" class="mtd" onClick="<%=actionDetail%>" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;" ><%=entity.getNumAvsActuel()%>&nbsp;</TD>
	<TD valign ="top" class="mtd" onClick="<%=actionDetail%>" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;" ><span id="aff<%=entity.getIdTiers()%>"></span>&nbsp;</TD>
<%if (app.isIdTiersExterneVisible()) {%>
	<TD valign ="top" class="mtd" onClick="<%=actionDetail%>" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;" ><%=entity.idTiersExterneFormate()%>&nbsp;</TD>
<%}%>
	<TD valign ="top" class="mtd" onClick="<%=actionDetail%>" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;" >
		<span  style="color:red"><%=(entity.isDead())?"( ":" "%></span>
		<%
			String nom = entity.getDesignation1_tiers();
			if (!JadeStringUtil.isEmpty(entity.getDesignation2_tiers())) {
				nom += " " + entity.getDesignation2_tiers();
			}
			if (!JadeStringUtil.isEmpty(entity.getDesignation3_tiers())) {
				nom += " - " + entity.getDesignation3_tiers();
			}
		%>
		<%=nom%>
		<span style="color:red"><%=(entity.isDead())?" )":" "%></span>
		<%=(entity.isDead())?"<br>":" "%>
		<span style="font-family:wingdings"><%=(entity.isDead())?"U":" "%></span>
		<%=(entity.isDead())?entity.getDateDeces():" "%>
		<br>
	</TD>
	
	<td valign ="top" class="mtd" onClick="<%=actionDetail%>" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">
		<%=(entity.getCsTypeTiers()!=null)?" "+entity.getSession().getCodeLibelle(entity.getCsTypeTiers()):" &nbsp;"%>
	</td>
		
	<td valign ="top" class="mtd" onClick="<%=actionDetail%>" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">
		<%=(entity.getLangue()!=null)?" "+entity.getSession().getCode(entity.getLangue()):" &nbsp;"%>
	</td>
<%} else {%>
	<% // pas de menu si on est en mode selection.
	if (!((request.getParameter("colonneSelection")!=null)&&(request.getParameter("colonneSelection").equals("yes")))) {
     %>
		<td class="mtd"  onClick="<%=actionDetail%>" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">&nbsp;</td>
	<%}%>
	<td class="mtd"  onClick="<%=actionDetail%>" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">&nbsp;</td>
	<td class="mtd"  onClick="<%=actionDetail%>" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">&nbsp;</td>
	<td class="mtd"  onClick="<%=actionDetail%>" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">&nbsp;</td>
<%}%>

<% if (!sameAffiliation) {%>
		<script>
				var el = document.getElementById("aff<%=entity.getIdTiers()%>");
				if (el.innerHTML !="") {
					el.innerHTML+="<br><br>";
				}
				el.innerHTML += "<b><%=entity.getNumAffilieActuel() + (!JadeStringUtil.isEmpty(entity.getAffiliationProvisoire()) && (entity.getAffiliationProvisoire().equals("1"))?" P":"")%></b><br><span class='date'><%=("".equals(entity.getDebutActivite()+entity.getFinActivite()))?"":entity.getDebutActivite()+"-"+entity.getFinActivite()%></span>";
		</script>
	<% }%> 
<%
	if (condition) {
		subLineColor="#F0F0F0";
	} else {
		subLineColor="#D0D5EA";
	}
%>
<%if (nbAffiliation == 0) {%>
	<TD valign='top' class="mtd" onClick="<%=actionDetail%>" style="<%=(sameTiers)?"border-top:solid 1px "+subLineColor:"border-top:solid 1px silver"%>;" >
		<%
			String rueComplete="";
			String rue= entity.getRue();
			String num = entity.getNumero();
			String cp = entity.getCasePostaleComp();
			
			if (!"".equals(rue+num)) {rueComplete = rue+" "+num+"<br>";}
			if (!"".equals(cp))  {cp+="<br>";}
			//pageContext.getOut().write(rueComplete+cp);
			
			rueComplete = globaz.globall.util.JAUtil.replaceString(rueComplete,"\"","&quot;");
			
		%>
		<%if  (!sameTiers) {%>
			<span id="adr<%=entity.getIdTiers()%>"></span>
		<%} %>
		
		
	</TD>
	<script>
		<%if (!"".equals(entity.getIdApplication())) {%>
			var el = document.getElementById("adr<%=entity.getIdTiers()%>");
			if (el.innerHTML !="") {
						el.innerHTML+="<br>";
			}
			<%if(!viewBean.getForSingleAdresseMode().booleanValue()){%>
				el.innerHTML +="<%=((globaz.pyxis.constantes.IConstantes.CS_AVOIR_ADRESSE_DOMICILE.equals(entity.getTypeAdresse()) && globaz.pyxis.constantes.IConstantes.CS_APPLICATION_DEFAUT.equals(entity.getIdApplication()))?"<span class='marker' >4</span>":"")%><b><%=entity.getSession().getCodeLibelle(entity.getIdApplication())%></b>/<%=entity.getSession().getCodeLibelle(entity.getTypeAdresse())%>&nbsp;"+
				"<span class='date'><%=("".equals(entity.getDateDebutRelation()))?"...":entity.getDateDebutRelation()%>-<%=("".equals(entity.getDateFinRelation()))?"...":entity.getDateFinRelation()%></span>"+
				"<div style='border: solid 1px silver;margin:2 2 2 2;padding:4 4 4 4'><%=rueComplete+cp%><%=entity.getNpa()%>&nbsp;<%=entity.getLocalite()%></div>";
			<%} else {%>
			el.innerHTML+="<%=rueComplete+cp%><%=entity.getNpa()%>&nbsp;<%=entity.getLocalite()%>";
			<%}%>
			
		
		<%} else {%>
			var el = document.getElementById("adr<%=entity.getIdTiers()%>");
			el.innerHTML +="&nbsp;"
		<%}%>
	</script>
	
<%} else {%>
   	<td>&nbsp;</td>
<%}%>
	
  
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