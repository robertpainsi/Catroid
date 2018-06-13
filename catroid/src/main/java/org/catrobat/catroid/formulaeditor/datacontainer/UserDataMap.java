/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2018 The Catrobat Team
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

package org.catrobat.catroid.formulaeditor.datacontainer;

import org.catrobat.catroid.formulaeditor.UserData;

import java.util.List;
import java.util.Map;

class UserDataMap<K, V extends UserData> {

	private Map<K, List<V>> keyDataMap;

	UserDataMap(Map<K, List<V>> keyDataMap) {
		this.keyDataMap = keyDataMap;
	}

	List<V> get(K key) {
		return keyDataMap.get(key);
	}

	V getUserData(K key, String name) {
		if (keyDataMap.containsKey(key)) {
			for (V item : keyDataMap.get(key)) {
				if (item.getName().equals(name)) {
					return item;
				}
			}
		}
		return null;
	}

	boolean containsKey(K key) {
		return keyDataMap.containsKey(key);
	}

	boolean containsInAnyKey(String name) {
		for (K key : keyDataMap.keySet()) {
			if (containsInKey(key, name)) {
				return true;
			}
		}
		return false;
	}

	boolean containsInKey(K key, String name) {
		return getUserData(key, name) != null;
	}
}
