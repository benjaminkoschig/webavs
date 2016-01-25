 
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
   <%
    globaz.pyxis.db.adressecourrier.TILocaliteListViewBean viewBean = (globaz.pyxis.db.adressecourrier.TILocaliteListViewBean )request.getAttribute ("viewBean");
    size =viewBean.getSize();
    detailLink ="pyxis?userAction=pyxis.adressecourrier.localite.afficher&selectedId=";
    session.setAttribute("listViewBean",viewBean);
    %>


<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 


    <th nowrap width="16">&nbsp;</th>
    <th nowrap width="100">NPA</th>
    <th nowrap width="400">Localité</th>
    <th nowrap width="150">Canton</th>
    <th nowrap width="*">Pays</th>
   
   
  
      
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%> 

<%
	actionDetail = "parent.location.href='"+detailLink+viewBean.getIdLocalite(i)+"&colonneSelection="+viewBean.getColonneSelection()+"'";
%>
    <TD class="mtd" width="16" >
    

    <%String url = request.getContextPath()+"/pyxis?userAction=pyxis.adressecourrier.localite.afficher&selectedId="+viewBean.getIdLocalite(i);%>
	<ct:menuPopup menu="TIMenuVide" detailLabelId="Detail" detailLink="<%=url%>" />

    
    
    
    </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="100"><%=viewBean.getNumPostal(i)%>&nbsp;</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="400"><%=viewBean.getLocalite(i)%>&nbsp;</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="150"><%=objSession.getCodeLibelle(viewBean.getIdCanton(i))%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="*"><%=viewBean.getPays(i)%>&nbsp;</TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  

<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>