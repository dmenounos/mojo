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
package mojo.web.core.v1;

import org.springframework.beans.factory.FactoryBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mojo.dao.AuditContext;

public class ProxyContextFactoryBean implements FactoryBean<ProxyContext> {

	private static final Logger logger = LoggerFactory.getLogger(ProxyContextFactoryBean.class);

	@Override
	public ProxyContext getObject() throws Exception {
		logger.debug("Creating proxy context object");
		return new ProxyContext();
	}

	@Override
	public Class<?> getObjectType() {
		return AuditContext.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
