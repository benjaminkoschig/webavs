<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
    globaz.pyxis.db.tiers.TIBanqueAdresseListViewBean viewBean = (globaz.pyxis.db.tiers.TIBanqueAdresseListViewBean)request.getAttribute ("viewBean");
    size = viewBean.size ();
    session.setAttribute("listViewBean",viewBean);
	menuName="tiers-banque";
	detailLink ="pyxis?userAction=pyxis.tiers.banque.afficher&selectedId=";

%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<style>
	.date {font-size:7pt;}
	.marker {font-family : webdings; color: red}
</style>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
  


	<Th nowrap width="16">&nbsp;</Th>

      
    <Th>Clearing</Th>
    <Th>Swift</Th>
    <Th>Iban</Th>
    <Th>Name</Th>
    <TH>Postkonto-Nr.</TH>
    <Th>Adresse</Th>
    <TH>Bereich/Typ</TH>

    
    <% 
    boolean oldCondition = true;
    boolean sameTiers = false;
    int pos = -1;
    %>
    
    
      <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
	<%
       	String subLineColor="";
	    	if (i==0) { oldCondition = condition; }
			else {
				if(!((globaz.pyxis.db.tiers.TIBanqueAdresse)viewBean.getEntity(i-1)).getIdTiers().equals(((globaz.pyxis.db.tiers.TIBanqueAdresse)viewBean.getEntity(i)).getIdTiers())){
					condition = !oldCondition;
					oldCondition = condition;
					sameTiers= false;
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
	globaz.pyxis.db.tiers.TIBanqueAdresse entity = (globaz.pyxis.db.tiers.TIBanqueAdresse) viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+entity.getIdTiersBanque()+"'";

%>
<%
	if  (!sameTiers) {
		pos ++;
%>

      <TD class="mtd" width="16" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">
      
      	<%String url = request.getContextPath()+"/pyxis?userAction=pyxis.tiers.banque.afficher&selectedId="+entity.getIdTiersBanque();%>
	<ct:menuPopup menu="tiers-banque" detailLabelId="Detail" detailLink="<%=url%>">
	 <ct:menuParam key="idTiers" value="<%=entity.getIdTiersBanque()%>"/>
	</ct:menuPopup> 

      
      
      </TD>
      
      
      
      <TD class="mtd" onClick="<%=actionDetail%>" align="right" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;"><%=entity.getClearing()%>&nbsp;<%=(JadeStringUtil.isIntegerEmpty(entity.getNewClearing()))?"":" ("+entity.getNewClearing()+")"%></TD>
      <TD class="mtd" onClick="<%=actionDetail%>" align="right" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;"><%=entity.getSwift()%>&nbsp;</TD>
      <TD class="mtd" onClick="<%=actionDetail%>" align="right" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;"><%=entity.getIban()%>&nbsp;</TD>
      <TD class="mtd" onClick="<%=actionDetail%>" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;"><%=entity.getDesignation1_tiers()+" "+entity.getDesignation2_tiers()%>&nbsp;</TD>
 	  <TD class="mtd" onClick="<%=actionDetail%>" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">
      <DIV align="center"><%=entity.getCcp()%>&nbsp;</DIV>
      </TD>
<%} else {%>
      <TD class="mtd" width="16" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">&nbsp;</TD>
      <TD class="mtd" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;" >&nbsp;</TD>
      <TD class="mtd" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">&nbsp;</TD>
      <TD class="mtd" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">&nbsp;</TD>
      <TD class="mtd" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">&nbsp;</TD>
      <TD class="mtd" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">&nbsp;</TD>
<%}%>
<%
	if (condition) {
		subLineColor="#F0F0F0";
	} else {
		subLineColor="#D0D5EA";
	}
%>

	<TD class="mtd" onClick="<%=actionDetail%>" style="<%=(sameTiers)?"border-top:solid 1px "+subLineColor:"border-top:solid 1px silver"%>;" >
		<i>
		<%
			String rueComplete="";
			String rue= entity.getRue();
			String num = entity.getNumero();
			String cp = entity.getCasePostaleComp();
			
			if (!"".equals(rue+num)) {rueComplete = rue+" "+num+"<br>";}
			if (!"".equals(cp))  {cp+="<br>";}
			pageContext.getOut().write(rueComplete+cp);
		%>
		</i>
		<b><%=entity.getNpa()%></b>&nbsp;<%=entity.getLocalite()%>
	
	</TD>
   	<TD class="mtd" onClick="<%=actionDetail%>" style="<%=(sameTiers)?"border-top:solid 1px "+subLineColor:"border-top:solid 1px silver"%>;">
	   	<%if (!"".equals(entity.getIdApplication())) {
		   
		   	// faire ressortir les adresses domicile
		   	if(globaz.pyxis.constantes.IConstantes.CS_AVOIR_ADRESSE_DOMICILE.equals(entity.getTypeAdresse()) && 
		   	  globaz.pyxis.constantes.IConstantes.CS_APPLICATION_DEFAUT.equals(entity.getIdApplication())) {
		   		pageContext.getOut().write("<span class='marker' >4</span>");
		   	}
		   	
		%>
		   	<b><%=entity.getSession().getCodeLibelle(entity.getIdApplication())%></b> / 
		   	<%=entity.getSession().getCodeLibelle(entity.getTypeAdresse())%>
	   		<br>
	   		<span class="date">
	   			<%=("".equals(entity.getDateDebutRelation()))?"...":entity.getDateDebutRelation()%>-
	   			<%=("".equals(entity.getDateFinRelation()))?"...":entity.getDateFinRelation()%>
	   		</span>
	   	<%}%>
   		&nbsp;
   	</TD>     
      <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>