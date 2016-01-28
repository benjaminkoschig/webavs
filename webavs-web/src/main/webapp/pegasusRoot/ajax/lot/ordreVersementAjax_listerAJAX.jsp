<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.lot.PCOrdreVersementAjaxViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored ="false" %>
<%
	PCOrdreVersementAjaxViewBean viewBean=(PCOrdreVersementAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
%>
	<liste>
	<%-- 
		<c:choose>
			<c:when test="${viewBean.displayOvsRow}">
				<c:forEach var="ov" items="${viewBean.ovsBd}" >
						<tr idEntity="${ov.simpleOrdreVersement.id}" >
							<td>&#160;</td>	
							<td style="text-align: left;">${ov.simpleOrdreVersement}</td>

							<td data-g-amountformatter=" ">${ov.simpleOrdreVersement.montant}</td>
							<td style="width:100px">&#160;</td>
							<td width="20%">&#160;</td>		
						</tr>
					</c:forEach>			
			</c:when>
			<c:otherwise> --%>
				<c:forEach var="entry" items="${viewBean.ovPeriode}" >
					<tr>
						<td class="TitreOv" colspan="6">
							<h1>
								<ct:FWLabel key="${entry.key.label}"/>
							</h1>
						</td>
					</tr>
					<c:forEach var="ov" items="${entry.value.ovs}" >
						<tr idEntity="${ov.id}" >
							<td>&#160;</td>	
							<td style="text-align: left;">
								${ov.descriptionOv}
							</td>
							<td>
								<c:if test="${ov.isDom2R}">
									<i style="font-size: 0.8em ">1/2</i>
								</c:if>
							</td>	
							<td data-g-amountformatter=" ">${ov.montant}</td>
							<td style="width:100px">&#160;</td>
							<td width="20%" style="text-align: right;">
								<c:if test="${viewBean.displayOvsRow}">
									${ov.noPeriode} 	
								</c:if>
							</td>		
						</tr>
					</c:forEach>			
					<tr>
						<td>&#160;</td>	
						<td style="text-align: left;font-weight: bold" colspan="1">&#160;&#160;&#160;Total</td>
						<td style="text-align: left;font-weight: bold; border-top: solid black 2px;" colspan="3" data-g-amountformatter=" " >${entry.value.sum}</td>
						<td>&#160;</td>	
					</tr>
				</c:forEach>
				
				<tr>
					<td style="font-weight: bold; text-align: left" colspan="2"> <ct:FWLabel key="${viewBean.libelleForOv}"/></td>
				    <td style="font-weight: bold; border-top:double 3px black; text-align: left" colspan="4" data-hackIE=" "  data-g-amountformatter=" " >${viewBean.montantDisponible}</td>
				</tr>
				
				<c:forEach var="entry" items="${viewBean.ovDetteCreancier}" >
					<tr>
						<td class="TitreOv" colspan="6">
							<h1>
								<ct:FWLabel key="${entry.key.label}"/>
							</h1>
						</td>
					</tr>
					<c:forEach var="ov" items="${entry.value.ovs}" >
						<tr idEntity="${ov.id}" >
							<td>&#160;</td>	
							<td style="text-align: left;">
								${ov.descriptionOv}
							</td>
							<td>&#160;</td>	
							<td data-g-amountformatter=" ">${ov.montant}</td>
							<td style="width:100px">&#160;</td>
							<td width="20%">&#160;</td>		
						</tr>
					</c:forEach>			
					<tr>
						<td>&#160;</td>	
						<td style="text-align: left;font-weight: bold" colspan="1">&#160;&#160;&#160;Total</td>
						<td style="text-align: left;font-weight: bold; border-top: solid black 2px;" colspan="3" data-g-amountformatter=" " >${entry.value.sum}</td>
						<td>&#160;</td>	
					</tr>
				</c:forEach>
		
				
				<c:forEach var="entry" items="${viewBean.ovVersement}" >
					<tr style="border-top:double 3px black;font-weight: bold;">
						<td class="TitreOv"  colspan="5">
					    	<h1><ct:FWLabel key="${viewBean.libelleForOv}"/></h1> 
					    </td>
					    <td  colspan="1" data-hackIE=" "  data-g-amountformatter=" " style="vertical-align: bottom; background-color: #B7E3FE;">
					    	${entry.value.sum}
					    </td>
					</tr>
					<c:forEach var="ov" items="${entry.value.ovs}" >
						<tr>
							<td>&#160;</td>	
				 			<td style="text-align: left">${ov.descriptionOv}</td>
							<td style="text-align: left;" colspan="3" data-g-amountformatter=" " >${ov.montant}</td>
							<td>&#160;</td>	
						</tr>
					</c:forEach>			
				</c:forEach>
				<%-- </c:otherwise>
		</c:choose>--%>
		
	</liste>