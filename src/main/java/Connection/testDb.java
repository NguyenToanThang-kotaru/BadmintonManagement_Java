package Connection;
//
//import Connection.DatabaseConnection;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//public class testDb {
//    public static void main(String[] args) {
//        try (Connection conn = DatabaseConnection.getConnection()) {
//            if (conn != null) {
//                System.out.println("Kết nối đến database thành công!");
//            } else {
//                System.out.println("Kết nối thất bại!");
//            }
//        } catch (SQLException e) {
//            System.out.println("Lỗi kết nối đến database: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class testDb {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Button Click Example");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JButton button = new JButton("Click Me");
        button.setBounds(100, 50, 100, 30);

        // Thêm sự kiện click cho button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                check(); // Gọi hàm check khi bấm nút
            }
        });

        frame.add(button);
        frame.setVisible(true);
    }

    // Hàm check
    public static void check() {
        JOptionPane.showMessageDialog(null, "Nút đã được nhấn!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
