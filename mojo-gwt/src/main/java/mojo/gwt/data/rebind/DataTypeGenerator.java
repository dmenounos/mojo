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
package mojo.gwt.data.rebind;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import mojo.gwt.data.client.type.ClassField;
import mojo.gwt.data.client.type.ClassType;
import mojo.gwt.data.client.type.ClassTypeRegistry;
import mojo.gwt.data.client.type.DataField;
import mojo.gwt.data.client.type.DataType;

public class DataTypeGenerator extends Generator {

	protected static final Pattern propertyPattern = Pattern.compile("(set|get|is)([A-Z][a-zA-Z_0-9]*)+");

	/** The qualified target name */
	protected String targetClassName;

	/** GWT introspection object */
	protected JClassType targetClassType;

	/** The type package name */
	protected String packageName;

	/** The generated simple name */
	protected String generatedClassName;

	/** The qualified parent type name */
	protected String parentClassName;

	/** GWT introspection object */
	protected JClassType parentClassType;

	protected void initTypes(GeneratorContext context) {

		//
		// Lookup nested class type.
		//

		targetClassType = context.getTypeOracle().findType(targetClassName);

		if (targetClassType == null) {
			throw new RuntimeException("Unable to find " + targetClassName);
		}

		if (targetClassType.isInterface() == null) {
			throw new RuntimeException(targetClassName + " is not an interface");
		}

		packageName = targetClassType.getPackage().getName();
		generatedClassName = targetClassType.getName().replace(".", "_") + "Impl";

		//
		// Extract the parent simple name from the nested one.
		// GWT classType.getName() == JDK class.getSimpleName()
		//

		String parentName = targetClassType.getName();
		int dotIndex = parentName.indexOf(".");

		if (dotIndex == -1) {
			throw new RuntimeException(targetClassName + "is not a nested interface");
		}

		parentName = parentName.substring(0, dotIndex);

		//
		// Build the parent qualified name.
		//

		parentClassName = packageName + "." + parentName;

		//
		// Lookup parent class type.
		//

		parentClassType = context.getTypeOracle().findType(parentClassName);

		if (parentClassType == null) {
			throw new RuntimeException("Unable to find " + parentClassType);
		}
	}

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String targetClassName) throws UnableToCompleteException {
		this.targetClassName = targetClassName;

		try {
			initTypes(context);

			PrintWriter printWriter = context.tryCreate(logger, packageName, generatedClassName);

			//
			// If the generated type exists printWriter will be null.
			//

			if (printWriter != null) {
				System.out.println();
				System.out.println("### Generating class: " + packageName + "." + generatedClassName);

				JClassType classTypeClassType = context.getTypeOracle().findType(ClassType.class.getName());

				// the gathered parent property names and types
				Map<String, String> properties = new HashMap<String, String>();

				for (JMethod method : parentClassType.getMethods()) {
					String methodName = method.getName();
					JType returnType = method.getReturnType();
					JParameter[] params = method.getParameters();

					Matcher propertyMatcher = propertyPattern.matcher(methodName);

					if (propertyMatcher.matches()) {
						String propertyName = propertyMatcher.group(2);
						propertyName = Character.toLowerCase(propertyName.charAt(0)) + propertyName.substring(1);

						String propertyType = null;

						if (params.length == 0) {
							// method is property getter
							propertyType = returnType.getQualifiedSourceName();
						}
						else if (params.length == 1) {
							// method is property setter
							propertyType = params[0].getType().getQualifiedSourceName();
						}
						else {
							throw new RuntimeException("Unable to find type for property: " + propertyName);
						}

						JClassType propertyClassType = context.getTypeOracle().findType(propertyType);
						boolean isScalar = ClassTypeRegistry.isScalar(propertyType);
						boolean isModelClass = false;

						// EXPERIMENTAL: Examine whether the property type
						// has a ClassType also. e.g., like this parent

						for (JField innerField : propertyClassType.getFields()) {
							if (innerField.isStatic()) {
								JClassType innerFieldType = innerField.getType().isClassOrInterface();

								if (innerFieldType != null && classTypeClassType.isAssignableFrom(innerFieldType)) {
									isModelClass = true;
								}
							}
						}

						if ((isScalar || isModelClass) && !properties.containsKey(propertyName)) {
							System.out.println("### Property: " + propertyType + " " + propertyName);
							properties.put(propertyName, propertyType);
						}
					}
				}

				ClassSourceFileComposerFactory composer;
				composer = new ClassSourceFileComposerFactory(packageName, generatedClassName);
				composer.setSuperclass(DataType.class.getSimpleName() + "<" + parentClassName + ">");
				composer.addImplementedInterface(targetClassType.getSimpleSourceName());
				composer.addImport(ArrayList.class.getName());
				composer.addImport(List.class.getName());
				composer.addImport(ClassField.class.getName());
				composer.addImport(ClassTypeRegistry.class.getName());
				composer.addImport(DataField.class.getName());
				composer.addImport(DataType.class.getName());
				composer.addImport(targetClassName);

				SourceWriter sourceWriter = composer.createSourceWriter(context, printWriter);

				StringBuilder sb = new StringBuilder();
				sb.append("private List<ClassField> fields;\n");

				//
				// Build constructor.
				//

				sb.append("public " + generatedClassName + "() {\n");
				sb.append("  super(" + parentClassName + ".class);\n");
				sb.append("  fields = new ArrayList<ClassField>(" + properties.size() + ");\n");

				for (Entry<String, String> entry : properties.entrySet()) {
					sb.append("  fields.add(new DataField(\"" + entry.getKey() + "\", " + entry.getValue() + ".class));\n");
				}

				sb.append("  ClassTypeRegistry.add(this);\n");
				sb.append("}\n");

				//
				// Build getFields(..).
				//

				sb.append("@Override\n");
				sb.append("public List<ClassField> getFields() {\n");
				sb.append("  return fields;\n");
				sb.append("}\n");

				//
				// Build getProperty(..).
				//

				sb.append("@Override\n");
				sb.append("public Object getProperty(" + parentClassName + " obj, String prop) {\n");
				sb.append("  " + parentClassName + " rec = (" + parentClassName + ") obj;\n");

				for (Entry<String, String> entry : properties.entrySet()) {
					String propertyName = entry.getKey();
					String propertyGetter = getter(propertyName);

					sb.append("  if (prop.equals(\"" + propertyName + "\")) {\n");
					sb.append("    return rec." + propertyGetter + "();\n");
					sb.append("  }\n");
				}

				sb.append("  return null;\n");
				sb.append("}\n");

				//
				// Build setProperty(..).
				//

				sb.append("@Override\n");
				sb.append("public void setProperty(" + parentClassName + " obj, String prop, Object val) {\n");
				sb.append("  " + parentClassName + " rec = (" + parentClassName + ") obj;\n");

				for (Entry<String, String> entry : properties.entrySet()) {
					String propertyName = entry.getKey();
					String propertyType = entry.getValue();
					String propertySetter = setter(propertyName);

					sb.append("  if (prop.equals(\"" + propertyName + "\")) {\n");
					sb.append("    rec." + propertySetter + "((" + propertyType + ") val);\n");
					sb.append("  }\n");
				}

				sb.append("}\n");

				//
				// Build create(..).
				//

				sb.append("@Override\n");
				sb.append("public " + parentClassName + " create() {\n");
				sb.append("  return new " + parentClassName + "();\n");
				sb.append("}\n");

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

	private String getter(String propertyName) {
		return "get" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
	}

	private String setter(String propertyName) {
		return "set" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
	}
}
