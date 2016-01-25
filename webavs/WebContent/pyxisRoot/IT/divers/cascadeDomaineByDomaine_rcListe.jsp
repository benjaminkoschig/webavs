<%-- tpl:insert page="/theme/list.jtpl" --%>
	<%@ page language="java" errorPage="/errorPage.jsp" %>
	<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
	<%@ include file="/theme/list/header.jspf" %>
	<%-- tpl:put name="zoneScripts"  --%>
 		<%
			globaz.pyxis.db.divers.TICascadeDomaineByDomaineListViewBean viewBean = (globaz.pyxis.db.divers.TICascadeDomaineByDomaineListViewBean)request.getAttribute ("viewBean");
			size = viewBean.size ();
			detailLink ="pyxis?userAction=pyxis.divers.cascadeDomaineByDomaine.afficher&selectedId=";
    	%>
	<%-- /tpl:put --%>
	<%@ include file="/theme/list/javascripts.jspf" %>
	<%-- tpl:put name="zoneHeaders"  --%>
		  <TH width="16">&nbsp;</TH>    
		  <TH width="35%"><ct:FWLabel key='DOMAINE_CLEF'/></TH>   
		  <TH width="15%"><ct:FWLabel key='POSITION'/></TH>  
		  <TH width="35%"><ct:FWLabel key='DOMAINE'/></TH>    
    <%-- /tpl:put --%> 
	<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
	<%@ include file="/theme/list/lineStyle.jspf" %>
	<%-- tpl:put name="zoneList"  --%>
		<%
			globaz.pyxis.db.divers.TICascadeDomaineByDomaine line = (globaz.pyxis.db.divers.TICascadeDomaineByDomaine) viewBean.getEntity(i);
			actionDetail = targetLocation  + "='" + detailLink + line.getIdCascadeDomaineByDomaine()+"'";
		%>
	  	<TD valign="top" class="mtd" width="16">
			<%String url = request.getContextPath()+"/pyxis?userAction=pyxis.divers.cascadeDomaineByDomaine.afficher&selectedId="+line.getIdCascadeDomaineByDomaine();%>
			<ct:menuPopup menu="TIMenuVide" detailLabelId="Detail" detailLink="<%=url%>">
			</ct:menuPopup> 
	  	</TD>
      	<TD class="mtd" onClick="<%=actionDetail%>" width="35%"><%=viewBean.getSession().getCodeLibelle(line.getCsDomaineClef())%></TD>
      	<TD class="mtd" onClick="<%=actionDetail%>" width="15%"><%=line.getPosition()%></TD>  
      	<TD class="mtd" onClick="<%=actionDetail%>" width="35%"><%=viewBean.getSession().getCodeLibelle(line.getCsDomaine())%></TD>      
	<%-- /tpl:put --%>
	<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
	<%-- /tpl:put --%>
	<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>