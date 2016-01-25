<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%@ page import="globaz.pyxis.db.adressepaiement.*"%>
<%
    globaz.pyxis.db.adressepaiement.TIAvoirPaiementListViewBean viewBean = (globaz.pyxis.db.adressepaiement.TIAvoirPaiementListViewBean)request.getAttribute ("viewBean");
    size =viewBean.getSize();
    detailLink ="pyxis?userAction=pyxis.adressepaiement.avoirPaiement.afficher&selectedId=";
    session.setAttribute("listViewBean",viewBean);
    menuName = "adressePaiement-detail";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
<TH width="16">&nbsp;</TH>
<TH width="*" align="left">Adresses de paiement</TH>
<Th width="10%">Application</Th>
<Th width="10%">du</Th>
<Th width="10%">au</Th>
<%
if("on".equals(request.getParameter("inclureHistorique"))){
%>

<Th width="10%">modifié le</Th>
<%}%>


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
	       TIAvoirPaiement prevEntity = (TIAvoirPaiement ) viewBean.getEntity(prevIndex);
		TIAvoirPaiement entity = (TIAvoirPaiement ) viewBean.getEntity(i);
		if (!entity.getIdAdressePmtInterne().equals( prevEntity.getIdAdressePmtInterne())) {
			pos++;
		}
			
		boolean currentAdresse = (entity.getIdAdressePmtInterne().equals(entity.getIdAdrPmtIntUnique()) );

		condition = (pos % 2 == 0);
	%>


    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%> 
<%
	actionDetail = "parent.location.href='"+detailLink+viewBean.getIdAdrPmtIntUnique(i)+"'";
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
	<TD class="mtd" width="16" ><ct:FWOptionSelectorTag name="<%=\"item\"+i%>" selectedId="<%=viewBean.getIdAdrPmtIntUnique(i)%>"/><%=(i<9)?"<span style='background:#b0c0e0;border:solid 1 black ;margin-left:4px'>"+(i+1)+"</span>":""%></TD>
	<TD class="mtd"  style="<%=style%>"   onClick="<%=actionDetail%>" width="*"><%=viewBean.getDescription(i)%>&nbsp;</TD>
       <TD class="mtd"  style="<%=style%>"   onClick="<%=actionDetail%>" width="*"><%=viewBean.getApplication(i)%>&nbsp;</TD>
       <TD class="mtd"  style="<%=style%>"   onClick="<%=actionDetail%>" width="*"><%=entity.getDateDebutRelation()%>&nbsp;</TD>
       <TD class="mtd"  style="<%=style%>"   onClick="<%=actionDetail%>" width="*"><%=entity.getDateFinRelation()%>&nbsp;</TD>
<%
if("on".equals(request.getParameter("inclureHistorique"))){
%>

	<TD class="mtd"  style="<%=style%>"  onClick="<%=actionDetail%>"><%=entity.getSpy().getDate()%>&nbsp;</TD>
<%}%>
	
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>