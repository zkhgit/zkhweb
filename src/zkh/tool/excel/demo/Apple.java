package zkh.tool.excel.demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zkh.tool.date.DateUtil;
import zkh.tool.excel.common.ExcelField;

/**
 * 实体Bean
 *
 * 赵凯浩
 * 2018年11月7日 下午4:19:10
 */
public class Apple {
	
	private int id;
	private String name; // 名称
	private Float price; // 单价
	private Double totalPrice; // 总价
	private Long number; // 数量
	private Date productionDate; // 出产日期
	private Boolean expire; // 是否过期
	
	public Apple() {
		super();
	}
	public Apple(int id, String name, Float price, Double totalPrice, Long number, Date productionDate,
			Boolean expire) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.totalPrice = totalPrice;
		this.number = number;
		this.productionDate = productionDate;
		this.expire = expire;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@ExcelField(column = 0, type = String.class, maxLength = 50, required = true)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Float getPrice() {
		return price;
	}
	
	public void setPrice(Float price) {
		this.price = price;
	}
	
	public Double getTotalPrice() {
		return totalPrice;
	}
	
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public Long getNumber() {
		return number;
	}
	
	public void setNumber(Long number) {
		this.number = number;
	}
	
	public Date getProductionDate() {
		return productionDate;
	}
	
	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}
	
	public Boolean getExpire() {
		return expire;
	}
	
	public void setExpire(Boolean expire) {
		this.expire = expire;
	}

	
	/**
	 * 组装对象list
	 * @return
	 */
	public static List<Apple> 组装List(){
		List<Apple> list = new ArrayList<Apple>();
		
		Apple apple = null;
		for (int i = 0; i < 3; i++) {
			apple =new Apple();
			apple.setId(i);
			apple.setName("富士" + 1 + "号");
			apple.setPrice(i + 0.1f);
			apple.setTotalPrice(i + 10.01d);
			apple.setNumber(i + 3l);
			apple.setProductionDate(DateUtil.getAppointDate("DATE", i));
			apple.setExpire(true);
			
			list.add(apple);
		}
		
		for (int i = 0; i < 3; i++) {
			apple =new Apple();
			apple.setId(i + 3);
			apple.setName("秦冠" + 1 + "号");
			apple.setPrice(i + 0.2f);
			apple.setTotalPrice(i + 5.01d);
			apple.setNumber(i + 5l);
			apple.setProductionDate(DateUtil.getAppointDate("HOUR", i));
			apple.setExpire(false);
			
			list.add(apple);
		}

		for (int i = 0; i < 100000; i++) {
			apple =new Apple();
			apple.setId(i + 6);
			apple.setName("蛇果" + 1 + "号");
			apple.setPrice(i + 0.3f);
			apple.setTotalPrice(i + 15.01d);
			apple.setNumber(i + 10l);
			apple.setProductionDate(DateUtil.getAppointDate("HOUR", 10*i));
			apple.setExpire(true);
			
			list.add(apple);
		}
		
		return list;
	}
}
