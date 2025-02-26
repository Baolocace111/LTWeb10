package service;

import java.util.ArrayList;
import java.util.List;

import dao.DaoProduct;
import entity.Category;
import entity.Image;
import entity.Product;
import entity.Subcategory;

public class ProductService {
	DaoProduct daoProduct = new DaoProduct();

	public List<Product> getRecentProduct() {
    	List<Product> list = new ArrayList<>();
    	String HQL = "From Product P Where P.status = 1 ORDER BY P.id_P DESC";
    	list = daoProduct.getRecentProduct(HQL, 4);
    	return list;
	}
	
	public List<Product> getTopProduct() {
		// group by how many product in bill
		String HQL = "SELECT P FROM Product P, Billdetail B WHERE P.id_P = B.product.id_P GROUP BY P.id_P ORDER BY COUNT(P.id_P) DESC";
		return daoProduct.getTopProduct(HQL, 8);
	}
	
	public List<Product> getAllProduct()
	{
		List<Product> list = new ArrayList<>();
		String HQL = "From Product P Where P.status = 1";
		list = daoProduct.getAllProduct(HQL);
		return list;
	}
	
	public List<Product> pagingAccount(String subcateID, int index, int show)
	{
		List<Product> list = new ArrayList<>();
		String HQL = "";
		if(subcateID.equals("-1")) {
    		HQL = "From Product P Where P.status = 1 Order By P.id_P ASC";
    	}
    	else {
    		HQL = "From Product P Where P.subcategory=" + subcateID +" and P.status = 1 Order By P.id_P ASC";
    	}
		//System.out.println(HQL);
		list = daoProduct.pagingAccount(HQL, (index-1)*show, show);
		return list;
	}
	
	public List<Product> searchByName(String txtSearch) {
        List<Product> list = new ArrayList<>();
        String HQL = "From Product P Where P.name_P like '%" + txtSearch + "%'";
        //System.out.println("\n"+ HQL +"\n");
        list = daoProduct.searchByName(HQL, '%' + txtSearch + '%');
        return list;
    }
	
	public int getCountAccount(String subCateID) {
		String HQL = "";
		if(subCateID.equals("-1")) {
			HQL = "select count(p) from Product p where p.status = 1";
		}
		else {
			HQL = "select count(p) from Product p Where p.subcategory=" + subCateID + "and where p.status=1";
		}
		return daoProduct.getCountProduct(HQL);
	}
	public int getCountProduct() {
		String HQL = "select count(id_P) from Product";	
		return daoProduct.getCountProduct(HQL);
	}
	public int getCountQuery(String query) {
		String HQL = "select count(P)" + query;
		return daoProduct.getCountQuery(HQL);
	}

	
	public List<Product> getProductAjax(String price1, String price2, String price3, String color1, String color2, String color3, 
			String subcateID, String index, int showP, String[] queryCount) {
		String fillTotal = "";

		// Fill Price
		String fillPrice = "";
		if(price1.equals("true")) {
			fillPrice += "P.price < 300000 ";
		}

		if(price2.equals("true")) {
			if(price1.equals("true")) {
				fillPrice += "OR ";
			}
			fillPrice += "(P.price >= 300000 and P.price < 800000) ";
		}
		if(price3.equals("true"))
		{
			if(price1.equals("true") || price2.equals("true")) {
				fillPrice += "OR ";
			}
			fillPrice += "P.price >= 800000 ";
		}
		// Fill Color
		String fillColor = "";
		if(color1.equals("true")) {
			fillColor += "P.color='Black' ";
		}

		if(color2.equals("true")) {
			if(color1.equals("true")) {
				fillColor += "Or ";
			}
			fillColor += "P.color='White' ";
		}
		if(color3.equals("true"))
		{
			if(color1.equals("true") || color2.equals("true")) {
				fillColor += "Or ";
			}
			fillColor += "P.color='Blue' ";
		}
		// Fill Total
		if(!fillPrice.equals("")) { // Khac rong
			fillTotal += "((" + fillPrice + ") ";
			if(!fillColor.equals("")) {
				fillTotal += "And (" + fillColor + ") ";
			}
			fillTotal += ") ";
		}
		else {
			if(!fillColor.equals("")) {
				fillTotal += "(" + fillColor + ") ";
			}
		}
		
		// Fill Catagory
		String fillCategory;
		String HQL = "";
		if(subcateID.equals("-1")) {
			//fillCategory = "";
			if(fillTotal == "") {
				HQL = "From Product P Where P.status = 1 Order By P.id_P ASC";
			}
			else {
				HQL = "From Product P Where P.status = 1 and" + fillTotal + " Order By P.id_P ASC";
			}
		}
		else {
			if(fillTotal == "")
			{
				HQL = "From Product P Where (P.subcategory=" + subcateID + ") and P.status = 1 Order By P.id_P ASC";
			}
			else {
				HQL = "From Product P Where (P.subcategory=" + subcateID + ") And " + fillTotal +"and P.status = 1 Order By P.id_P ASC";
			}
			//fillCategory = "id_subCate=? AND";
			
		}
		//System.out.println("\n HQL Nè:"+ HQL + "\n");
		//System.out.println((Integer.parseInt(index)-1)*showP).toString() + " " + showP);
		List<Product> list = new ArrayList<>();
		list = daoProduct.getProductAjax(HQL, ((Integer.parseInt(index)-1)*showP), showP);
		queryCount[0] = HQL.substring(0, HQL.toString().indexOf("Order"));
		//System.out.println(queryCount[0]);
		
		return list;
	}
	public void InsertProduct(Product product, Image img) {
		daoProduct.insertProduct(product, img);
	}
	public void UpdateProduct(Product product, Image img) {
		daoProduct.updateProduct(product, img);
	}
	public void DeleteProduct(String id_P) {
		String HQL = "Update Product P Set P.status = 0 Where P.id_P =" + id_P;
		daoProduct.deleteProduct(HQL);
	}
	public static void main (String[] args) {
	}
}
