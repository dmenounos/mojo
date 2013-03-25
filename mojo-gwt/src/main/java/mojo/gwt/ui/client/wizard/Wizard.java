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

public class Wizard {

	private Page[] pages;
	private int active;

	public void init(Page... pages) {
		this.pages = pages;

		if (hasPages()) {
			begin();
		}
	}

	public Page[] getPages() {
		return pages;
	}

	public Page getActivePage() {
		return hasPages() ? pages[active] : null;
	}

	public boolean hasPages() {
		return pages != null && pages.length > 0;
	}

	public boolean hasNextPage() {
		return pages != null && active < pages.length - 1;
	}

	public boolean hasPreviousPage() {
		return pages != null && active > 0;
	}

	/**
	 * Invoke the first page.
	 */
	public void begin() {
		if (!hasPages()) {
			throw new RuntimeException("No pages");
		}

		invokePage(pages[0]);
	}

	/**
	 * Invoke the next page, but only after submitting the active one.
	 */
	public void next() {
		if (!hasNextPage()) {
			throw new RuntimeException("No next page");
		}

		if (getActivePage().onSubmit()) {
			invokePage(pages[++active]);
		}
	}

	/**
	 * Invoke the previous page.
	 */
	public void previous() {
		if (!hasPreviousPage()) {
			throw new RuntimeException("No previous page");
		}

		invokePage(pages[--active]);
	}

	/**
	 * Submit the last page and call onFinish().
	 */
	public void finish() {
		if (!hasPages()) {
			throw new RuntimeException("No pages");
		}

		if (pages[pages.length - 1].onSubmit()) {
			onFinish();
		}
	}

	protected void invokePage(Page page) {
		if (page.getContext() == null) {
			page.init(this);
		}
	}

	/**
	 * Callback on wizzard finish.
	 */
	protected void onFinish() {
	}
}
