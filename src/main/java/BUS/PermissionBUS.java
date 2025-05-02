package BUS;

import DTO.PermissionDTO;
import DAO.PermissionDAO;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PermissionBUS {

    public static List<String> getModule(List<String> functionCodes) {
        List<String> suffixes = new ArrayList<>();

        for (String code : functionCodes) {
            String suffix = code.substring(code.indexOf('_') + 1);
            suffixes.add(suffix);
        }

        return suffixes;
    }

    public static List<String> convertName(List<String> functionCodes) {
        List<String> converted = new ArrayList<>();

        for (String code : functionCodes) {
            converted.add(convertNumberCodeToFunctionCode(code));
        }
        return converted;
    }

    public static String decodeFunctionName(String functionCode) {
        return switch (functionCode) {
            // Sản phẩm
            case "CN001", "xem_sp" ->
                "Xem sản phẩm";
            case "CN002", "sua_sp" ->
                "Sửa sản phẩm";

            // Hóa đơn
            case "CN003", "xem_hd" ->
                "Xem hóa đơn";
            case "CN004", "them_hd" ->
                "Thêm hóa đơn";
            case "CN005", "sua_hd" ->
                "Sửa hóa đơn";
            case "CN006", "xoa_hd" ->
                "Xóa hóa đơn";

            // Nhân viên
            case "CN007", "xem_nv" ->
                "Xem nhân viên";
            case "CN008", "them_nv" ->
                "Thêm nhân viên";
            case "CN009", "sua_nv" ->
                "Sửa nhân viên";
            case "CN010", "xoa_nv" ->
                "Xóa nhân viên";

            // Nhà cung cấp
            case "CN011", "xem_ncc" ->
                "Xem nhà cung cấp";
            case "CN012", "them_ncc" ->
                "Thêm nhà cung cấp";
            case "CN013", "sua_ncc" ->
                "Sửa nhà cung cấp";
            case "CN014", "xoa_ncc" ->
                "Xóa nhà cung cấp";

            // Hóa đơn nhập
            case "CN015", "xem_hdn" ->
                "Xem hóa đơn nhập";
            case "CN016", "them_hdn" ->
                "Thêm hóa đơn nhập";
            case "CN017", "xoa_hdn" ->
                "Xóa hóa đơn nhập";

            // Khách hàng
            case "CN018", "xem_kh" ->
                "Xem khách hàng";
            case "CN019", "them_kh" ->
                "Thêm khách hàng";
            case "CN020", "sua_kh" ->
                "Sửa khách hàng";
            case "CN021", "xoa_kh" ->
                "Xóa khách hàng";

            // Tài khoản
            case "CN022", "xem_tk" ->
                "Xem tài khoản";
            case "CN023", "them_tk" ->
                "Thêm tài khoản";
            case "CN024", "sua_tk" ->
                "Sửa tài khoản";
            case "CN025", "xoa_tk" ->
                "Xóa tài khoản";

            // Bảo hành
            case "CN026", "xem_bh" ->
                "Xem bảo hành";
            case "CN027", "sua_bh" ->
                "Sửa bảo hành";

            // Quyền
            case "CN028", "xem_quyen" ->
                "Xem quyền";
            case "CN029", "them_quyen" ->
                "Thêm quyền";
            case "CN030", "sua_quyen" ->
                "Sửa quyền";
            case "CN031", "xoa_quyen" ->
                "Xóa quyền";

            // Thống kê
            case "CN032", "xem_thongke" ->
                "Xem thống kê";

            default ->
                functionCode; // Trả về nguyên bản nếu không khớp
        };
    }

    public static String convertNumberCodeToFunctionCode(String numberCode) {
        return switch (numberCode) {
            // Sản phẩm
            case "CN001" ->
                "xem_sp";
            case "CN002" ->
                "sua_sp";

            // Hóa đơn
            case "CN003" ->
                "xem_hd";
            case "CN004" ->
                "them_hd";
            case "CN005" ->
                "sua_hd";
            case "CN006" ->
                "xoa_hd";

            // Nhân viên
            case "CN007" ->
                "xem_nv";
            case "CN008" ->
                "them_nv";
            case "CN009" ->
                "sua_nv";
            case "CN010" ->
                "xoa_nv";

            // Nhà cung cấp
            case "CN011" ->
                "xem_ncc";
            case "CN012" ->
                "them_ncc";
            case "CN013" ->
                "sua_ncc";
            case "CN014" ->
                "xoa_ncc";

            // Hóa đơn nhập
            case "CN015" ->
                "xem_hdn";
            case "CN016" ->
                "them_hdn";
            case "CN017" ->
                "xoa_hdn";

            // Khách hàng
            case "CN018" ->
                "xem_kh";
            case "CN019" ->
                "them_kh";
            case "CN020" ->
                "sua_kh";
            case "CN021" ->
                "xoa_kh";

            // Tài khoản
            case "CN022" ->
                "xem_tk";
            case "CN023" ->
                "them_tk";
            case "CN024" ->
                "sua_tk";
            case "CN025" ->
                "xoa_tk";

            // Bảo hành
            case "CN026" ->
                "xem_bh";
            case "CN027" ->
                "sua_bh";

            // Quyền
            case "CN028" ->
                "xem_quyen";
            case "CN029" ->
                "them_quyen";
            case "CN030" ->
                "sua_quyen";
            case "CN031" ->
                "xoa_quyen";

            // Thống kê
            case "CN032" ->
                "xem_thongke";

            default ->
                numberCode.toLowerCase(); // Trả về dạng lowercase nếu không khớp
        };
    }

    public static List<PermissionDTO> searchPermission(String keyword) {
        return PermissionDAO.searchPermission(keyword);
    }

    public static boolean validatePermission(PermissionDTO permission) {
        String name = permission.getName();

        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Tên quyền không được để trống.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int slChucNang = Integer.parseInt(permission.getSlChucNang());
        if (slChucNang <= 0) {
            JOptionPane.showMessageDialog(null,
                    "Một quyền phải có ít nhất một chức năng.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

}
