 
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
	globaz.pyxis.db.adressepaiement.TIAdressePaiementUtiliseeListViewBean viewBean = (globaz.pyxis.db.adressepaiement.TIAdressePaiementUtiliseeListViewBean )request.getAttribute ("viewBean");
	size = viewBean.size ();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
        
    <Th nowrap width="20%">SVN</Th>
      
    <Th width="30%">Abr.-Nr.</Th>
      
    <Th width="30%">Name </Th>
    <Th width="30%">Von</Th>
    <Th width="30%">Bis</Th>
    <Th width="20%">Anwendung</Th>
   
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%>
<%
	globaz.pyxis.db.adressepaiement.TIAvoirPaiement entity = (globaz.pyxis.db.adressepaiement.TIAvoirPaiement) viewBean.getEntity(i);
%>
      <TD class="mtd" width="20%" align="left"><%=viewBean.getNumAvs(i)%>&nbsp;</TD>
      <TD class="mtd" width="30%" align="left"><%=viewBean.getNumAffilie(i)%>&nbsp;</TD>
      <TD class="mtd" width="30%">
      <DIV align="left"><%=viewBean.getNom(i)%></DIV>
      </TD>
      <td class="mtd"><%=entity.getDateDebutRelation()%>&nbsp;</td>
      <td class="mtd"><%=entity.getDateFinRelation()%>&nbsp;</td>

    <TD class="mtd" width="20%">
      <DIV align="left"><%=viewBean.getLibelleApplication(i)%></DIV>
      </TD>
    
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>