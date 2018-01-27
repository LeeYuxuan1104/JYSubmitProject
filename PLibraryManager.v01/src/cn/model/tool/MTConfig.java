package cn.model.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

@SuppressWarnings("deprecation")
public class MTConfig {
	//	数据库交互的工具类;
	private MTDataBaseTool mtDataBaseTool;
	public MTConfig() {
		mtDataBaseTool=new MTDataBaseTool();
		//初始化表单
		checkTableStruction();
	}
	//	上传图片的类;
	@SuppressWarnings({"rawtypes" })
	public String uploadMap(HttpServletRequest req,String kind,String folder){
		File file=null;
		//	临时目录;
		String 		pathTemp = req.getSession().getServletContext().getRealPath("/")+ "temp"; 
		//	文件检测;
		file 				 = new File(pathTemp);
		if (!file.exists()) {
			file.mkdirs();
		}
		//  正式目录;
		String 		pathTrue =req.getSession().getServletContext().getRealPath("/")+ "photo"; 
		//	文件检测;
		file 				 = new File(pathTrue);
		if (!file.exists()) {
			file.mkdirs();
		}
		//	Disk上传的内容;
		DiskFileUpload fu 	 = new DiskFileUpload();

		fu.setSizeMax(1 * 1024 * 1024); // 设置允许用户上传文件大小,单位:字节
		fu.setSizeThreshold(4096); 		// 设置最多只允许在内存中存储的数据,单位:字节
		fu.setRepositoryPath(pathTemp); // 设置一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录

		// 开始读取上传信息
		List 	fileItems  	 = null;

		try {
			fileItems = fu.parseRequest(req);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Iterator 	  iter  = fileItems.iterator();   // 依次处理每个上传的文件
		while (iter.hasNext()){

			FileItem  item  = (FileItem) iter.next(); // 忽略其他不是文件域的所有表单信息
			
			if (!item.isFormField()){

				String name = item.getName();		  // 获取上传文件名,包括路径
				
				pathTrue=pathTrue+File.separator+kind+File.separator+folder;
					
				file=new File(pathTrue);
				if(!file.exists()){
					file.mkdirs();
				}
				long   size = item.getSize();
				if ((name == null || name.equals("")) && size == 0)
					continue;
				file 	    = new File(pathTrue, name+".jpg");
				try {
					item.write(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "success";
			}
		}
		return "fail";
	}
	
	/*进行数据表单的检测*/
	public void checkTableStruction(){
		String sql="select * from user_info";
		ArrayList<String[]> tables= mtDataBaseTool.query(sql);
		if(tables==null){
			ArrayList<String> list=new ArrayList<>();
			list.add(table_user_info);
			list.add(table_kind_book_info);
			list.add(table_item_book_info);
			list.add(table_borrow_info);
			list.add(table_book_state_info);
			for(String item:list){
				mtDataBaseTool.doDBUpdate(item);
			}
		}
	}
	
	/*初始化表单结构*/
	// 用户信息表;
	private String table_user_info=
			"create table user_info (" +
			"id Integer primary key auto_increment," +
			"uid varchar(32) not null," +
			"uname varchar(32) not null," +
			"upwd varchar(32) not null," +
			"urole varchar(32) not null," +
			"note varchar(500)," +
			"img varchar(500) ," +
			"phone varchar(32) not null," +
			"email varchar(32) not null" +
			")";
	//	书大类信息;
	private String table_kind_book_info=
			"create table kind_book_info (" +
			"id integer primary key auto_increment," +
			"kid varchar(32) not null," +
			"kname varchar(32) not null," +
			"note varchar(500))";
	//	书小类信息;
	private String table_item_book_info=
			"create table item_book_info (" +
			"id integer primary key auto_increment," +
			"iid varchar(32) not null," +
			"iname varchar(32) not null," +
			"note varchar(500)," +
			"author varchar(32) not null," +
			"press varchar(100) not null," +
			"ptime varchar(100) not null," +
			"count varchar(100) not null," +
			"kid varchar(32) not null," +
			"img varchar(100)" +
			")";
	//	书借阅信息;
	private String table_borrow_info=
			"create table borrow_info (" +
			"id integer primary key auto_increment," +
			"bid varchar(32) not null," +
			"iid varchar(32) not null," +
			"iname varchar(32) not null," +
			"borrower varchar(32) not null," +
			"btime varchar(32) not null," +
			"deadline varchar(32) not null," +
			"state varchar(32) not null," +
			"outstate varchar(32) not null," +
			"instate varchar(32) not null," +
			"inimg varchar(1000) " +
			")";
	//	借书状态信息;
	private String table_book_state_info=
			"create table book_state_info (" +
			"id integer primary key auto_increment," +
			"iid varchar(32) not null," +
			"count varchar(32) not null," +
			"ccount varchar(32) not null," +
			"ebids varchar(1000))";
	///////////////////////////////////////////////////
	
	
}
