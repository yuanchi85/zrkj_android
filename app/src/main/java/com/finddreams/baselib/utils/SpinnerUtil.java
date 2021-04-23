package com.finddreams.baselib.utils;

import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class SpinnerUtil {
	public static void setSpinnerItemSelectedByValue(Spinner spinner, String value) {

		SpinnerAdapter apsAdapter = spinner.getAdapter(); // 得到SpinnerAdapter对象

		int k = apsAdapter.getCount();

		for (int i = 0; i < k; i++) {

			if (value.equals(apsAdapter.getItem(i).toString())) {

				spinner.setSelection(i, true);// 默认选中项

				break;

			}

		}

	}
}
