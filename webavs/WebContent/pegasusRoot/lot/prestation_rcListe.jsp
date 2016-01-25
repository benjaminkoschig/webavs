<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="java.util.List"%>
<%@page import="globaz.pegasus.vb.lot.PCPrestationListViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.pegasus.utils.PCPrestationGroupByIterator"%>
<%@page import="ch.globaz.pegasus.business.models.lot.Prestation"%>
<%@page import="globaz.pegasus.utils.PCUserHelper"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Comparator"%>

<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_PC_PRE_L"

	PCPrestationListViewBean viewBean = (PCPrestationListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	
	PCPrestationGroupByIterator gbIter = null;
	if(viewBean.iterator()!=null){
		gbIter = new PCPrestationGroupByIterator(viewBean.iterator());
	}
	detailLink ="pegasus?userAction="+IPCActions.ACTION_LOT_ORDRE_VERSEMENT+".afficher&selectedId=";
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
 
	Comparator myComparator=new Comparator (){
		   public int compare(Object o1, Object o2){
			   
			   	int o1Int = new Integer((String) o1).intValue();
			   	int o2Int = new Integer((String) o2).intValue();
			   	int res=0;
			   	
			    if (o1Int > o2Int){res=1;}
			    else if (o1Int < o2Int){res=-1;}
			    else if (o1Int == o2Int){res=0;}
			    
			    return res;
		   }
	};

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

<TH>&nbsp;</TH>
	<TH><ct:FWLabel key="JSP_PC_PRE_L_PRESTATAIRE"/></TH>
	<TH><ct:FWLabel key="JSP_PC_PRE_L_PERIODE"/></TH>
	<TH><ct:FWLabel key="JSP_PC_PRE_L_MONTANT"/></TH>
    <TH><ct:FWLabel key="JSP_PC_PRE_L_ETAT"/></TH>
    <TH><ct:FWLabel key="JSP_PC_PRE_L_NO_LOT"/></TH>
    <TH><ct:FWLabel key="JSP_PC_PRE_L_NO"/></TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
	    if(gbIter != null && gbIter.hasNext()){

			Prestation line = (Prestation) gbIter.next();
			Prestation current=line;
	    	actionDetail = targetLocation  + "='" + detailLink + line.getSimplePrestation().getIdPrestation()+"'";
	%>      
    <TD class="mtd" width="">
	   	<ct:menuPopup menu="pegasus-optionsprestation" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getSimplePrestation().getIdPrestation()%>">
			<ct:menuParam key="selectedId" value="<%=line.getSimplePrestation().getIdPrestation()%>"/>
			<ct:menuParam key="idPrestation" value="<%=line.getSimplePrestation().getIdPrestation()%>"/>
			<ct:menuParam key="montantPrestation" value="<%=line.getSimplePrestation().getMontantTotal()%>"/>
			<ct:menuParam key="idTierRequerant" value="<%=line.getTiersBeneficiaire().getTiers().getIdTiers()%>"/>
		</ct:menuPopup>
    </TD>
    <TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=PCUserHelper.getDetailAssure(objSession, line.getTiersBeneficiaire())%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getPeriod()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="right"><%=new FWCurrency(line.getSimplePrestation().getMontantTotal()).toStringFormat()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getSimplePrestation().getCsEtat())%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getSimplePrestation().getIdLot()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getSimplePrestation().getIdPrestation()%>&nbsp;</TD>
	<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>