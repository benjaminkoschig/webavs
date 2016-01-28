<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
    <%
	globaz.pyxis.db.adressecourrier.TIPaysListViewBean viewBean = (globaz.pyxis.db.adressecourrier.TIPaysListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
	detailLink ="pyxis?userAction=pyxis.adressecourrier.pays.afficher&selectedId=";
    	session.setAttribute("listViewBean",viewBean);
    %>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%>
	<TH nowrap width="16">&nbsp;</TH>
      <TH nowrap width="93">ISO Code</TH>
      <TH width="366">Bezeichnung</TH>
      <Th width="133">Code centrale</Th>
    <Th width="133">Vorwahl</Th>
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%> 

	<%
	actionDetail = "parent.location.href='"+detailLink+viewBean.getIdPays(i)+"&colonneSelection="+viewBean.getColonneSelection()+"'";
	%>
	<TD class="mtd" width="16" >
	
	
	<%String url = request.getContextPath()+"/pyxis?userAction=pyxis.adressecourrier.pays.afficher&selectedId="+viewBean.getIdPays(i);%>
	<ct:menuPopup menu="TIMenuVide" detailLabelId="Detail" detailLink="<%=url%>" />
	
	
	
	</TD>
      <TD class="mtd"  onClick="<%=actionDetail%>" width="93"><%=viewBean.getCodeIso(i)%>&nbsp;</TD>
      <TD class="mtd"  onClick="<%=actionDetail%>" width="366"><%=viewBean.getLibelle(i)%>&nbsp;</TD>
      <TD class="mtd"  onClick="<%=actionDetail%>" width="133" align="center"><%=viewBean.getCodeCentrale(i)%>&nbsp;</TD>
      <TD class="mtd"  onClick="<%=actionDetail%>" width="133"><%=viewBean.getIndicatif(i)%>&nbsp;</TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>