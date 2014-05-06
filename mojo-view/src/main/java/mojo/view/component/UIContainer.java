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
package mojo.view.component;

import java.util.ArrayList;
import java.util.List;

public abstract class UIContainer extends UIComponent {

	private List<UIComponent> children;

	public UIContainer add(UIComponent c) {
		for (UIComponent comp : getChildren()) {
			if (c.getId().equals(comp.getId())) {
				throw new RuntimeException("Duplicate component id in this container: " + c.getId());
			}
		}

		getChildren().add(c);
		c.setParent(this);

		return this;
	}

	/**
	 * Returns child component at the specified index
	 */
	public UIComponent get(int index) {
		return getChildren().get(index);
	}

	/**
	 * Get a child component by looking it up with the given path.
	 * 
	 * A component path consists of component ids separated by colons, e.g.
	 * "b:c" identifies a component "c" inside container "b" inside this
	 * container.
	 */
	public UIComponent get(String path) {
		String[] ids = path.split(":");
		UIContainer cont = this;

		for (int i = 0; i < ids.length; ++i) {
			if (cont.hasChildren()) {
				for (UIComponent comp : cont.getChildren()) {
					if (ids[i].equals(comp.getId())) {

						// Before the last step we have
						// to have container types.

						if (i < ids.length - 1) {
							if (comp instanceof UIContainer) {
								cont = (UIContainer) comp;
								break;
							}
							else {
								// failure result
								return null;
							}
						}

						// At the last step we don't care
						// of the component type.

						if (i == ids.length - 1) {
							// success result
							return comp;
						}
					}
				}
			}
		}

		// not found result
		return null;
	}

	protected List<UIComponent> getChildren() {
		if (children == null) {
			children = new ArrayList<UIComponent>();
		}

		return children;
	}

	protected boolean hasChildren() {
		return children != null && !children.isEmpty();
	}
}
