 
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
	globaz.pyxis.vb.tiers.TIAdministrationAdresseListViewBean viewBean = (globaz.pyxis.vb.tiers.TIAdministrationAdresseListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
	session.setAttribute("listViewBean",viewBean);
	detailLink ="pyxis?userAction=pyxis.tiers.administration.afficher&selectedId=";
	menuName ="tiers-administration";
%>
<style>
	.date {font-size:7pt;}
	.marker {font-family : webdings; color: red}
</style>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 


    <th width="16">&nbsp;</th>
    <Th nowrap width="8%">Codice</Th>
      
    <Th width="30%">Cognome</Th>
    <TH width="8%">Genere</TH>
    <Th width="30%">Indirizzo</Th>
    <TH width="24%">Dominio / Tipo</TH>
     
      
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
				if(!((globaz.pyxis.db.tiers.TIAdministrationAdresse)viewBean.getEntity(i-1)).getIdTiers().equals(((globaz.pyxis.db.tiers.TIAdministrationAdresse)viewBean.getEntity(i)).getIdTiers())){
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
	globaz.pyxis.db.tiers.TIAdministrationAdresse entity = (globaz.pyxis.db.tiers.TIAdministrationAdresse) viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+entity.getIdTiers()+"'";
%>
<%
	if  (!sameTiers) {
		pos ++;
%>

      <TD class="mtd" width="16" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">
      
  
	<%String url = request.getContextPath()+"/pyxis?userAction=pyxis.tiers.administration.afficher&selectedId="+entity.getIdTiers();%>
	<ct:menuPopup menu="tiers-administration" detailLabelId="Detail" detailLink="<%=url%>">
	 <ct:menuParam key="idTiers" value="<%=entity.getIdTiers()%>"/>
	</ct:menuPopup> 
      
      
      </TD>
      <TD class="mtd" onClick="<%=actionDetail%>" width="8%" align="right" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;"><%=entity.getCodeAdministration()%>&nbsp;</TD>
      <TD class="mtd" onClick="<%=actionDetail%>" width="30%" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;"><%=entity.getNom()+" "+entity.getDesignation3_tiers()%>&nbsp;</TD>
      <TD class="mtd" onClick="<%=actionDetail%>" width="8%" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">
      <DIV align="center"><%=entity.getSession().getCodeLibelle(entity.getGenreAdministration())%>&nbsp;</DIV>
      </TD>


<%
} else {
%>     
      <TD class="mtd" width="16" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">&nbsp;</TD>
      <TD class="mtd" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;" >&nbsp;</TD>
      <TD class="mtd" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">&nbsp;</TD>
      <TD class="mtd" style="<%=(sameTiers)?"":"border-top:solid 1px silver"%>;">&nbsp;</TD>

<%
}
%> 
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
<!--


      <TD class="mtd" onClick="<%=actionDetail%>" width="30%"><%=entity.getRue()+" "+entity.getNumero()%>&nbsp;</TD>
      <TD class="mtd" onClick="<%=actionDetail%>" width="24%"><%=entity.getNpa()+" "+entity.getLocalite()%>&nbsp;</TD>
-->      
      <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>