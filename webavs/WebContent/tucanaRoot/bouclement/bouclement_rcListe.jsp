<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.tucana.db.bouclement.TUBouclementListViewBean viewBean = (globaz.tucana.db.bouclement.TUBouclementListViewBean) request.getAttribute("viewBean");
    size = viewBean.getSize();

	detailLink = "tucana?userAction=tucana.bouclement.bouclement.afficher&idBouclement=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
		<TH>&nbsp;</TH>
		<TH><ct:FWLabel key="MOIS" /></TH>
		<TH><ct:FWLabel key="ANNEE" /></TH>
		<TH><ct:FWLabel key="NO_CA" /></TH>
		<TH><ct:FWLabel key="NO_CG" /></TH>
		<TH><ct:FWLabel key="NO_ACM" /></TH>
		<TH><ct:FWLabel key="NO_AF" /></TH>
<%-- 	<TH><ct:FWLabel key="SOLDE" /></TH> --%>
		<TH><ct:FWLabel key="DATE_ETAT" /></TH>
		<TH><ct:FWLabel key="AGENCE" /></TH>
		<TH><ct:FWLabel key="ETAT" /></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
			<%
			globaz.tucana.db.bouclement.TUBouclementViewBean line = (globaz.tucana.db.bouclement.TUBouclementViewBean) viewBean.getEntity(i);
			actionDetail = targetLocation  + "='" + detailLink + line.getIdBouclement() +"'";	
			
			%>
			<TD class="mtd" width="">
				<%
					String detLink = detailLink + line.getIdBouclement();
				%>
				<ct:menuPopup menu="OVTUBouclement"
					target="top.fr_main" detailLink="<%=detLink%>" detailLabelId="MNU_DETAIL" labelId="MNU_OPTIONS">
					<ct:menuParam key="idBouclement" value="<%=line.getIdBouclement()%>"/>
					<ct:menuParam key="year" value="<%=globaz.jade.client.util.JadeStringUtil.fillWithZeroes(line.getAnneeComptable(),4)%>"/>
					<ct:menuParam key="month" value="<%=globaz.jade.client.util.JadeStringUtil.fillWithZeroes(line.getMoisComptable(),2)%>"/>
					<ct:menuParam key="csAgence" value="<%=line.getCsAgence()%>"/>	
					<ct:menuParam key="_method" value="ADD" />
					<ct:menuParam key="_valid" value="error" />				
				</ct:menuPopup>
			</TD>	
			<TD align="right" class="mtd" onclick="<%=actionDetail%>"><%=line.getMoisComptable()%></TD>
			<TD align="right" class="mtd" onclick="<%=actionDetail%>"><%=line.getAnneeComptable()%></TD>
			<% if ("".equals(line.getPassageCA().getNoPassage())){%>
				<TD align="center" class="mtd" onclick="<%=actionDetail%>"><IMG SRC="<%=request.getContextPath()%>/images/erreur.gif"></TD>				
			<% }else{ %>
				<TD align="right" class="mtd" onclick="<%=actionDetail%>"><%=line.getPassageCA().getNoPassage()%></TD>
			<% } %>
			<% if ("".equals(line.getPassageCG().getNoPassage())){%>
				<TD align="center" class="mtd" onclick="<%=actionDetail%>"><IMG SRC="<%=request.getContextPath()%>/images/erreur.gif"></TD>				
			<% }else{ %>
				<TD align="right" class="mtd" onclick="<%=actionDetail%>"><%=line.getPassageCG().getNoPassage()%></TD>
			<% } %>
			<% if ("".equals(line.getPassageACM().getNoPassage())){%>
				<TD align="center" class="mtd" onclick="<%=actionDetail%>"><IMG SRC="<%=request.getContextPath()%>/images/erreur.gif"></TD>				
			<% }else{ %>
				<TD align="right" class="mtd" onclick="<%=actionDetail%>"><%=line.getPassageACM().getNoPassage()%></TD>
			<% } %>
			<% if ("".equals(line.getPassageAF().getNoPassage())){%>
				<TD align="center" class="mtd" onclick="<%=actionDetail%>"><IMG SRC="<%=request.getContextPath()%>/images/erreur.gif"></TD>				
			<% }else{ %>
				<TD align="right" class="mtd" onclick="<%=actionDetail%>"><%=line.getPassageAF().getNoPassage()%></TD>
			<% } %>
<%-- 		<TD align="right" class="mtd" onclick="<%=actionDetail%>"><%=line.getSoldeBouclement()%></TD>  --%>
			<TD align="right"" class="mtd" onclick="<%=actionDetail%>"><%=line.getDateEtat()%></TD>
			<TD align="left" class="mtd" onclick="<%=actionDetail%>"><%=line.getSession().getCode(line.getCsAgence())%></TD>
			<%
			if (line.getCsEtat().equals(globaz.tucana.constantes.ITUCSConstantes.CS_ETAT_ENCOURS)){
			%>
				<TD align="center" class="mtd" onclick="<%=actionDetail%>"><IMG SRC="<%=request.getContextPath()%>/images/cadenas_ouvert.gif"></TD>
			<%
			}else if (line.getCsEtat().equals(globaz.tucana.constantes.ITUCSConstantes.CS_ETAT_BOUCLE)){
			%>
				<TD align="center" class="mtd" onclick="<%=actionDetail%>"><IMG SRC="<%=request.getContextPath()%>/images/cadenas.gif"></TD>
			<% }else{ %>
				<TD align="center" class="mtd" onclick="<%=actionDetail%>"><IMG SRC="<%=request.getContextPath()%>/images/verrou.gif"></TD>
			<% } %>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>

	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>