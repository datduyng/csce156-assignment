package product;

import java.util.HashMap;

public abstract class Product {

	private String productCode;
	private String productType; 
	
	public Product(Product product) {
		this.productCode = product.getProductCode();
		this.productType = product.getProductType();
	}
	public Product(){
		//default;
	}
	
	public Product(String productCode, String productType) {
		setProductCode(productCode);
		setProductType(productType);
	}

	public String getProductCode() {
		return productCode;
	}
	
	public String getProductType() {
		return productType;
	}


	
	public void setProductCode(String productCode) {
		if(productCode != null && productCode.length() > 0) {
			this.productCode = productCode;
		}else {
			this.productCode = "na";
		}
		
	}
	
	public void setProductType(String productType) {
		if(productType != null && productType.length() > 0) {
			this.productType = productType;
		}else {
			this.productType = "na";
		}
	}
	
	public abstract double calculateSubTotal(int quantity, String invoiceDate, HashMap<Product,Integer> productList);

	/**
	 * @Override
	 */
	public String toString() {
		return this.productCode + ";" + this.productType;
	}
	
	
}