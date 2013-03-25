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
package mojo.gwt.data.client.util;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Event handler management support.
 */
public class Observable {

	private HandlerManager handlerManager;

	protected Observable() {
		handlerManager = new HandlerManager(this);
	}

	protected HandlerManager getHandlerManager() {
		return handlerManager;
	}

	/**
	 * Convenience method.
	 */
	protected void fireEvent(GwtEvent<?> event) {
		getHandlerManager().fireEvent(event);
	}

	/**
	 * Convenience method. Detects and initializes self aware event handlers.
	 */
	protected <H extends EventHandler> HandlerRegistration addHandler(Type<H> type, H handler) {
		HandlerRegistration registration = getHandlerManager().addHandler(type, handler);

		if (handler instanceof Observer) {
			Observer observer = (Observer) handler;
			observer.setRegistration(registration);
		}

		return registration;
	}
}
