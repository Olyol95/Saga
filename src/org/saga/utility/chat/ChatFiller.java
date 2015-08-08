package org.saga.utility.chat;

import java.util.*;

import org.bukkit.ChatColor;
import org.saga.messages.colours.Colour.CustomColour;

public class ChatFiller {

	/**
	 * Default character length.
	 */
	public final static Double DEFAULT_LENGTH = 1.0;

	/**
	 * Maximum character length.
	 */
	public final static Double MAX_LENGTH = 2.0;

	/**
	 * Gap fill string maximum size.
	 */
	private final static Double MAX_GAP = 2.0/3.0;

	/**
	 * Chat width.
	 */
	public final static Double CHAT_WIDTH = 53.0;

	/**
	 * Size map.
	 */
	private final static HashMap<Character, Double> SIZE_MAP = new HashMap<Character, Double>() {

		/**
		 * Serial version UID.
		 */
		private static final long serialVersionUID = 1L;

		{
			put('f', 5.0 / 6.0);
			put('i', 1.0 / 3.0);
			put('k', 5.0 / 6.0);
			put('l', 1.0 / 2.0);
			put('t', 4.0 / 6.0);
			put('I', 2.0 / 3.0);
			put('(', 5.0 / 6.0);
			put(')', 5.0 / 6.0);
			put('<', 5.0 / 6.0);
			put('>', 5.0 / 6.0);
			put('{', 5.0 / 6.0);
			put('}', 5.0 / 6.0);
			put(',', 1.0 / 3.0);
			put('.', 1.0 / 3.0);
			put('[', 4.0 / 6.0);
			put(']', 4.0 / 6.0);
			put('|', 2.0 / 3.0);
			put('*', 5.0 / 6.0);
			put('"', 5.0 / 6.0);
			put('!', 1.0 / 3.0);
			put(':', 1.0 / 3.0);
			put('\'', 1.0 / 2.0);
			put('`', 0.5);

			put('\u278A', 4.0 / 3.0);
			put('\u278B', 4.0 / 3.0);
			put(' ', 2.0 / 3.0);
			put('\u278C', 4.0 / 3.0);

			put('\u2500', 1.5);
			put('\u2502', 1.0);
			put('\u250C', 1.5);
			put('\u2510', 1.0);
			put('\u2514', 1.5);
			put('\u2518', 1.5);

			put('\u2550', 1.5);
			put('\u2551', 1.5);

			put('\u2554', 1.5);
			put('\u2560', 1.5);
			put('\u255A', 1.5);

			put('\u2557', 1.5);
			put('\u2563', 1.5);
			put('\u255D', 1.5);

			put('\u2591', 1.5);
			put('\u2592', 1.5);
			put('\u2593', 1.5);

			put('\u278A', 0.5);
			put('\u278B', 4.0 / 6.0);
			put('\u278C', 13.0 / 15.0);
			put('\u0000', 1.0 / 3.0);

			put('\u2B24', 8.0 / 6.0);

			put(CustomColour.PREVIOUS_COLOR.getChar(), 0.0);
			put(CustomColour.NORMAL_FORMAT.getChar(), 0.0);
			put(ChatColor.COLOR_CHAR, 0.0);

		}
	};

	/**
	 * Gap fill chars.
	 */
	private final static HashSet<Character> FILL_CHARS = new HashSet<Character>() {
		private static final long serialVersionUID = 1L;
		{
			add('\u278A');
			add('\u278B');
			add('\u278C');
			add('\u0000');
		}
	};

	/**
	 * Fills a string.
	 * 
	 * @param str
	 *            string to fill
	 * @param reqLength
	 *            required length
	 * @return string with the given length
	 */
	public static String fillString(String str, Double reqLength) {

		char[] chars = str.toCharArray();

		StringBuilder result = new StringBuilder();
		Double length = 0.0;

		// Cut size:
		for (int i = 0; i < chars.length; i++) {

			Double charLength = SIZE_MAP.get(chars[i]);
			if (charLength == null)
				charLength = DEFAULT_LENGTH;

			if (length + charLength > reqLength)
				break;

			result.append(chars[i]);

			if (!(chars[i] == ChatColor.COLOR_CHAR || (i > 0 && chars[i - 1] == ChatColor.COLOR_CHAR)))
				length += charLength;

		}

		// Add spaces:
		String fillChar = " ";
		Double fillLength = 2.0/3.0;
		while (true) {

			Double gapLength = reqLength - length;

			// Gap filled:
			if (gapLength <= 0)
				break;

			// Add custom fillers:
			if (gapLength < fillLength) {

				fillChar = findCustom(gapLength);
				if (fillChar != null) {
					result.append(fillChar);
				}

				break;

			}

			result.append(fillChar);
			length += fillLength;

		}

		return result.toString();

	}

	/**
	 * Finds a custom character with the best fit.
	 * 
	 * @param gapLen
	 *            gap length
	 * @return char that best fits the gap, null if none
	 */
	private static String findCustom(Double gapLen) {

		gapLen+=(1.0/3.0)+0.15;

		Set<Character> gapStrs = new HashSet<>(FILL_CHARS);
		String bestFiller = "";
		Double bestFitLen = 100.0;

		for (Character gapStr : gapStrs) {

			Double strLen = 0.0;
			String filler = "";

			while(strLen <= gapLen - SIZE_MAP.get(gapStr)) {

				filler += gapStr;
				strLen += SIZE_MAP.get(gapStr);

			}

			if (gapLen - strLen < bestFitLen) {

				bestFitLen = gapLen - strLen;
				bestFiller = filler;

			}

		}

		if (bestFiller.equals("")) {
			return null;
		} else {
			return bestFiller;
		}

	}

	/**
	 * Calculates the length of a string.
	 * 
	 * @param str
	 *            string
	 * @return string length
	 */
	public static Double calcLength(String str) {

		char[] chars = str.toCharArray();

		Double length = 0.0;

		for (int i = 0; i < chars.length; i++) {

			Double charLength = SIZE_MAP.get(chars[i]);
			if (charLength == null)
				charLength = DEFAULT_LENGTH;

			if (!(chars[i] == ChatColor.COLOR_CHAR || (i > 0 && chars[i - 1] == ChatColor.COLOR_CHAR)))
				length += charLength;

		}

		return length;

	}

	/**
	 * Adjusts filler characters.
	 * 
	 * @param str
	 *            string
	 * @return adjusted string
	 */
	public static String adjustFillers(String str) {

		// str = str.replace("\u278A", ChatColor.DARK_GRAY + "`");
		// str = str.replace("\u278B", ChatColor.DARK_GRAY + "\'");
		// str = str.replace("\u278C", ChatColor.DARK_GRAY + "\"");

		str = str.replace("\u278A", ChatColor.DARK_GRAY + "`");
		str = str.replace("\u278B", ChatColor.DARK_GRAY + "" + ChatColor.BOLD
				+ "`" + CustomColour.NORMAL_FORMAT);
		str = str.replace("\u278C", ChatColor.DARK_GRAY + "" + ChatColor.BOLD
				+ " " + CustomColour.NORMAL_FORMAT);
		str = str.replace("\u0000", ChatColor.DARK_GRAY + ".");

		return str;

	}

}
