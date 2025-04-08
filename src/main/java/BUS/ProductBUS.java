package BUS;

import DAO.ProductDAO;
import DTO.ProductDTO;
import java.util.List;
import javax.swing.JOptionPane;

public class ProductBUS {

    public List<ProductDTO> getAllDetailImport() {
        return ProductDAO.getAllProducts();
    }

    public void updateDetailImport(ProductDTO product) {
        ProductDAO dao = new ProductDAO();
        dao.updateProduct(product);
    }

    public boolean validateProduct(ProductDTO product) {
        String productName = product.getProductName().trim();
        String gia = product.getGia().trim();

        // Không cho tên chỉ toàn số
        if (productName.matches("\\d+")) {
            JOptionPane.showMessageDialog(null,
                    "Tên sản phẩm không được chỉ chứa số.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Không chứa ký tự đặc biệt (chỉ cho phép chữ cái, số và khoảng trắng)
        if (!productName.matches("^[\\p{L}0-9\\s+\\-\\.]+$")) {
            JOptionPane.showMessageDialog(null,
                    "Tên sản phẩm không được chứa ký tự đặc biệt.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!gia.matches("^\\d+$")) {
            JOptionPane.showMessageDialog(null,
                    "Giá chỉ được chứa số.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        List<ProductDTO> productList = ProductDAO.getAllProducts();
        for (ProductDTO p : productList) {
            if (p.getProductName().trim().equalsIgnoreCase(productName)
                    && (product.getProductID() == null || !product.getProductID().equals(p.getProductID()))) {
                JOptionPane.showMessageDialog(null,
                        "Tên sản phẩm đã tồn tại!",
                        "Lỗi nhập liệu",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }
}
