package io.github.lr.querythreads.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.concurrent.TimeUnit;

/**
 * Utilitario con funciones comunes.
 * 
 * @author lravanal
 *
 */
public class UtilsHelper {
	
	private static final String ZERO = "0";
	private static final String SPACE = " ";

	public static final String DATE_PATTERN = "dd/MM/yyyy";
	public static final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";

	public static String convertDuration(long millis) {
		if (millis <= 0) {
			return ZERO;
		}

		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
		millis -= TimeUnit.SECONDS.toMillis(seconds);

		StringBuilder result = new StringBuilder();
		if (days > 0) {
			result.append(SPACE);
			result.append(days);
			result.append(SPACE);
			result.append(days == 1 ? "día" : "días");
		}
		if (hours > 0) {
			result.append(SPACE);
			result.append(hours);
			result.append(SPACE);
			result.append(hours == 1 ? "hora" : "horas");
		}
		if (minutes > 0) {
			result.append(SPACE);
			result.append(minutes);
			result.append(SPACE);
			result.append(minutes == 1 ? "minuto" : "minutos");
		}
		if (seconds > 0) {
			result.append(SPACE);
			result.append(seconds);
			result.append(SPACE);
			result.append(seconds == 1 ? "segundo" : "segundos");
		}
		if (days == 0 && hours == 0 && minutes == 0 && millis > 0) {
			result.append(SPACE);
			result.append(millis);
			result.append(SPACE);
			result.append(millis == 1 ? "milisegundo" : "milisegundos");
		}

		return result.toString();
	}

	private static DecimalFormatSymbols symbols;

	static {
		symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator('.');
	}

	public static String convertFileSize(long size) {
		if (size <= 0) {
			return ZERO;
		}

		final String[] units = new String[] { "Bytes", "kBytes", "MBytes", "GBytes", "TBytes" };

		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

		return new DecimalFormat("#,##0.#", symbols).format(size / Math.pow(1024, digitGroups)) + SPACE + units[digitGroups];
	}
}
