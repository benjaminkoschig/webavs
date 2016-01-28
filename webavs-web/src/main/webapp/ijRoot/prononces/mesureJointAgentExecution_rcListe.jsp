<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.ij.vb.prononces.IJMesureJointAgentExecutionListViewBean viewBean = (globaz.ij.vb.prononces.IJMesureJointAgentExecutionListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "ij?userAction=ij.prononces.mesureJointAgentExecution.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH>&nbsp;</TH>    
	<TH><ct:FWLabel key="JSP_AGENT_EXECUTION"/></TH>
	<TH><ct:FWLabel key="JSP_DATE_DEBUT"/></TH>
	<TH><ct:FWLabel key="JSP_DATE_FIN"/></TH>
    <!--<TH><ct:FWLabel key="JSP_TYPE_ATTESTATION"/></TH>-->
    <!--<TH><ct:FWLabel key="JSP_ATTESTATION_GROUPEE"/></TH>-->
    <TH><ct:FWLabel key="JSP_LANGUE"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
		globaz.ij.vb.prononces.IJMesureJointAgentExecutionViewBean line = (globaz.ij.vb.prononces.IJMesureJointAgentExecutionViewBean) viewBean.getEntity(i);
		actionDetail = "parent.fr_detail.location.href='" + detailLink + line.getIdMesure() +"&idPrononce="+line.getIdPrononce()+"&noAVS="+viewBean.getNoAVS()+"&dateDebutPrononce="+viewBean.getDateDebutPrononce()+"&csTypeIJ="+viewBean.getCsTypeIJ()+"'";
	
		String detailMenu = detailLink + line.getIdMesure() +"&idPrononce="+line.getIdPrononce()+"&noAVS="+viewBean.getNoAVS()+"&dateDebutPrononce="+viewBean.getDateDebutPrononce()+"&csTypeIJ="+viewBean.getCsTypeIJ();
	%>
    <TD class="mtd" width="">
    </TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getNomAgentExecution()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateDebut()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateFin()%>&nbsp;</TD>
	<!--<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getLibelleTypeAttestation()%>&nbsp;</TD>-->
	<!--<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><IMG src="<%=request.getContextPath()+line.getImageAttestationGroupee()%>" alt=""></TD>-->
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getLibelleLangue()%>&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>