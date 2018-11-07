package zkh.tool.list.test;

import java.util.ArrayList;
import java.util.List;

import zkh.tool.list.ListSortUtil;

public class Test {
	
	public static void main(String[] args) {		
		CourseTeacher ct = null;
		List<CourseTeacher> list = new ArrayList<CourseTeacher>();
		
		for (int i = 0; i < 100; i++) {
			ct = new CourseTeacher();
			ct.setDd(i+0l);
			list.add(ct);
		}
		
		// 排序
		String [] sortNameArr = {"dd", "note"};  
        boolean [] isAscArr = {true,true};  
        ListSortUtil.sort(list,sortNameArr,isAscArr);
		
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getDd());
		}
	}
}