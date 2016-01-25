<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
    globaz.pyxis.db.tiers.TIHistoTiersListViewBean viewBean = (globaz.pyxis.db.tiers.TIHistoTiersListViewBean) request.getAttribute("viewBean");
    size =viewBean.size();
    
	String idTiers = "";
	if(!globaz.jade.client.util.JadeStringUtil.isNull(request.getParameter("idTiers"))){
		idTiers = request.getParameter("idTiers");
	}
    detailLink ="pyxis?userAction=pyxis.tiers.histoTiers.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
		<TH nowrap width="16">&nbsp;</TH>
		<Th><ct:FWLabel key="CHAMP"/></Th>
		<Th><ct:FWLabel key="VALEUR"/></Th>
		<Th><ct:FWLabel key="DE"/></Th>
		<Th><ct:FWLabel key="MOTIF"/></Th>
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%> 
	<%
		globaz.pyxis.db.tiers.TIHistoTiersViewBean line = (globaz.pyxis.db.tiers.TIHistoTiersViewBean) viewBean.getEntity(i);
		actionDetail = "parent.location.href='"+detailLink+line.getIdHistorique()+"&idTiers="+idTiers+"'";
		String dLink = request.getContextPath()+"/"+detailLink + line.getIdHistorique()+"&idTiers="+idTiers;
	 %>
	<TD class="mtd" width="16" >
	 <ct:menuPopup menu="TIMenuVide" detailLabelId="Detail" detailLink="<%=dLink%>" >
	 	<ct:menuParam key="selectedId" value="<%=line.getIdHistorique()%>"/>
	 </ct:menuPopup>
	<%if(globaz.pyxis.db.tiers.TITiers.FIELD_DESIGNATION1.equals(line.getChamp())){%>
		<TD class="mtd" onclick="<%=actionDetail%>"><ct:FWLabel key="NOM_RAISON_SOC"/></TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%="".equals(line.getValeur())?"&nbsp;": line.getValeurDesignation1()%></TD>
	<%}else if(globaz.pyxis.db.tiers.TITiers.FIELD_DESIGNATION2.equals(line.getChamp())){%>
		<TD class="mtd" onclick="<%=actionDetail%>"><ct:FWLabel key="PRENOM_RAISON_SOC"/></TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%="".equals(line.getValeur())?"&nbsp;": line.getValeurDesignation2()%></TD>
	<%}else if(globaz.pyxis.db.tiers.TITiers.FIELD_DESIGNATION3.equals(line.getChamp())){%>
		<TD class="mtd" onclick="<%=actionDetail%>"><ct:FWLabel key="NOM_SUITE_1"/></TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%="".equals(line.getValeur())?"&nbsp;": line.getValeurDesignation3()%></TD>
	<%}else if(globaz.pyxis.db.tiers.TITiers.FIELD_DESIGNATION4.equals(line.getChamp())){%>
		<TD class="mtd" onclick="<%=actionDetail%>"><ct:FWLabel key="NOM_SUITE_2"/></TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%="".equals(line.getValeur())?"&nbsp;": line.getValeurDesignation4()%></TD>
	<%}else if(globaz.pyxis.db.tiers.TITiers.FIELD_TITRE.equals(line.getChamp())){%>
		<TD class="mtd" onclick="<%=actionDetail%>"><ct:FWLabel key="TITRE"/></TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%="".equals(line.getValeur())?"&nbsp;": line.getValeurTitre()%></TD>
	<%}else if(globaz.pyxis.db.tiers.TITiers.FIELD_PAYS.equals(line.getChamp())){%>
		<TD class="mtd" onclick="<%=actionDetail%>"><ct:FWLabel key="NATIONALITE"/></TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%="".equals(line.getValeur())?"&nbsp;": line.getValeurPays()%></TD>
	<%}else if(globaz.pyxis.db.tiers.TITiers.FIELD_DATE_DECES.equals(line.getChamp())){%>
		<TD class="mtd" onclick="<%=actionDetail%>"><ct:FWLabel key="DECES"/></TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%="".equals(line.getValeur())?"&nbsp;": line.getValeurDate()%></TD>
	<%}%>
	<TD class="mtd" onClick="<%=actionDetail%>"><%="".equals(line.getDateDebut())?"&nbsp;": line.getDateDebut()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%="".equals(line.getMotif())?"&nbsp;": line.getSession().getCodeLibelle(line.getMotif())%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>