<head>
    <title><@s.text name="region.title"/></title>
    <meta name="resource" content="${region.resource.title}"/>
    <meta name="submenu" content="occ"/>
</head>
	

<h2>${region.label}</h2>  

<@s.form>
	<fieldset>
	<table id="details">
		<tr>
		  <th><@s.text name="region.label"/></th>
		  <td>${region.label}</td>
		</tr>
		<tr>
		  <th><@s.text name="region.type"/></th>
		  <td>${region.type}</td>
		</tr>
		<#if region.parent??>
		<tr>
		  <th><@s.text name="region.parent"/></th>
		  <@s.url id="occRegionUrl" action="occRegion" namespace="/" includeParams="none">
			<@s.param name="resource_id" value="${resource_id}"/>
			<@s.param name="id" value="${region.parent.id}"/>
		  </@s.url>
		  <td><a href="${occRegionUrl}">${region.parent}</a></td>
		</tr>
		</#if>
		<tr>
		  <th><@s.text name="region.occTotal"/></th>
		  <td>${region.occTotal!0}</td>
		</tr>
		<!-- 
		<tr>
		  <th>Number of Taxa</th>
		  <td>0</td>
		</tr>
		 -->
	</table>
	</fieldset>
</@s.form>

<br/>

<div id="loc-geoserver" class="stats map">
	<label><@s.text name="stats.occPointMap"/></label>
	<img src="${geoserverMapUrl}" width="${width}" height="${height}" />
</div>

<br class="clearfix" />

<#include "/WEB-INF/pages/inc/occurrenceList.ftl">  