<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
    globaz.pyxis.db.tiers.TIReferencePaiementListViewBean viewBean = (globaz.pyxis.db.tiers.TIReferencePaiementListViewBean)request.getAttribute ("viewBean");
    size = viewBean.size ();
    session.setAttribute("listViewBean",viewBean);
	menuName="reference-paiement";
	detailLink ="pyxis?userAction=pyxis.tiers.referencePaiement.afficher&selectedId=";
%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<style>
	.date {font-size:7pt;}
	.marker {font-family : webdings; color: red}
</style>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	<%-- tpl:put name="zoneHeaders"  --%>

	<Th nowrap width="16">&nbsp;</Th>
    <Th>Etichetta</Th>
    <Th>Riferimento QR</Th>
    <Th>Data d'inizio</Th>
	<Th>Data di fine</Th>
    <Th>Numero di conto</Th>
    <Th>Indirizzo di pagamento</Th>

    <%
    boolean oldCondition = true;
    boolean sameAdresse = false;
    int pos = 0;
    %>

    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
	<%
       	String subLineColor="";
	    	if (i==0) { oldCondition = condition; }
			else {
				if (!((globaz.pyxis.db.tiers.TIReferencePaiementViewBean)viewBean.getEntity(i-1)).getIdReferenceQR().equals(((globaz.pyxis.db.tiers.TIReferencePaiementViewBean)viewBean.getEntity(i)).getIdReferenceQR())) {
					condition = !oldCondition;
					oldCondition = condition;
					sameAdresse = false;
				} else {
					condition = oldCondition;
					sameAdresse = true;
				}
			}
    %>

    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
	<%-- tpl:put name="zoneList"  --%>
<%
	globaz.pyxis.db.tiers.TIReferencePaiementViewBean entity = (globaz.pyxis.db.tiers.TIReferencePaiementViewBean) viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+entity.getIdReferenceQR()+"&selectedId="+entity.getIdReferenceQR()+"&colonneSelection="+request.getParameter("colonneSelection")+"'";
%>
<%
	if  (!sameAdresse) {
		pos ++;
%>
      <TD class="mtd" width="16" style="<%=(sameAdresse)?"":"border-top:solid 1px silver"%>;">
			<%String url = request.getContextPath()+"/pyxis?userAction=pyxis.tiers.referencePaiement.afficher&selectedId="+entity.getIdReferenceQR()+"&colonneSelection="+request.getParameter("colonneSelection");%>
			<ct:menuPopup menu="reference-paiement" detailLabelId="Detail" detailLink="<%=url%>">
	 			<ct:menuParam key="idReference" value="<%=entity.getIdReferenceQR()%>"/>
			</ct:menuPopup>
      </TD>

      <TD class="mtd" onClick="<%=actionDetail%>" align="right" style="<%=(sameAdresse)?"":"border-top:solid 1px silver"%>;"><%=entity.getLibelle()%>&nbsp;</TD>
      <TD class="mtd" onClick="<%=actionDetail%>" align="right" style="<%=(sameAdresse)?"":"border-top:solid 1px silver"%>;"><%=entity.getReferenceQR()%>&nbsp;</TD>
      <TD class="mtd" onClick="<%=actionDetail%>" align="right" style="<%=(sameAdresse)?"":"border-top:solid 1px silver"%>;"><%=entity.getDateDebut()%>&nbsp;</TD>
	  <TD class="mtd" onClick="<%=actionDetail%>" align="right" style="<%=(sameAdresse)?"":"border-top:solid 1px silver"%>;"><%=entity.getDateFin()%>&nbsp;</TD>

<%} else {%>
      <TD class="mtd" width="16" style="<%=(sameAdresse)?"":"border-top:solid 1px silver"%>;">&nbsp;</TD>
      <TD class="mtd" style="<%=(sameAdresse)?"":"border-top:solid 1px silver"%>;" >&nbsp;</TD>
      <TD class="mtd" style="<%=(sameAdresse)?"":"border-top:solid 1px silver"%>;">&nbsp;</TD>
      <TD class="mtd" style="<%=(sameAdresse)?"":"border-top:solid 1px silver"%>;">&nbsp;</TD>
	  <TD class="mtd" style="<%=(sameAdresse)?"":"border-top:solid 1px silver"%>;">&nbsp;</TD>
<%}%>
<%
	if (condition) {
		subLineColor="#F0F0F0";
	} else {
		subLineColor="#D0D5EA";
	}
%>

	<TD class="mtd" onClick="<%=actionDetail%>" style="<%=(sameAdresse)?"border-top:solid 1px "+subLineColor:"border-top:solid 1px silver"%>;" ><%=entity.getDetailNumCompteBancaire()%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" style="<%=(sameAdresse)?"border-top:solid 1px "+subLineColor:"border-top:solid 1px silver"%>;" ><i><%pageContext.getOut().write(entity.getDetailAdresseCourt());%></i></TD>

    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>