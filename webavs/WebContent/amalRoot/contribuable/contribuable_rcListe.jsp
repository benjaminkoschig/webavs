<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.amal.vb.contribuable.AMContribuableListViewBean"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
	    <%
	    	AMContribuableListViewBean viewBean=(AMContribuableListViewBean)request.getAttribute("viewBean");	    	
   	    	size=viewBean.getSize();
   	    	detailLink = "amal?userAction=amal.contribuable.contribuable.afficher&selectedId=";
	    	    	    	
   	    	menuName = "amal-menuprincipal";	    	    
	    %>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>	   	    
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	    
<%@page import="globaz.amal.vb.contribuable.AMContribuableViewBean"%>

		<TH colspan="2"><ct:FWLabel key="JSP_AM_CON_L_DOSSIER"/></TH>   		
   		<TH><ct:FWLabel key="JSP_AM_CON_L_NOMPRENOM"/></TH>   		
   		<TH><ct:FWLabel key="JSP_AM_CON_L_DATENAISSANCE"/></TH>
   		<TH><ct:FWLabel key="JSP_AM_CON_L_NNSS"/></TH>
   		<TH><ct:FWLabel key="JSP_AM_CON_R_NO_CONTRIBUABLE"/></TH>
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <%
    	AMContribuableViewBean line = (AMContribuableViewBean)viewBean.getEntity(i);
    	boolean contribuableDisabled = !line.getContribuableRcListe().getIsContribuableActif();
        String detailUrl = "parent.location.href='" + detailLink + line.getContribuableRcListe().getId()+"'";
    %>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		
		
		<TD class="mtd" nowrap width="20px">
	     	<ct:menuPopup menu="amal-optionsdossiers" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getContribuableRcListe().getIdContribuable()%>">
	     			<ct:menuParam key="selectedId" value="<%=line.getContribuableRcListe().getIdFamille()%>"/>  
		 	</ct:menuPopup>
     	</TD>
     	
		<TD class="mtd" width="23px" nowrap onClick="<%=actionDetail%>">
			<%if (line.getContribuableRcListe().getIsContribuable()){ %>
				<%if (line.getContribuableRcListe().getIsContribuableActif()){ %>
					C
				<%} else { %>
					C F
				<% } %>
			<% } else { %>
				&nbsp;
			<% } %>			
		</TD>
		
		<%
		boolean showFamilleDetail = false;
		if(viewBean.getContribuableRCListeSearch().getSearchInTiers()!=null){
			if(viewBean.getContribuableRCListeSearch().getSearchInTiers()==false){
				showFamilleDetail = true;
			}
		}else{
			showFamilleDetail = true;
		}
		%>
		<% if(showFamilleDetail==false){%>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>" <%=contribuableDisabled?"style='font-style:italic;color:red'":""%>>
				<% if (JadeStringUtil.isBlankOrZero(line.getContribuableRcListe().getDateDeces())) { %>
					<%=line.getContribuableRcListe().getDesignation1()%>&nbsp;<%=line.getContribuableRcListe().getDesignation2()%>
				<% } else { %>
					<span  style="color:red">(</span>
					<%=line.getContribuableRcListe().getDesignation1()%>&nbsp;<%=line.getContribuableRcListe().getDesignation2()%>
					<span  style="color:red">)</span>
					<span style="font-family:wingdings">U</span>
					<%=line.getContribuableRcListe().getDateDeces() %>
				<% } %>
				<span data-g-note="idExterne:<%=line.getContribuableRcListe().getId()%>,tableSource:¦globaz.amal.vb.contribuable.AMContribuableViewBean¦,inList:true" >
			
			</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>" <%=contribuableDisabled?"style='font-style:italic;color:red'":""%>>
				<%=line.getContribuableRcListe().getDateNaissance()%>&nbsp;
			</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>" <%=contribuableDisabled?"style='font-style:italic;color:red'":""%>>
				<%=line.getContribuableRcListe().getNumAvsActuel()%>&nbsp;
			</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>" <%=contribuableDisabled?"style='font-style:italic;color:red'":""%>>
				<%=line.getContribuableRcListe().getNumContribuable()%>&nbsp;
			</TD>
		<%}else{%>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>">
				<%=line.getContribuableRcListe().getFamilleNomPrenom()%>
			</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getContribuableRcListe().getFamilleDateNaissance()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getContribuableRcListe().getFamilleNoAVS()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>">&nbsp;</TD>
		<%}%>
				
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>