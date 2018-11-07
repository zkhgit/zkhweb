package zkh.tool.Timer;

import java.io.File;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时递归删除指定文件夹
 *
 * 赵凯浩
 * 2018年9月25日 下午3:09:16
 */
public class DeleteFolder extends java.util.TimerTask{
	
	private Logger logger = LoggerFactory.getLogger(DeleteFolder.class);
	private static DeleteFolder deleteFolder;
	private String path;
	private Timer timer;
	
	public static DeleteFolder getInstance(String path, Timer timer){
        if (deleteFolder == null) {
        	deleteFolder = new DeleteFolder(path, timer);
        }
        return deleteFolder;
    }
	
	/**
	 * @param path 要删除的文件夹路径
	 * @param timer 用于终止当前定时器
	 */
	private DeleteFolder(String path, Timer timer) {
		this.path = path;
		this.timer = timer;
	}
	
	@Override
	public void run() {
		try {			
			File file = new File(path);
			delteFile(file);
			logger.info("删除文件夹成功：" + path);
		} catch (Exception e) {
			logger.error("删除文件夹失败：" + path);
		}
		 this.timer.cancel();
	}
	
	/**
	 * 递归删除文件及文件夹
	 * @param file
	 */
	private void delteFile(File file) throws Exception{
	   File[] filearray = file.listFiles();
	   if(filearray!=null){
		   for(File f:filearray){
			   if(f.isDirectory()){
				   delteFile(f);   
			   }else{
				   f.delete();
			   }
		   }
		   file.delete();
	   }
	}
	
	public static void main(String[] args) {
		Timer timer = new Timer();
		timer.schedule(DeleteFolder.getInstance("G:\\tempfile\\2018\\9\\17", timer), 10000); // 单位：毫秒
	}
}
