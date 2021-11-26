package com.example.onlineshop.enums;

public enum SearchOperation {

	EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, STARTS_WITH, ENDS_WITH, CONTAINS;

	private static final String[] SIMPLE_OPERATION_SET = { ":", "!", ">", "<", "~", ";" };

	public static SearchOperation getSimpleOperation(final char input) {
		switch (input) {
		case ':':
			return EQUALITY;
		case '!':
			return NEGATION;
		case '>':
			return GREATER_THAN;
		case '<':
			return LESS_THAN;
		case '~':
			return LIKE;
		case ';':
			return STARTS_WITH;
		default:
			return null;
		}
	}

	public static String[] getSimpleOpertaionSet() {
		return SIMPLE_OPERATION_SET;
	}
}
