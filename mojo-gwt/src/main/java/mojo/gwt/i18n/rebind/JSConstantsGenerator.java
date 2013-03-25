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
package mojo.gwt.i18n.rebind;

import java.io.PrintWriter;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class JSConstantsGenerator extends Generator {

	public static final String NS = "i18n";

	/** The qualified type name */
	private String typeName;

	/** GWT introspection structure */
	private JClassType classType;

	/** The type package name */
	private String packageName;

	/** The generated simple name */
	private String generatedClassName;

	private void initTypes(GeneratorContext context) {

		//
		// Lookup nested class type.
		//

		classType = context.getTypeOracle().findType(typeName);

		if (classType == null) {
			throw new RuntimeException("Unable to find " + typeName);
		}

		if (classType.isInterface() == null) {
			throw new RuntimeException(typeName + " is not an interface");
		}

		packageName = classType.getPackage().getName();
		generatedClassName = classType.getName().replace(".", "_") + "Impl";
	}

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
		this.typeName = typeName;

		try {
			initTypes(context);

			PrintWriter printWriter = context.tryCreate(logger, packageName, generatedClassName);

			//
			// If the generated type exists printWriter will be null.
			//

			if (printWriter != null) {
				System.out.println();
				System.out.println("### Generating class: " + packageName + "." + generatedClassName);

				ClassSourceFileComposerFactory composer;
				composer = new ClassSourceFileComposerFactory(packageName, generatedClassName);
				composer.addImplementedInterface(classType.getSimpleSourceName());
				composer.addImport(typeName);

				SourceWriter sourceWriter = composer.createSourceWriter(context, printWriter);

				StringBuilder sb = new StringBuilder();

				for (JMethod method : classType.getMethods()) {
					System.out.println("### Message: " + typeName + "." + method.getName());

					String stringName = String.class.getCanonicalName();
					String methodName = method.getName();
					JParameter[] params = method.getParameters();

					//
					// Build method signature.
					//

					sb.append("public native " + stringName + " " + methodName + "(");

					for (int i = 0; i < params.length; i++) {
						JParameter param = params[i];
						sb.append(param.getType().getQualifiedSourceName() + " param" + i);

						if (i < params.length - 1) {
							sb.append(", ");
						}
					}

					sb.append(")\n");

					//
					// Build method body.
					//

					sb.append("/*-{\n");
					sb.append("return $wnd." + NS + "." + typeName + "." + methodName + "(");

					for (int i = 0; i < params.length; i++) {
						sb.append("param" + i);

						if (i < params.length - 1) {
							sb.append(", ");
						}
					}

					sb.append(");\n");
					sb.append("}-*/;\n");

				}

				// System.out.println(sb);

				sourceWriter.println(sb.toString());
				sourceWriter.commit(logger);
			}
		}
		catch (RuntimeException e) {
			logger.log(TreeLogger.ERROR, e.getMessage());
			throw new UnableToCompleteException();
		}

		return packageName + '.' + generatedClassName;
	}

	protected void test(JClassType classType, String typeName) {
		System.out.println("### ----------------------------------------------------");
		System.out.println("### typeName.equals(classType.getQualifiedSourceName()) " + typeName.equals(classType.getQualifiedSourceName()));
		System.out.println("### classType.getPackage().getName()   " + classType.getPackage().getName());
		System.out.println("### classType.getQualifiedSourceName() " + classType.getQualifiedSourceName());
		System.out.println("### classType.getName()                " + classType.getName());
		System.out.println("### classType.getSimpleSourceName()    " + classType.getSimpleSourceName());
	}
}
