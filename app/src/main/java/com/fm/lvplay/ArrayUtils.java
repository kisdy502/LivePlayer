package com.fm.lvplay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ArrayUtils {
	
	/**
	 * 获取map value的末位元素
	 *
	 * @param map
	 * @return
	 */
	public static <K, V> Entry<K, V> getTail(Map<K, V> map) {
		Iterator<Entry<K, V>> iterator = map.entrySet().iterator();
		Entry<K, V> tail = null;
		while (iterator.hasNext()) {
			tail = iterator.next();
		}
		return tail;
	}
	
	/**
	 * 判断数组是否为空
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(List<?> list) {
		return null == list || list.isEmpty();
	}
	
	/**
	 * 通过value获取key
	 * @param map
	 * @param value
	 * @return
	 */
	public static String getKey(Map<String, Integer> map, int value) {
		String key = null;
		for (String getKey : map.keySet()) {
			if (map.get(getKey) == value) {
				key = getKey;
			}
		}
		return key;
	}
	
	/**
	 * map置空
	 * @param map
	 */
	public static void mapToNull(Map<?, ?> map) {
		if (map != null) {
			map.clear();
			map = null;
		}
	}
	
	public static <T> List<T> getCloneData(ArrayList<T> list) {
		List<T> listCopy = new ArrayList<T>();
		if (list == null) return null;
		listCopy.addAll(list);
		return listCopy;
	}
	
	public static <T> Boolean isInRange(List<T> list, int index) {
		if (isEmpty(list) || index < 0) {
			return false;
		}
		return index < list.size();
	}

	public static int getValidIndex(int index, int size) {
		if (index < 0) {
			return 0;
		} else if (index >= size) {
			return size - 1;
		}
		return index;
	}

	public static int getCycleIndex(int index, int size) {
		if (index >= size) {
			index = 0;
		} else if (index < 0) {
			index = size - 1;
		}
		return index;
	}
	
}
