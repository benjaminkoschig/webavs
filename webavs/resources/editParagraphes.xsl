<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output 
      method="html"
      encoding="ISO-8859-1"
      doctype-public="-//W3C//DTD HTML 4.01//EN"
      doctype-system="http://www.w3.org/TR/html4/strict.dtd"
      indent="yes" />

   <xsl:template match="document">
		<tr bgcolor="#B3C4DB" >
			<td colspan="3" height="40"> </td>
		</tr>
		<xsl:apply-templates select="paragraph" />
		<tr bgcolor="#B3C4DB">
			<td colspan="3" height="40"> </td>
		</tr>
   </xsl:template>

   <xsl:template match="paragraph">
		<tr>
			<td bgcolor="#B3C4DB">
				 <strong><xsl:value-of select="@titre"/></strong>
			 </td>
			 <xsl:choose>					
					<xsl:when test="count(child::text)>0">
						<xsl:choose>
							<xsl:when test="child::text[@isEditable='true']">
								<td>
									<xsl:apply-templates select="text">
										<xsl:with-param name="position" select="@position"></xsl:with-param>
										<xsl:with-param name="niveau" select="@niveau"></xsl:with-param>
									</xsl:apply-templates>
								</td>
							</xsl:when>
							<xsl:otherwise>
								<td style="background-color:#CCCCCC;color:#636363">
									<xsl:apply-templates select="text" />
								</td>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
					<td>
						<xsl:apply-templates select="array" />
					</td>
					</xsl:otherwise>
			</xsl:choose>
			
			<td bgcolor="#B3C4DB" width="40"> </td>
		</tr>
   </xsl:template>
   
  <xsl:template match="text">
	  <xsl:param name="position" select="0"></xsl:param>
	  <xsl:param name="niveau" select="0"></xsl:param>
		<xsl:choose>
				<xsl:when test="@isEditable='true'">
				   <textarea rows="10" cols="80" style="width: 100%" class="editable" name="{$niveau}_{$position}">
					   <xsl:value-of select="."/>
				   </textarea>
				</xsl:when>
				<xsl:otherwise>	  
					<br></br>
						<label><xsl:value-of select="." disable-output-escaping="yes"/></label>
					<br></br>
				</xsl:otherwise>
			</xsl:choose>
   </xsl:template>   
   
   <xsl:template match="array">
   	   <textarea rows="10" cols="80" style="width: 100%" class="editable">
			<table>
				<tbody>
					<xsl:apply-templates select="line" />
				</tbody>
			</table>
	   </textarea>
   </xsl:template>
   
  <xsl:template match="line">
		<tr>
			<xsl:apply-templates select="col" />
		</tr>
   </xsl:template>
   
   <xsl:template match="col">
		<td>
			<xsl:value-of select="."/>
		</td>
   </xsl:template>

</xsl:stylesheet>
