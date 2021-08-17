<%try{%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.draco.translation.*"%>
<%@page import="globaz.draco.db.declaration.*"%>
<%@page import="globaz.globall.util.*"%>
<%@ page import="java.math.*"%>
<%
globaz.draco.db.declaration.DSLigneDeclarationListViewBean viewBean = (globaz.draco.db.declaration.DSLigneDeclarationListViewBean)request.getAttribute("viewBean");
size = viewBean.getSize ();
detailLink = "draco?userAction=draco.declaration.ligneDeclaration.afficher&selectedId=";

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
            
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
			<TH nowrap width="100" align="left">Assurance</TH>            
            <TH align="right">Année</TH>
            <TH align="right">Masse effective</TH>
            <TH align="right">Taux</TH>
            <TH align="right">Fraction</TH>
            <TH align="right">Cotisation due</TH>
            <%
            	BigDecimal cotisationDue = new BigDecimal(0);
            	
            %>
            <TH align="right">Acompte</TH>
            <%
            	BigDecimal acompte = new BigDecimal(0);
            	
            %>
            <TH align="right">Solde cotisations</TH>
            <%
            	BigDecimal montTotal = new BigDecimal(0);
            	
            %>
            <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
            <% DSLigneDeclarationViewBean entity = (DSLigneDeclarationViewBean)viewBean.getEntity(i);
            actionDetail = targetLocation + "='" + detailLink + entity.getIdLigneDeclaration()+"'"; %>
            <TD class="mtd" onClick="<%=actionDetail%>" width="100" align="left"><%=entity.getAssuranceLibelle()%></TD>
            <TD class="mtd" onClick="<%=actionDetail%>" width="100" align="left"><%=entity.getAnneCotisation()%></TD>
            <TD class="mtd" onClick="<%=actionDetail%>" align="right"><%if(globaz.jade.client.util.JadeStringUtil.isDecimalEmpty(entity.getMontantDeclaration())){%><%=entity.getMontantDeclaration()%><%}else{%><%=JANumberFormatter.format(JANumberFormatter.round(JANumberFormatter.deQuote(entity.getMontantDeclaration()),0.05,2,JANumberFormatter.NEAR))%><%}%></TD>
            <%if (!JAUtil.isStringEmpty(entity.getTauxAssuranceDeclaration())){%>
            <TD class="mtd" onClick="<%=actionDetail%>" align="right"><%=entity.getTauxAssuranceDeclaration()%></TD>
            <%} else {%>
            <TD class="mtd" onClick="<%=actionDetail%>" align="right"><%=JANumberFormatter.fmt(entity.getTauxAssurance(),true,true,true,5)%></TD>            
            <%}%>
            <%if (!JAUtil.isStringEmpty(entity.getFractionAssuranceDeclaration())){%>
            <TD class="mtd" onClick="<%=actionDetail%>" align="right"><%=entity.getFractionAssuranceDeclaration()%></TD>
            <%} else {%>
            <TD class="mtd" onClick="<%=actionDetail%>" align="right"><%=entity.getFractionAssurance()%></TD>            
            <%}%>
            <TD class="mtd" onClick="<%=actionDetail%>" align="right"><%=JANumberFormatter.formatNoRound(entity.getCotisationDue())%>&nbsp;</TD> 
            <% if (!JAUtil.isStringEmpty(entity.getCotisationDue())){
	  		    cotisationDue = cotisationDue.add(new BigDecimal(JANumberFormatter.deQuote(entity.getCotisationDue())));
            }%>
                <% if(entity.getDeclaration().getTypeDeclaration().equals(CodeSystem.CS_PRINCIPALE)
                    || entity.getDeclaration().getTypeDeclaration().equals(CodeSystem.CS_BOUCLEMENT_ACCOMPTE)){
                    if (entity.getDeclaration().getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_COMPTABILISE)) {%>
                    <TD class="mtd" onClick="<%=actionDetail%>" align="right"><%=JANumberFormatter.formatNoRound(entity.getCumulCotisationDeclaration())%>&nbsp;</TD>
                        <%if (!JAUtil.isStringEmpty(entity.getCumulCotisationDeclaration())){
                            acompte = acompte.add(new BigDecimal(JANumberFormatter.deQuote(entity.getCumulCotisationDeclaration())));
                        }%>
                <%} else {%>
                    <TD class="mtd" onClick="<%=actionDetail%>" align="right">
                        <%if (entity.getCompteur() != null) {%>
                            <%=JANumberFormatter.formatNoRound(entity.getCompteur().getCumulCotisation())%>
                        <%}%>&nbsp;
                    </TD>
                <%
                    if (entity.getCompteur()!=null && !JAUtil.isStringEmpty(entity.getCompteur().getCumulCotisation())){
                        acompte = acompte.add(new BigDecimal(JANumberFormatter.deQuote(entity.getCompteur().getCumulCotisation())));
                    }
                %>
                <%}
            }else{ %>
                <TD class="mtd" onClick="<%=actionDetail%>" align="right">&nbsp;</TD>
            <%}%>
            <TD class="mtd" onClick="<%=actionDetail%>" align="right"><%=JANumberFormatter.formatNoRound(entity.getSoldeCotisation())%>&nbsp;</TD>
            <% if (JAUtil.isStringEmpty(entity.getSoldeCotisation())){}else{
	  			montTotal = montTotal.add(new BigDecimal(JANumberFormatter.deQuote(entity.getSoldeCotisation()))); 
	  		}%>

<ct:menuChange displayId="options" menuId="DS-OptionsDeclaration" showTab="options">
	<ct:menuSetAllParams key="idDeclaration" value="<%=entity.getIdDeclaration()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=entity.getIdDeclaration()%>"/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
		<tr bgcolor="black">
			<td colspan="8"></td>
		</tr>
		<tr style="font-size : x-small; font-family : Verdana, Arial; background : #F0F0F0">
			<TD class="mtd" align="left"><b>Total</b></TD>
			<TD class="mtd" align="left">&nbsp;</TD>
			<TD class="mtd" align="left">&nbsp;</TD>
			<TD class="mtd" align="left">&nbsp;</TD>
			<TD class="mtd" align="left">&nbsp;</TD>
			<TD class="mtd" align="right"><%=JANumberFormatter.fmt(cotisationDue.toString(),true,true,true,2)%>&nbsp;</TD>
			<TD class="mtd" align="right"><%=JANumberFormatter.fmt(acompte.toString(),true,true,true,2)%>&nbsp;</TD>			
			<TD class="mtd" align="right"><%=JANumberFormatter.fmt(montTotal.toString(),true,true,true,2)%>&nbsp;</TD>
		</TR>
        <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
<%}catch(Exception e){
	e.printStackTrace();
}%>