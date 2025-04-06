/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

/**
 *
 * @author Thang Nguyen
 */
public class PermissionBUS {

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

    public static String encodeFunctionName(String displayName) {
        return switch (displayName) {
            // Sản phẩm
            case "Xem sản phẩm" ->
                "CN001";
            case "Sửa sản phẩm" ->
                "CN002";

            // Hóa đơn
            case "Xem hóa đơn" ->
                "CN003";
            case "Thêm hóa đơn" ->
                "CN004";
            case "Sửa hóa đơn" ->
                "CN005";
            case "Xóa hóa đơn" ->
                "CN006";

            // Nhân viên
            case "Xem nhân viên" ->
                "CN007";
            case "Thêm nhân viên" ->
                "CN008";
            case "Sửa nhân viên" ->
                "CN009";
            case "Xóa nhân viên" ->
                "CN010";

            // Nhà cung cấp
            case "Xem nhà cung cấp" ->
                "CN011";
            case "Thêm nhà cung cấp" ->
                "CN012";
            case "Sửa nhà cung cấp" ->
                "CN013";
            case "Xóa nhà cung cấp" ->
                "CN014";

            // Hóa đơn nhập
            case "Xem hóa đơn nhập" ->
                "CN015";
            case "Thêm hóa đơn nhập" ->
                "CN016";
            case "Xóa hóa đơn nhập" ->
                "CN017";

            // Khách hàng
            case "Xem khách hàng" ->
                "CN018";
            case "Thêm khách hàng" ->
                "CN019";
            case "Sửa khách hàng" ->
                "CN020";
            case "Xóa khách hàng" ->
                "CN021";

            // Tài khoản
            case "Xem tài khoản" ->
                "CN022";
            case "Thêm tài khoản" ->
                "CN023";
            case "Sửa tài khoản" ->
                "CN024";
            case "Xóa tài khoản" ->
                "CN025";

            // Bảo hành
            case "Xem bảo hành" ->
                "CN026";
            case "Sửa bảo hành" ->
                "CN027";

            // Quyền
            case "Xem quyền" ->
                "CN028";
            case "Thêm quyền" ->
                "CN029";
            case "Sửa quyền" ->
                "CN030";
            case "Xóa quyền" ->
                "CN031";

            // Thống kê
            case "Xem thống kê" ->
                "CN032";

            default ->
                displayName; // Trả về nguyên bản nếu không khớp
        };
    }
}
