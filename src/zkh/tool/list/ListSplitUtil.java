package zkh.tool.list;

import java.util.ArrayList;
import java.util.List;

/**
 * 拆分
 * 描述：按指定大小拆分List
 *
 * 赵凯浩
 * 2018年11月8日 下午5:27:58
 */
public class ListSplitUtil<T> {
	
	public static <T> List<List<T>> split(List<T> resList,int count){
        
        if(resList==null ||count<1)
            return  null ;
        List<List<T>> ret=new ArrayList<List<T>>();
        int size=resList.size();
        if(size<=count){ //数据量不足count指定的大小
            ret.add(resList);
        }else{
            int pre=size/count;
            int last=size%count;
            //前面pre个集合，每个大小都是count个元素
            for(int i=0;i<pre;i++){
                List<T> itemList=new ArrayList<T>();
                for(int j=0;j<count;j++){
                    itemList.add(resList.get(i*count+j));
                }
                ret.add(itemList);
            }
            //last的进行处理
            if(last>0){
                List<T> itemList=new ArrayList<T>();
                for(int i=0;i<last;i++){
                    itemList.add(resList.get(pre*count+i));
                }
                ret.add(itemList);
            }
        }
        return ret;
        
    }

}
