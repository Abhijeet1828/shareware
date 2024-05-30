package com.custom.sharewise.dto;

import java.util.Comparator;

import org.apache.commons.lang3.compare.ComparableUtils;

public class PairMaxComparator implements Comparator<Pair> {

	@Override
	public int compare(Pair o1, Pair o2) {
		if (ComparableUtils.is(o1.netAmount()).lessThan(o2.netAmount()))
			return 1;
		else if (ComparableUtils.is(o1.netAmount()).greaterThan(o2.netAmount()))
			return -1;
		else
			return 0;
	}

}
