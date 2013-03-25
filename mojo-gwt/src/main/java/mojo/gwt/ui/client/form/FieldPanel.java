/*
 * Copyright (C) 2010 Dimitrios Menounos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mojo.gwt.ui.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

import mojo.gwt.ui.client.util.WebUtils;

/**
 * Field wrapper panel.
 */
public class FieldPanel<F extends FocusWidget> extends Composite {

	private static final Resources res;
	private static final Style css;

	static {
		res = GWT.create(Resources.class);
		res.style().ensureInjected();
		StyleInjector.flush();
		css = res.style();
	}

	private F[] fields;
	private Label label;
	private String error;

	public FieldPanel(Label label, F... fields) {
		this(label, null, fields);
	}

	public FieldPanel(Label label, Composite wrap, F... fields) {
		Panel panel = new FlowPanel();
		panel.addStyleName(css.FieldPanel());

		if (label != null) {
			this.label = label;
			label.addStyleName(css.label());
			panel.add(label);
		}

		this.fields = fields;

		for (F field : fields) {
			field.addFocusHandler(new OnFocus());
			field.addBlurHandler(new OnBlur());
			field.addStyleName(css.field());

			if (wrap == null) {
				panel.add(field);
			}
		}

		if (wrap != null) {
			panel.add(wrap);
		}

		initWidget(panel);
	}

	public Label getLabel() {
		return label;
	}

	public F[] getFields() {
		return fields;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;

		if (error != null) {
			WebUtils.tip(error, fields[0]);
			addStyleName(css.error());
		}
		else {
			removeStyleDependentName("error");
			removeStyleName(css.error());
		}
	}

	private class OnFocus implements FocusHandler {

		@Override
		public void onFocus(FocusEvent event) {
			addStyleName(css.focus());
		}
	}

	private class OnBlur implements BlurHandler {

		@Override
		public void onBlur(BlurEvent event) {
			removeStyleName(css.focus());
		}
	}

	public interface Resources extends ClientBundle {

		@Source("FieldPanel.css")
		Style style();
	}

	public interface Style extends CssResource {

		String FieldPanel();

		String focus();

		String error();

		String field();

		String label();
	}
}
