package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.CategoryService;
import service.ProductService;
import entity.Product;
import entity.Subcategory;
import entity.Category;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {
    // get all category
    protected List<Subcategory> getCategory() {
        CategoryService categoryService = new CategoryService();
        List<Subcategory> list = categoryService.getAllSubCategory();
        return list;
    }

    protected List<Product> getRecentProduct() {
        ProductService productService = new ProductService();
        List<Product> list = productService.getRecentProduct();
        return list;
    }
    
    protected HashMap<Integer, Integer> countProduct(List<Subcategory> list) {
        CategoryService categoryService = new CategoryService();
        HashMap<Integer, Integer> map = new HashMap<>();
        for (Subcategory subcategory : list) {
            int subcategoryId = subcategory.getId_SubCate();
            int count = categoryService.countProduct(subcategoryId);
            map.put(subcategoryId, count);
        }
        return map;
    }


	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
        
        // ProductService pService = new ProductService();
        // // Get 4 Recent Product
        // List<Product> list = pService.getRecentProduct();
    
        List<Subcategory> categories = getCategory();
        List<Product> recentProducts = getRecentProduct();
        HashMap<Integer, Integer> countProduct = countProduct(categories);

        HttpSession session = request.getSession();
        session.setAttribute("allCategories", categories);
        session.setAttribute("recentProducts", recentProducts);
        session.setAttribute("countProduct", countProduct);
        
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
