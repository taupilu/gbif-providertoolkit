<#include "/WEB-INF/pages/inc/header.ftl">
 <title><@s.text name="admin.extensions.title"/></title>
 <#assign currentMenu = "admin"/>
<#include "/WEB-INF/pages/inc/menu.ftl">

<h1><@s.text name="admin.extensions.title"/></h1>

<#list extensions as ext>	
<a name="${ext.rowType}"></a>          
<div class="definition">	
  <div class="title">
  	<div class="head">
        <a href="extension.do?id=${ext.rowType}">${ext.title}</a>
  	</div>
  	<div class="actions">
	  <form action='extension.do' method='post'>
		<input type='hidden' name='id' value='${ext.rowType}' />
		<input type='submit' name='delete' value='Remove' />
  	  </form>
  	</div>
  </div>
  <div class="body">
      	<div>
			${ext.description!}
			<#if ext.link?has_content><br/><@s.text name="basic.seealso"/> <a href="${ext.link}">${ext.link}</a></#if>              	
      	</div>
      	<div class="details">
      		<table>
          		<tr><th><@s.text name="extension.properties"/></th><td>${ext.properties?size}</td></tr>
          		<tr><th><@s.text name="basic.name"/></th><td>${ext.name}</td></tr>
          		<tr><th><@s.text name="basic.namespace"/></th><td>${ext.namespace}</td></tr>
          		<tr><th><@s.text name="extension.rowtype"/></th><td>${ext.rowType}</td></tr>
          		<tr><th><@s.text name="basic.keywords"/></th><td>${ext.subject!}</td></tr>
      		</table>
      	</div>
  </div>
</div>
</#list>

<#if (numVocabs>0)>
<hr/>
<h3><@s.text name="extension.vocabularies.title"/></h3>
<p>
	<@s.text name="extension.vocabularies.last.update"><@s.param>${vocabsLastUpdated?date?string.medium}</@s.param></@s.text>
	  <form action='extensions.do' method='post'>
		<input type='submit' name='updateVocabs' value='Update' />
	<@s.text name="extension.vocabularies.number"><@s.param>${numVocabs}</@s.param></@s.text>
  	  </form>
</p>
</#if>
<hr/>

<h3><@s.text name="extension.further.title"/></h3>

<#list newExtensions as ext>
<div class="definition">	
  <div class="title">
  	<div class="head">
		${ext.title}
  	</div>
  	<div class="actions">
	  <form action='extension.do' method='post'>
		<input type='hidden' name='url' value='${ext.url}' />
		<input type='submit' name='install' value='Install' />
  	  </form>
  	</div>
  </div>
  <div class="body">
      	<div>
		${ext.description!}
      	</div>
      	<div class="details">
      		<table>
          		<tr><th><@s.text name="extension.rowtype"/></th><td>${ext.rowType!}</td></tr>
          		<tr><th><@s.text name="basic.keywords"/></th><td>${ext.subject!}</td></tr>
      		</table>
      	</div>
  </div>
</div>
</#list>

<#include "/WEB-INF/pages/inc/footer.ftl">