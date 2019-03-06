/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2019 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.test.code;

import com.google.common.reflect.TypeToken;
import com.google.gson.internal.Primitives;

import junit.framework.TestCase;

import org.catrobat.catroid.content.Project;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class SerializedClassMissingDefaultConstructor extends TestCase {
	private final String rootPackage = "org.catrobat.catroid";
	private final Reflections reflections = new Reflections(rootPackage);
	private final Set<Class> handled = new HashSet<>();

	public void testSerializedClassMissingDefaultConstructor() {
		checkSerializedClassMissingDefaultConstructor(Project.class);
	}

	private void checkSerializedClassMissingDefaultConstructor(Class clazz) {
		if (handled.contains(clazz)) {
			return;
		}
		handled.add(clazz);

		if (clazz.isEnum()) {
//			System.out.println("  " + clazz.getName() + " ignored");
			return;
		} else if (clazz.getPackage() == null) {
			if (!clazz.getCanonicalName().startsWith(rootPackage)) {
//				System.out.println("  " + clazz.getName() + " ignored");
				return;
			}
		} else {
			if (!clazz.getPackage().getName().startsWith(rootPackage)) {
//				System.out.println("  " + clazz.getName() + " ignored");
				return;
			}
		}

		if (!clazz.isInterface()) {
			boolean missingConstructor = false;
			try {
				if (clazz.getConstructor() == null) { // getDeclaredConstructor if default constructor can also be private, package-private or protected.
					missingConstructor = true;
				}
			} catch (NoSuchMethodException e) {
				missingConstructor = true;
			}

			if (missingConstructor) {
				System.out.println(clazz.getName() + " is missing public default constructor");
			} else {
//				System.out.println(clazz.getName());
			}
		}

		if (clazz.isInterface()) {
			for (Type interfaze : clazz.getGenericInterfaces()) {
				checkSerializedClassMissingDefaultConstructor(TypeToken.of(interfaze).getRawType());
			}
		} else {
			checkSerializedClassMissingDefaultConstructor(clazz.getSuperclass());
		}

		if (clazz.getGenericSuperclass() instanceof ParameterizedType) {
			foo(clazz.getGenericSuperclass());
		}
		for (Class subClazz : (Set<Class>) reflections.getSubTypesOf(clazz)) {
			checkSerializedClassMissingDefaultConstructor(subClazz);
		}
		for (Field field : clazz.getDeclaredFields()) {
			Class fieldType = field.getType();
			Type fieldGenericType = field.getGenericType();

			if (Modifier.isTransient(field.getModifiers())
					|| Primitives.isPrimitive(fieldType) || Primitives.isWrapperType(fieldType) || fieldType.equals(String.class) || fieldType.isEnum()) {
				continue;
			}

			if (fieldGenericType instanceof ParameterizedType) {
				foo(fieldGenericType);
			}
			checkSerializedClassMissingDefaultConstructor(fieldType);
		}
	}

	private void foo(Type type) {
		for (Type p : ((ParameterizedType) type).getActualTypeArguments()) {
			checkSerializedClassMissingDefaultConstructor(TypeToken.of(p).getRawType());
			if (p instanceof ParameterizedType) {
				foo(p);
			}
		}
	}
}
