 
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%@ page import="globaz.pyxis.db.adressecourrier.*"%>
    <%
    globaz.pyxis.db.adressecourrier.TIAvoirAdresseListViewBean viewBean = (globaz.pyxis.db.adressecourrier.TIAvoirAdresseListViewBean )request.getAttribute ("viewBean");
    size =viewBean.getSize();
    detailLink ="pyxis?userAction=pyxis.adressecourrier.avoirAdresse.afficher&idTiers="+viewBean.getIdTiers()+"&selectedId=";
    %>


<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
    <!--<TH width="40"> &nbsp;</TH> -->

    <Th width="16">&nbsp;</Th>
    <Th>Destinatario</Th>
    <Th>Via  </Th>
    <Th>Numéro</Th>
    <Th>Località</Th>
    <Th>Application</Th>
    <Th>Tipo</Th>
    <Th>du</Th>
    <Th>au</Th>
<%
if("on".equals(request.getParameter("inclureHistorique"))){
%>
    <Th>modifié le</Th>
<%
}
%>
<%
	int pos = 0;
 %>  
    
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
	<%	int prevIndex = 0;
		if (i>0) {
			prevIndex = i-1;
		} 
	       TIAvoirAdresse prevEntity = (TIAvoirAdresse) viewBean.getEntity(prevIndex);
		TIAvoirAdresse entity = (TIAvoirAdresse) viewBean.getEntity(i);
		if (!entity.getIdAdresseInterne().equals( prevEntity.getIdAdresseInterne())) {
			pos++;
		}
			
		boolean currentAdresse = (entity.getIdAdresseInterne().equals(entity.getIdAdresseIntUnique()) );

		condition = (pos % 2 == 0);
	%>

    
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%> 

   <TD class="mtd" width="16" >
   
<%String url = request.getContextPath()+"/pyxis?userAction=pyxis.tiers.alias.afficher&selectedId="+entity.getIdAdresseIntUnique()+"&idApplication"+entity.getIdApplication();%>
<ct:menuPopup menu="TIMenuVide" detailLabelId="Detail" detailLink="<%=url%>" />
   
   
   </TD>
<%
     actionDetail = "parent.location.href='"+detailLink+entity.getIdAdresseIntUnique()+"&idApplication="+entity.getIdApplication()+"'";
%>
<%
	String style="";
	if("on".equals(request.getParameter("inclureHistorique"))){
	
		if (currentAdresse ){
			style="font-weight : bold;border-top: solid 1px silver;";
		} else {
			style="padding-left:0.6cm;font-size:8pt";
		}
	}
%>
   
    <TD class="mtd"  style="<%=style%>"  onClick="<%=actionDetail%>"><%=viewBean.getNom(i)%>&nbsp;</TD>
    <TD class="mtd"  style="<%=style%>"  onClick="<%=actionDetail%>"><%=entity.getRue()%>&nbsp;</TD>
    <TD class="mtd"  style="<%=style%>"  onClick="<%=actionDetail%>"><%=entity.getNumeroRue()%>&nbsp;</TD>
    <TD class="mtd"  style="<%=style%>"  onClick="<%=actionDetail%>"><%=viewBean.getLocalite(i)%>&nbsp;</TD>
    <TD class="mtd"  style="<%=style%>"  onClick="<%=actionDetail%>"><%=viewBean.getApplication(i)%>&nbsp;</TD>
    <TD class="mtd"  style="<%=style%>"  onClick="<%=actionDetail%>"><%=viewBean.getType(i)%>&nbsp;</TD>
    <TD class="mtd"  style="<%=style%>"  onClick="<%=actionDetail%>"><%=entity.getDateDebutRelation()%>&nbsp;</TD>
    <TD class="mtd"  style="<%=style%>"  onClick="<%=actionDetail%>"><%=entity.getDateFinRelation()%>&nbsp;</TD>
<%
if("on".equals(request.getParameter("inclureHistorique"))){
%>

    <TD class="mtd"  style="<%=style%>"  onClick="<%=actionDetail%>"><%=entity.getSpy().getDate()%>&nbsp;</TD>
<%}%>

<!--
    <TD class="mtd"  style="<%=style%>"  onClick="<%=actionDetail%>"><%=entity.getIdAdresseIntUnique()%>&nbsp;</TD>
-->

      
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  

<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>