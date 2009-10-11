/**
 * 
 */
package net.yaourtprod.stockchecker.downloader.abcbourse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Sauzedde
 * 
 */
public class LabelAndCodes {

	/** This class static logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(LabelAndCodes.class);

	private static final Pattern FORMATED_STRING_PATTERN = Pattern.compile(";");
	public static final CodeKind ISIN = new CodeKind("ISIN");
	public static final CodeKind SICOVAM = new CodeKind("SICOVAM");
	public static final CodeKind TICKER = new CodeKind("TICKER");

	private Code primaryCode = null;
	private String label = null;
	private Map<CodeKind, Code> otherCodes = null;

	public static LabelAndCodes getFromFormattedString(final String s) throws IllegalArgumentException {
		final String[] fields = FORMATED_STRING_PATTERN.split(s);
		// Checks for ISIN Name Sicovam ticker
		if (null != fields && 4 == fields.length) {
			final Code isin = new Code(ISIN, fields[0]);
			final LabelAndCodes lac = new LabelAndCodes(isin, fields[1]);
			try {
				final Code sicovam = new Code(SICOVAM, fields[2]);
				lac.putOtherCode(sicovam);
			} catch (final IllegalArgumentException iae) {
				LOGGER.debug("Discarding empty SICOVAM code.");
			}
			try {
				final Code ticker = new Code(TICKER, fields[3]);
				lac.putOtherCode(ticker);
			} catch (final IllegalArgumentException iae) {
				LOGGER.debug("Discarding empty TICKER code.");
			}
			return lac;
		} else {
			throw new IllegalArgumentException("The provided String is not well formatted. Expected format is : ISIN;Name;SICOVAM;TICKER");
		}
	}

	public LabelAndCodes(final Code primaryCode, final String label) {
		super();
		if (null != primaryCode && null != label && !label.isEmpty()) {
			this.primaryCode = primaryCode;
			this.label = label;
		} else {
			throw new IllegalArgumentException("A non null, non empty primary code and label are mandatory.");
		}
	}

	/**
	 * Gets the primaryCode as Code.
	 * 
	 * @return the primaryCode
	 */
	public Code getPrimaryCode() {
		return primaryCode;
	}

	/**
	 * Sets the primaryCode as Code.
	 * 
	 * @param primaryCode
	 *          the primaryCode to set
	 */
	public void setPrimaryCode(final Code primaryCode) {
		this.primaryCode = primaryCode;
	}

	/**
	 * Gets the label as String.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the label as String.
	 * 
	 * @param label
	 *          the label to set
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * Gets the otherCodes as Collection<Code>.
	 * 
	 * @return the otherCodes
	 */
	public Collection<Code> getOtherCodes() {
		if (null != otherCodes) {
			return otherCodes.values();
		} else {
			return null;
		}
	}

	/**
	 * Sets the otherCodes as Collection<Code>.
	 * 
	 * @param otherCodes
	 *          the otherCodes to set
	 */
	public void setOtherCodes(final Collection<Code> otherCodes) {
		if (null != otherCodes) {
			for (final Code c : otherCodes) {
				putOtherCode(c);
			}
		} else {
			this.otherCodes = null;
		}
	}

	public Code putOtherCode(final Code otherCode) {
		if (null == otherCodes) {
			otherCodes = new HashMap<CodeKind, Code>();
		}

		if (null != otherCode) {
			if (primaryCode.getKind().equals(otherCode.getKind())) {
				final Code oldCode = primaryCode;
				primaryCode = otherCode;
				return oldCode;
			} else {
				return otherCodes.put(otherCode.getKind(), otherCode);
			}
		} else {
			return null;
		}
	}

	public Code removeOtherCode(final Code otherCode) {
		if (null != otherCodes && null != otherCode) {
			return otherCodes.remove(otherCode.getKind());
		} else {
			return null;
		}
	}
}
