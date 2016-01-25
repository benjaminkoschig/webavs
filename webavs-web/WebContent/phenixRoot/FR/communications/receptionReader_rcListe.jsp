<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<% 
	CPReceptionReaderListViewBean viewBean = (CPReceptionReaderListViewBean) session.getAttribute("listViewBean");
    detailLink ="phenix?userAction=phenix.communications.receptionReader.afficher&selectedId=";  
    size = viewBean.getSize();
    menuName = "Communications-journalRetour-default";
    
    
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    
		<%@page import="globaz.phenix.db.communications.CPReceptionReaderListViewBean"%>
<%@page import="globaz.phenix.db.communications.CPReceptionReaderViewBean"%>
<th width="20">&nbsp;</th>
      	<TH width="5" align="left">Canton</TH>
      	<TH width="5" align="left">Abréviation</TH>
      	<TH width="5" align="left">Format XML</TH>
      	<TH width="5" align="left">Nom classe</TH>
      	<TH width="5" align="left">Recherche Tiers</TH>
      	<TH width="5" align="left">Nom Fichier Envoi</TH>
		<!--th  width="10%">Succès</th-->

    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>


		<% 
	  		CPReceptionReaderViewBean line = (CPReceptionReaderViewBean) viewBean.getEntity(i);
	    	actionDetail = targetLocation+"='"+detailLink+line.getIdCommunicationReader()+"'";	  
	 		String tmp = detailLink + line.getIdCommunicationReader();
		 %>
	     <TD class="mtd" width="2">
		     <ct:menuPopup menu="CP-OnlyDetail" label="<%=optionsPopupLabel%>" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>" target="top.fr_main">
			 	<ct:menuParam key="idJournalRetour" value="<%=line.getIdCommunicationReader()%>"/>  
			 </ct:menuPopup>
	     </TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="5" align="right"><%=line.getLibelleCanton()%>&nbsp;</TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="5" align="right"><%=line.getCanton()%>&nbsp;</TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="5" align="right"><%=line.getFormatXml()%>&nbsp;</TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="5" align="right"><%=line.getNomClass()%>&nbsp;</TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="5" align="right"><%=line.getRechercheTiers()%>&nbsp;</TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="5" align="right"><%=line.getNomFichier()%>&nbsp;</TD>
	     <%--TD class="mtd" onclick="<%=actionDetail%>" width="10%" align="center"> <IMG src="<%=request.getContextPath()%><%=line.isSuccess()?"/images/ok.gif" : "/images/erreur.gif"%>"></TD--%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>