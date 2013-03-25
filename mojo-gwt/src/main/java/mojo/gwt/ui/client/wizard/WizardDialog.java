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
package mojo.gwt.ui.client.wizard;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;

import mojo.gwt.ui.client.WebDialog;

public class WizardDialog extends WebDialog {

	private static final String STYLENAME = "WizardDialog";

	private Wizard wizard;

	private DockPanel dockPanel;
	private FlowPanel bodyPanel;
	private FlowPanel buttonsPanel;

	private Button previousButton;
	private Button nextButton;
	private Button finishButton;

	public WizardDialog() {
		this(true);
	}

	public WizardDialog(boolean hasButtons) {
		dockPanel = new DockPanel();
		dockPanel.addStyleName(STYLENAME + "-dockPanel");

		bodyPanel = new FlowPanel();
		bodyPanel.addStyleName(STYLENAME + "-bodyPanel");
		dockPanel.add(bodyPanel, DockPanel.CENTER);

		if (hasButtons) {
			buttonsPanel = new FlowPanel();
			buttonsPanel.addStyleName(STYLENAME + "-buttonsPanel");
			dockPanel.add(buttonsPanel, DockPanel.SOUTH);
		}

		setWidget(dockPanel);
		addStyleName(STYLENAME);
	}

	public Wizard getWizard() {
		if (wizard == null) {
			wizard = new Wizard() {

				@Override
				protected void invokePage(Page page) {
					super.invokePage(page);

					bodyPanel.clear();
					bodyPanel.add(getActivePage().getWidget());

					if (buttonsPanel != null) {
						buttonsPanel.clear();

						if (hasPreviousPage()) {
							buttonsPanel.add(getPreviousButton());
						}

						if (hasNextPage()) {
							buttonsPanel.add(getNextButton());
						}
						else {
							buttonsPanel.add(getFinishButton());
						}
					}

					setText(getActivePage().getTitle());
				}

				@Override
				protected void onFinish() {
					hide();
				}
			};
		}

		return wizard;
	}

	private Button getPreviousButton() {
		if (previousButton == null) {
			previousButton = new Button("Previous");
			previousButton.addStyleName(STYLENAME + "-previousButton");
			previousButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					getWizard().previous();
				}
			});
		}

		return previousButton;
	}

	private Button getNextButton() {
		if (nextButton == null) {
			nextButton = new Button("Next");
			nextButton.addStyleName(STYLENAME + "-nextButton");
			nextButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					getWizard().next();
				}
			});
		}

		return nextButton;
	}

	private Button getFinishButton() {
		if (finishButton == null) {
			finishButton = new Button("Finish");
			finishButton.addStyleName(STYLENAME + "-finishButton");
			finishButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					getWizard().finish();
				}
			});

		}

		return finishButton;
	}
}
