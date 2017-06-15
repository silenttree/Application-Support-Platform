/**

 * Month field component.

 * @extends Ext.form.field.Date

 * @author Maicon Schelter

 */

Ext.define('Asc.extension.editor.Month',{

	

	 extend		: 'Ext.form.field.Date'

	,alias		: ['widget.ascmonthfield']

	

	/**

	 * @cfg {String} format

	 * Format date component.

	 */

	,format		: 'Y-m'

	

	/**

	 * Create component picker month.

	 * @method createPicker

	 * @return {Object}

	 * @private

	 */

	,createPicker : function()

	{

		var  me		= this

			,picker	= me.monthPicker;

		

		if(!picker)

		{

			me.monthPicker = picker = new Ext.picker.Month({

				 ownerCt	: me.ownerCt

				,renderTo	: document.body

				,floating	: true

				,shadow		: false

				,small		: me.showToday === false

				,listeners : {

					 scope			: me

					,cancelclick	: me.onCancelClick

					,okclick		: me.onOkClick

					,yeardblclick	: me.onOkClick

					,monthdblclick	: me.onOkClick

				}

			});

			

			if(!me.disableAnim)

			{

				picker.hide();

				me.isExpanded = false;

			}

			

			me.on('beforehide', Ext.Function.bind(me.hideMonthPicker, me, [false]));

		}

		

		return picker;

	}

	

	/**

	 * Click component method.

	 * @method onOkClick

	 * @param {Object} picker

	 * @param {String} value

	 * @private

	 */

	,onOkClick : function(picker, value)

	{

		var  me		= this

			,month	= value[0]

			,year	= value[1]

			,date	= new Date(year, month, 1);

		

		if(date.getMonth() !== month)

		{

			date = Ext.Date.getLastDateOfMonth(new Date(year, month, 1));

		}

		

		me.activeDate = date = Ext.util.Format.date(date, me.format);

		me.setValue(date);

		me.hideMonthPicker();

	}

	

	/**

	 * Cancel selection and set last value.

	 * @method onCancelClick

	 * @private

	 */

	,onCancelClick : function()

	{

		this.setValue(this.activeDate);

		this.hideMonthPicker();

	}

	

	/**

	 * Show or hide picker month.

	 * @method hideMonthPicker

	 * @param {Boolean} animate

	 * @return {Object}

	 */

	,hideMonthPicker : function(animate)

	{

		var  me		= this

			,picker	= me.picker;

		

		if(picker)

		{

			if(me.shouldAnimate(animate))

			{

				me.runAnimation(true);

			}

			else

			{

				picker.hide();

				me.isExpanded = false;

			}

		}

		

		return me;

	}

	

	/**

	 * Return animate show or hide component.

	 * @method shouldAnimate

	 * @param {Boolean} animate

	 * @return {Boolean}

	 */

	,shouldAnimate : function(animate)

	{

		return Ext.isDefined(animate) ? animate : !this.disableAnim;

	}

	

	/**

	 * Show or hide picker month animate.

	 * @method runAnimation

	 * @param {Boolean} isHide

	 */

	,runAnimation : function(isHide)

	{

		var	 me		= this

			,picker	= this.picker

			,options = {

				 duration : 200

				,callback : function()

				{

					if(isHide)

					{

						picker.hide();

						me.isExpanded = false;

					}

					else

					{

						picker.show();

						me.isExpanded = true;

					}

				}

			};

		

		if(isHide)

		{

			picker.el.slideOut('t', options);

		}

		else

		{

			picker.el.slideIn('t', options);

		}

	}

});