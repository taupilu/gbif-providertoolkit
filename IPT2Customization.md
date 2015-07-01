# How to Style Your IPT #



## Introduction ##

It couldn't be easier to customize the style of your IPT. The following guide explains how to customize the IPT, and preserve your customization when upgrading your IPT's version.

In short, customization can be achieved by applying CSS overrides. Continue reading the instructions that follow for more details.

## Instructions ##

  1. Apply your desired CSS overrides in custom.css (choose a different color scheme for example). You can find this file inside the deployed war folder, e.g. $tomcat/webapps/ipt/styles The original [custom.css](https://code.google.com/p/gbif-providertoolkit/source/browse/trunk/gbif-ipt/src/main/webapp/styles/custom.css) comes pre-populated with a set of CSS overrides that give the IPT its fresh new look, basically changing the shades of green displayed all over the IPT in tables, links, etc.
  1. Upon completion, backup the custom.css file somewhere safe so that it can be added once again after each IPT upgrade, which unfortunately will overwrite the custom.css file each time.

Take a look at the before and after screenshots below showing the effects of the default custom.css file.

**Before applying custom.css**
![http://gbif-providertoolkit.googlecode.com/svn/trunk/gbif-ipt-docs/ipt2/v205/IPTDefaultStyle.png](http://gbif-providertoolkit.googlecode.com/svn/trunk/gbif-ipt-docs/ipt2/v205/IPTDefaultStyle.png)
**After applying custom.css**
![http://gbif-providertoolkit.googlecode.com/svn/trunk/gbif-ipt-docs/ipt2/v205/IPTCustomizedStyle.png](http://gbif-providertoolkit.googlecode.com/svn/trunk/gbif-ipt-docs/ipt2/v205/IPTCustomizedStyle.png)