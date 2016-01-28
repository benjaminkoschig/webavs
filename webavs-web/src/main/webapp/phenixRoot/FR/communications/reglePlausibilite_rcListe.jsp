<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<% 
	globaz.phenix.db.communications.CPReglePlausibiliteListViewBean viewBean = (globaz.phenix.db.communications.CPReglePlausibiliteListViewBean) session.getAttribute("listViewBean");
    detailLink ="phenix?userAction=phenix.communications.reglePlausibilite.afficher&selectedId="; 
    size = viewBean.getSize();
    menuName = "Communications-plausibilite";
%>


<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    
		<th width="20">&nbsp;</th>
      	<TH width="*" align="left">Description</TH>
      	<th  width="15">Priorité</th>
		<th  width="15%">Actif</th>
		<th  width="15%">Canton</th>
    	
    
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	  <% 
			target = "parent.fr_detail";
			targetLocation = target+".location.href";
			globaz.phenix.db.communications.CPReglePlausibiliteViewBean line = (globaz.phenix.db.communications.CPReglePlausibiliteViewBean) viewBean.getEntity(i);
			actionDetail = targetLocation+"='"+detailLink+line.getIdPlausibilite()+"'";
	  		String tmp = detailLink + line.getIdPlausibilite();
	  %>
	  <TD class="mtd" width="">
	    <ct:menuPopup menu="CP-plausibilites" label="<%=optionsPopupLabel%>" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>" target="top.fr_main">
			<ct:menuParam key="idPlausibilite" value="<%=line.getIdPlausibilite()%>"/>  
		</ct:menuPopup>
	  </TD>
      <TD class="mtd" onclick="<%=actionDetail%>" width="*"><%=line.getDescription_fr()%>&nbsp;</TD>
      <TD class="mtd" onclick="<%=actionDetail%>" width="15"><%=line.getPriorite()%>&nbsp;</TD>
      <TD class="mtd" onclick="<%=actionDetail%>" width="15%" align="center"> <IMG src="<%=request.getContextPath()%><%=line.isActif()?"/images/select.gif" : "/images/blank.gif"%>"></TD>
      <TD class="mtd" onclick="<%=actionDetail%>" width="*"><%=globaz.phenix.translation.CodeSystem.getLibelle(session,line.getCanton())%>&nbsp;</TD>


<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>