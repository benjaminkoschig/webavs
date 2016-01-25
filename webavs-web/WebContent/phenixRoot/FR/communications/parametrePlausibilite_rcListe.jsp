<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.phenix.db.communications.CPParametrePlausibiliteListViewBean viewBean = (globaz.phenix.db.communications.CPParametrePlausibiliteListViewBean) session.getAttribute("listViewBean");
    detailLink ="phenix?userAction=phenix.communications.parametrePlausibilite.afficher&selectedId="; 
    size = viewBean.getSize();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    
		<th width="20">&nbsp;</th>
		<th  width="15">N°</th>
      	<TH width="*" align="left">Description</TH>
      	<th  width="15">Priorité</th>
      	<th  width="45">Type de message</th>
		<th  width="15%">Actif</th>
    
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>

	  <% 
			target = "parent.fr_detail";
			targetLocation = target+".location.href";
			globaz.phenix.db.communications.CPParametrePlausibiliteViewBean line = (globaz.phenix.db.communications.CPParametrePlausibiliteViewBean) viewBean.getEntity(i);
			actionDetail = targetLocation+"='"+detailLink+line.getIdParametre()+"'";			
	  		String tmp = detailLink + line.getIdParametre();
	 %>
     <TD class="mtd" width="">
     	<ct:menuPopup menu="CP-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
     </TD>
     <TD class="mtd" onclick="<%=actionDetail%>" width="15"><%=line.getIdParametre()%>&nbsp;</TD>
     <TD class="mtd" onclick="<%=actionDetail%>" width="*"><%=line.getDescription_fr()%>&nbsp;</TD>
     <TD class="mtd" onclick="<%=actionDetail%>" width="15"><%=line.getPriorite()%>&nbsp;</TD>
     <TD class="mtd" onclick="<%=actionDetail%>" width="45"><%=line.getLibelleTypeMessage()%>&nbsp;</TD>
     <TD class="mtd" onclick="<%=actionDetail%>" width="15%" align="center"> <IMG src="<%=request.getContextPath()%><%=line.isActif()?"/images/select.gif" : "/images/blank.gif"%>"></TD>


<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>