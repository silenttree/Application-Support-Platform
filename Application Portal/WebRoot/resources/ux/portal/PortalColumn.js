/**
 * @class Ext.ux.portal.PortalColumn
 * @extends Ext.container.Container
 * A layout column class used internally be {@link Ext.ux.portal.PortalPanel}.
 */
Ext.define('Ext.ux.portal.PortalColumn', {
    extend: 'Ext.container.Container',
    alias: 'widget.uxPortalColumn',

    requires: [
        'Ext.layout.container.Anchor',
        'Ext.ux.portal.Portlet'
    ],

    layout: 'anchor',
    defaultType: 'uxPortlet',
    cls: 'x-portal-column'

    // This is a class so that it could be easily extended
    // if necessary to provide additional behavior.
});