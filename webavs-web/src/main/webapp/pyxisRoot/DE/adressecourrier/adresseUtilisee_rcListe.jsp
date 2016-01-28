 
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
    <%
	globaz.pyxis.db.adressecourrier.TIAdresseUtiliseeListViewBean viewBean = (globaz.pyxis.db.adressecourrier.TIAdresseUtiliseeListViewBean )request.getAttribute ("viewBean");
	size = viewBean.size ();
       detailLink ="pyxis?userAction=pyxis.adressecourrier.avoirAdresse.afficher&_method=add";
%>


<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 

        
    <Th nowrap width="20%">SVN</Th>
      
    <Th width="20%">Abr.-Nr.</Th>
    <Th width="30%">Name</Th>
    <Th width="30%">Von</Th>
    <Th width="30%">Bis </Th>
    <Th width="20%">Anwendung</Th>
    <Th width="10%">Typ</Th>    

<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%>

<%
     
     actionDetail = "parent.location.href='"+detailLink+"&idTiers="+viewBean.getIdTiers(i)+"'";
	globaz.pyxis.db.adressecourrier.TIAvoirAdresse entity = (globaz.pyxis.db.adressecourrier.TIAvoirAdresse) viewBean.getEntity(i);

%>

      <TD class="mtd" onclick="<%=actionDetail%>" width="20%" align="left"><%=viewBean.getNumAvs(session, i)%>&nbsp;</TD>
      <TD class="mtd" onclick="<%=actionDetail%>" width="20%" align="left"><%=viewBean.getNumAffilie(session, i)%>&nbsp;</TD>
      <TD class="mtd" onclick="<%=actionDetail%>" width="30%">
      <DIV align="left"><%=viewBean.getNom(i)%></DIV>
      </TD>
      
      <td class="mtd"><%=entity.getDateDebutRelation()%>&nbsp;</td>
      <td class="mtd"><%=entity.getDateFinRelation()%>&nbsp;</td>
    <TD class="mtd" onclick="<%=actionDetail%>" width="20%">
      <DIV align="left"><%=viewBean.getLibelleApplication(i)%></DIV>
      </TD>
    <TD class="mtd" onclick="<%=actionDetail%>" width="10%">
      <DIV align="center"><%=viewBean.getType(i)%></DIV>
      </TD>


<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  

<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>