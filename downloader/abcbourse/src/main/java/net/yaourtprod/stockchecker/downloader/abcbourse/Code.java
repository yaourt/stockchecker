/**
 * 
 */
package net.yaourtprod.stockchecker.downloader.abcbourse;

/**
 * @author Thomas Sauzedde
 * 
 */
public class Code {
	private CodeKind kind = null;
	private String code = null;

	public Code(final CodeKind kind, final String code) throws IllegalArgumentException {
		super();
		if (null != kind && null != code && !code.isEmpty()) {
			this.kind = kind;
			this.code = code;
		} else {
			throw new IllegalArgumentException("A non null, non empty kind and code are mandatory.");
		}
	}

	/**
	 * Gets the kind as CodeKind.
	 * 
	 * @return the kind
	 */
	public CodeKind getKind() {
		return kind;
	}

	/**
	 * Sets the kind as CodeKind.
	 * 
	 * @param kind
	 *          the kind to set
	 */
	public void setKind(final CodeKind kind) {
		this.kind = kind;
	}

	/**
	 * Gets the code as String.
	 * 
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the code as String.
	 * 
	 * @param code
	 *          the code to set
	 */
	public void setCode(final String code) {
		this.code = code;
	}
}
