package com.huacheng.huiservers.view;

import java.util.Comparator;

import com.huacheng.huiservers.geren.bean.GroupMemberBean;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<GroupMemberBean> {

	public int compare(GroupMemberBean o1, GroupMemberBean o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
