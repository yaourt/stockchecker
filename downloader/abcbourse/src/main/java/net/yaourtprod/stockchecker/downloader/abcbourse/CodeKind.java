/**
 * 
 */
package net.yaourtprod.stockchecker.downloader.abcbourse;

/**
 * @author Thomas Sauzedde
 * 
 */
public class CodeKind {
	private String kind = null;

	public CodeKind(final String kind) throws IllegalArgumentException {
		super();
		if (null != kind && !kind.isEmpty()) {
			this.kind = kind;
		} else {
			throw new IllegalArgumentException("A non null, non empty kind is mandatory.");
		}
	}

	/**
	 * Gets the kind as String.
	 * 
	 * @return the kind
	 */
	public String getKind() {
		return kind;
	}

	@Override
	public String toString() {
		return kind;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass()) {
			if (obj.getClass() == String.class && null != kind) {
				return kind.equalsIgnoreCase((String) obj);
			} else {
				return false;
			}
		}

		final CodeKind other = (CodeKind) obj;
		if (kind == null) {
			if (other.kind != null) {
				return false;
			}
		}

		return kind.equalsIgnoreCase(other.kind);
	}

}
