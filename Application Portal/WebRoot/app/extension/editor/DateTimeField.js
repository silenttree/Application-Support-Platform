/* * 14Feb 2012 - DateTimeField
 * Francois Marie De Mey - eeddow@gmail
 * 
 * Date-time field with one common input box and two trigger/picker
 */
(function() {
    var 
    //Combine a date and a time. Doesn't modify any 
    combine = function(me, date, time) {
        if(Ext.isString(date)) date = me.parseDate(date);
        if(!date) date = new Date();
        if(Ext.isString(time)) time = me.parseDate(time);
        if(!time) time = new Date();
        var rv = new Date(date);
        rv.setHours(time.getHours());
        rv.setMinutes(time.getMinutes());
        rv.setSeconds(time.getSeconds());
        return rv;
    },
    //Trick the 'format' option for some timeField functions needing only time to be used
    oldFormatFunction = function(methodName) {
        return function() {
            var me = this, rv, dateCtl = me.datetimeField;
            me.format = dateCtl.timeFormat;
            rv = Ext.form.TimeField.prototype[methodName].apply(me, arguments);
            me.format = dateCtl.format;                    
            return rv;
        };
    };
    
    Ext.define('Asc.extension.editor.DateTimeField', {
        extend: 'Ext.form.DateField',
        alias: 'widget.ascdatetimefield',
        requires: ['Ext.form.TimeField'],


        trigger2Cls: Ext.baseCSSPrefix + 'form-time-trigger',


        /**
         * @cfg {Number} pickerMaxHeight
         * The maximum height of the {@link Ext.picker.Time} dropdown.
         */
        /**
         * @cfg {String} minTimeText
         * The error text to display when the entered time is before {@link #minTime}.
         */
        /**
         * @cfg {Date/String} minTime
         * The minimum allowed time. Can be either a Javascript date object with a valid time value or a string time in a
         * valid format -- see {@link #format} and {@link #altFormats}.
         */
        /**
         * @cfg {String} maxTimeText
         * The error text to display when the entered time is after {@link #maxTime}.
         */
        /**
         * @cfg {Date/String} maxTime
         * The maximum allowed time. Can be either a Javascript date object with a valid time value or a string time in a
         * valid format -- see {@link #format} and {@link #altFormats}.
         */
        /**
         * @cfg {Number} increment
         * The number of minutes between each time value in the list.
         */
        /**
         * @cfg {String} format
         * The default date format string which can be overriden for localization support. The format must be valid
         * according to {@link Ext.Date#parse}.
         */
        format : "m/d/Y H:i",
        /**
         * @cfg {String} format
         * The default time format string used to express only the time part. The format must be valid
         * according to {@link Ext.Date#parse}.
         */
        timeFormat: "H:i",
        /**
         * @cfg {String} altFormats
         * Multiple date formats separated by "|" to try when parsing a user input value and it does not match the defined
         * format.
         */
        altFormats : "m/d/Y H:i|n/j/Y H:i|n/j/y H:i|m/j/y H:i|n/d/y H:i|m/j/Y H:i|n/d/Y H:i|m-d-y H:i|m-d-Y H:i|m/d H:i|m-d H:i|md H:i|mdy H:i|mdY H:i|d H:i|Y-m-d H:i|n-j H:i|n/j H:i",


        initComponent: function() {
            var me = this, elm, config = {
                datetimeField: me,
                
                //share the format
                format: me.format,
                altFormats: me.altFormats,
                
                //Straight forwards
                minText: me.minTimeText,
                maxText: me.maxTimeText,
                
                //Don't set the value, juste the "time" part of the value
                onListSelectionChange: function(list, recordArray) {
                    var me = this,
                        record = recordArray[0],
                        val = record ? record.get('date') : null;
                    
                    if (!me.ignoreSelection) {
                        me.skipSync = true;
                        me.datetimeField.setValue(combine(me, me.getValue(), val));
                        me.skipSync = false;
                        me.fireEvent('select', me, val);
                        me.picker.clearHighlight();
                        me.collapse();
                        me.inputEl.focus();
                    }
                },
                
                //Oops... we must use the 'time' format some times
                createPicker: oldFormatFunction('createPicker'),
                setLimit: oldFormatFunction('setLimit')
            }, forward = {
                minValue: me.minTime,
                maxValue: me.maxTime,
                increment: me.increment,
                pickerMaxHeight: me.pickerMaxHeight
            };
            for(elm in forward)
                if(forward.hasOwnProperty(elm) && {}.u!== forward[elm])
                    config[elm] = forward[elm];
            //Create the "virtual" time field
            me.timeField = new Ext.form.TimeField(config);
            this.callParent();
        },
        onRender: function() {
            var me = this, triggers;


            me.callParent(arguments);
            triggers = me.triggerEl;


            me.dateTrigger = triggers.item(0);
            me.timeTrigger = triggers.item(1);
            
            Ext.apply(me.timeField, {
                rendered: true,
                el: me.el,
                inputEl: me.inputEl,
                bodyEl: me.bodyEl,
                errorEl: me.errorEl,
                doc: me.doc
            });
            
        },
        mimicBlur: function(e) {
            var me = this,
                picker = me.timeField.picker;
            // ignore mousedown events within the picker element
            if (!picker || !e.within(picker.el, false, true)) {
                me.callParent(arguments);
            }
        },


        //Suddenly, a wide trigger appears...
        getTriggerWidth: function() {
            return this.hideTrigger || this.readOnly ? 0 : this.timeTrigger.getWidth() + this.dateTrigger.getWidth() + this.triggerWrap.getFrameWidth('lr');
        }, 


        //Manually expand our "virtual" time field
        onTrigger2Click: function() {
            var me=this;
            me.collapse();
            me.timeField.onTriggerClick();
        },
        onTriggerClick: function() {
            var me=this;
            me.timeField.collapse();
            me.callParent(arguments);
        },
        
        //Don't set the value, juste the "date" part of the value
        onSelect: function(m, d) {
            var me = this;


            me.setValue(combine(me, d, me.getValue()));
            me.fireEvent('select', me, d);
            me.collapse();
        },
        
        //Combinations
        getErrors: function(value) {
            var me = this,
                format = Ext.String.format,
                errors = me.callParent(arguments),
                minValue = me.minTime,
                maxValue = me.maxTime,
                date;


            value = me.formatDate(value || me.processRawValue(me.getRawValue()));


            if (value === null || value.length < 1) { // if it's blank and textfield didn't flag it then it's valid
                 return errors;
            }


            date = me.parseDate(value);
            if (!date) {
                //avoid double format error
                return errors;
            }


            if (minValue && date < minValue) {
                errors.push(format(me.minText, me.formatDate(minValue)));
            }


            if (maxValue && date > maxValue) {
                errors.push(format(me.maxText, me.formatDate(maxValue)));
            }


            return errors;
        },
        setValue: function() {
            var me = this, args = arguments, timeField = me.timeField;
            timeField.setValue.apply(timeField, arguments);
            return me.callParent(args);
        },
        
        //Straight forwards
        setMaxTime: function() {
            var timeField = this.timeField;
            return timeField.setMaxValue.apply(timeField, arguments);
        },
        setMinTime: function() {
            var timeField = this.timeField;
            return timeField.setMinValue.apply(timeField, arguments);
        }
    
    });
})();