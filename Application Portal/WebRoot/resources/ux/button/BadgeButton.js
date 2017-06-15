/**
 * @class Ext.ux.button.BadgeButton
 * @extends Ext.button.Button
 * 
 * @author Tomasz Jagusz
 * based on: http://blogs.walkingtree.in/2012/07/16/badge-text-in-extjs-4-1/
 * based on: https://github.com/vondervick/ext/tree/master/badge_button
 */

Ext.define('Ext.ux.button.BadgeButton', {
    extend: 'Ext.button.Button',
    alias: 'widget.badgebutton',

    config: {
        badgeText: ''
    },

    renderTpl: ['<span id="{id}-btnWrap" class="{baseCls}-wrap', '<tpl if="splitCls"> {splitCls}</tpl>', '{childElCls}" unselectable="on">', '<span id="{id}-btnEl" class="{baseCls}-button {baseCls}-hasBadge">', '<span id="{id}-btnInnerEl" class="{baseCls}-inner {innerCls}', '{childElCls}" unselectable="on">', '{text}', '</span>', '<tpl if="badgeText"><span id="{id}-btnBadge" class="{baseCls}-badgeCls" reference="badgeElement" unselectable="on">{badgeText}</span></tpl>', '<span role="img" id="{id}-btnIconEl" class="{baseCls}-icon-el {iconCls}', '{childElCls} {glyphCls}" unselectable="on" style="', '<tpl if="iconUrl">background-image:url({iconUrl});</tpl>', '<tpl if="glyph && glyphFontFamily">font-family:{glyphFontFamily};</tpl>">', '<tpl if="glyph">&#{glyph};</tpl><tpl if="iconCls || iconUrl">&#160;</tpl>', '</span>', '</span>', '</span>',
    // if "closable" (tab) add a close element icon
    '<tpl if="closable">', '<span id="{id}-closeEl" class="{baseCls}-close-btn" title="{closeText}" tabIndex="0"></span>', '</tpl>'],

    childEls: ['btnBadge'],

    initComponent: function(config) {
        Ext.apply(this.config, config);
        this.callParent(arguments);

        this.addEvents(
        /**
         * @event badgetextchange
         * Fired when the button's badge text is changed by the {@link #setBadgeText} method.
         * @param {Ext.ux.container.BadgeButton} this
         * @param {String} oldText
         * @param {String} newText
         */'badgetextchange');
    },

    getTemplateArgs: function() {
        var me = this,
            glyph = me.glyph,
            glyphFontFamily = Ext._glyphFontFamily,
            glyphParts;

        if (typeof glyph === 'string') {
            glyphParts = glyph.split('@');
            glyph = glyphParts[0];
            glyphFontFamily = glyphParts[1];
        }

        return {
            innerCls: me.getInnerCls(),
            splitCls: me.getSplitCls(),
            iconUrl: me.icon,
            iconCls: me.iconCls,
            glyph: glyph,
            glyphCls: glyph ? me.glyphCls : '',
            glyphFontFamily: glyphFontFamily,
            text: me.text || '&#160;',
            badgeText: me.badgeText || undefined
        };
    },


    /**
     * Sets this Button's Badge text
     * @param {String} text The button badge text
     * @return {Ext.ux.container.BadgeButton} this
     */
    setBadgeText: function(text) {
        text = text || '';
        var me = this,
            oldBadgeText = me.badgeText || '';

        if (text != oldBadgeText) {

            if (Ext.isEmpty(text)) {
                me.btnBadge.addCls('hide-badge');
            } else {
                me.btnBadge.removeCls('hide-badge');
            }

            me.badgeText = text;
            if (me.rendered) {
                me.btnBadge.update(text || '');
                //me.setComponentCls();
                if (Ext.isStrict && Ext.isIE8) {
                    // weird repaint issue causes it to not resize
                    me.el.repaint();
                }
                //me.updateLayout();
            }
            me.fireEvent('badgetextchange', me, oldBadgeText, text);
        }
        return me;
    }
});