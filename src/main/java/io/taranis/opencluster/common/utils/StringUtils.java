package io.taranis.opencluster.common.utils;

public class StringUtils {
	
	private StringUtils() {
		
	}

	public static final String EMPTY_STRING = "";
	
	public static boolean isNullOrEmpty(String str) {
		return (str == null || str.isEmpty());
	}
	
	public static String emptyIfNull(String str) {
		return str == null ? EMPTY_STRING : str;
	}
	
}
