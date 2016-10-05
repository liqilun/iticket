package com.iticket.service;

import java.util.List;

public interface BaseService {
	List queryByRowsRange(final String hql, final int from, final int maxrows, final Object...params);
	List queryByNameParams(final String hql, final int from, final int maxnum, final String paramnames, final Object... params);
}
