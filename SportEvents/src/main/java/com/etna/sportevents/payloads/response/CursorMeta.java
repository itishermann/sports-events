package com.etna.sportevents.payloads.response;

import org.springframework.data.domain.Page;

public class CursorMeta {
	private String cursor;

	private String previous;

	private String next;

	public CursorMeta() {
	}

	public CursorMeta(String cursor, String previous, String next) {
		this.cursor = cursor;
		this.previous = previous;
		this.next = next;
	}

	public <T> CursorMeta(Page<T> page, int cursor, int size) {
		String next = null;
		String previous = null;
		if(!page.hasContent() ) {
			this.cursor = String.valueOf(cursor);
			this.previous = previous;
			this.next = next;
			return;
		}
		if(page.getNumberOfElements() < size) {
			this.next = next;
		} else if (page.getNumberOfElements() >= size ) {
        // get last element
				T last = page.toList().get(page.getNumberOfElements()-1);
				// if T have an getId() method
				if (last.getClass().getDeclaredMethods().length > 0) {
						try {
								next = String.valueOf(last.getClass().getDeclaredMethod("getId").invoke(last));
						} catch (Exception ignored) {
						}
				}
    }
    if (cursor > 0) {
        // get first element id - size
				T first = page.getContent().get(0);
				// if T have an getId() method
				if (first.getClass().getDeclaredMethods().length > 0) {
						try {
								String p = String.valueOf(first.getClass().getDeclaredMethod("getId").invoke(first));
								if(p!=null) {
									previous = String.valueOf(Integer.parseInt(p) - size - 1);
								}
						} catch (Exception ignored) {
						}
				}
    }
		this.cursor = String.valueOf(cursor);
		this.previous = previous;
		this.next = next;
	}

	public String getCursor() {
		return cursor;
	}

	public void setCursor(String cursor) {
		this.cursor = cursor;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}
}
