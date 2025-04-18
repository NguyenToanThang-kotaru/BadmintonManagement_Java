-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 18, 2025 at 04:27 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `shopcaulong_java`
--

-- --------------------------------------------------------

--
-- Table structure for table `bao_hanh`
--

CREATE TABLE `bao_hanh` (
  `ma_bao_hanh` varchar(100) NOT NULL,
  `ma_serial` varchar(100) NOT NULL,
  `ly_do_bao_hanh` varchar(200) NOT NULL,
  `thoi_gian_bao_hanh` int(100) NOT NULL,
  `trang_thai` varchar(200) NOT NULL,
  `is_deleted` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Triggers `bao_hanh`
--
DELIMITER $$
CREATE TRIGGER `before_insert_update_bao_hanh` BEFORE INSERT ON `bao_hanh` FOR EACH ROW BEGIN
    DECLARE ngay_xuat_hd DATE;
    SELECT hd.ngay_xuat INTO ngay_xuat_hd
    FROM chi_tiet_hoa_don ct
    JOIN hoa_don hd ON ct.ma_hoa_don = hd.ma_hoa_don
    WHERE ct.ma_serial = NEW.ma_serial
    LIMIT 1;
    
    SET NEW.thoi_gian_bao_hanh = DATEDIFF(NOW(), ngay_xuat_hd);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `chi_tiet_hoa_don`
--

CREATE TABLE `chi_tiet_hoa_don` (
  `ma_chi_tiet_hoa_don` varchar(100) NOT NULL,
  `ma_san_pham` varchar(100) NOT NULL,
  `so_luong` int(11) NOT NULL,
  `gia` int(11) NOT NULL,
  `ma_hoa_don` varchar(100) NOT NULL,
  `ma_serial` varchar(100) NOT NULL,
  `is_deleted` tinyint(4) NOT NULL,
  `loi_nhuan` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `chi_tiet_nhap_hang`
--

CREATE TABLE `chi_tiet_nhap_hang` (
  `ma_chi_tiet_nhap_hang` varchar(100) NOT NULL,
  `ma_nhap_hang` varchar(100) NOT NULL,
  `ma_san_pham` varchar(100) NOT NULL,
  `so_luong` int(11) NOT NULL,
  `gia` int(11) NOT NULL,
  `is_deleted` tinyint(4) NOT NULL,
  `ma_nha_cung_cap` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `chuc_nang`
--

CREATE TABLE `chuc_nang` (
  `ma_chuc_nang` varchar(100) NOT NULL,
  `ten_chuc_nang` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `chuc_nang`
--

INSERT INTO `chuc_nang` (`ma_chuc_nang`, `ten_chuc_nang`) VALUES
('CN001', 'xem_sp'),
('CN002', 'sua_sp'),
('CN003', 'xem_hd'),
('CN004', 'them_hd'),
('CN005', 'sua_hd'),
('CN006', 'xoa_hd'),
('CN007', 'xem_nv'),
('CN008', 'them_nv'),
('CN009', 'sua_nv'),
('CN010', 'xoa_nv'),
('CN011', 'xem_ncc'),
('CN012', 'them_ncc'),
('CN013', 'sua_ncc'),
('CN014', 'xoa_ncc'),
('CN015', 'xem_hdn'),
('CN016', 'them_hdn'),
('CN017', 'xoa_hdn'),
('CN018', 'xem_kh'),
('CN019', 'them_kh'),
('CN020', 'sua_kh'),
('CN021', 'xoa_kh'),
('CN022', 'xem_tk'),
('CN023', 'them_tk'),
('CN024', 'sua_tk'),
('CN025', 'xoa_tk'),
('CN026', 'xem_bh'),
('CN027', 'sua_bh'),
('CN028', 'xem_quyen'),
('CN029', 'them_quyen'),
('CN030', 'sua_quyen'),
('CN031', 'xoa_quyen'),
('CN032', 'xem_thongke');

-- --------------------------------------------------------

--
-- Table structure for table `danh_sach_san_pham`
--

CREATE TABLE `danh_sach_san_pham` (
  `ma_serial` varchar(100) NOT NULL,
  `ma_san_pham` varchar(100) NOT NULL,
  `is_deleted` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `hoa_don`
--

CREATE TABLE `hoa_don` (
  `ma_hoa_don` varchar(100) NOT NULL,
  `ma_nhan_vien` varchar(100) NOT NULL,
  `ma_khach_hang` varchar(100) DEFAULT NULL,
  `tong_tien` int(11) NOT NULL,
  `ngay_xuat` datetime DEFAULT current_timestamp(),
  `is_deleted` tinyint(4) NOT NULL,
  `tong_loi_nhuan` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Triggers `hoa_don`
--
DELIMITER $$
CREATE TRIGGER `update_bao_hanh_when_hoa_don_changes` AFTER INSERT ON `hoa_don` FOR EACH ROW BEGIN
    UPDATE bao_hanh bh
    JOIN chi_tiet_hoa_don ct ON bh.ma_serial = ct.ma_serial
    SET bh.thoi_gian_bao_hanh = DATEDIFF(NOW(), NEW.ngay_xuat)
    WHERE ct.ma_hoa_don = NEW.ma_hoa_don;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `update_bao_hanh_when_ngay_xuat_changes` AFTER UPDATE ON `hoa_don` FOR EACH ROW BEGIN
    IF OLD.ngay_xuat <> NEW.ngay_xuat THEN
        UPDATE bao_hanh bh
        JOIN chi_tiet_hoa_don ct ON bh.ma_serial = ct.ma_serial
        SET bh.thoi_gian_bao_hanh = DATEDIFF(NOW(), NEW.ngay_xuat)
        WHERE ct.ma_hoa_don = NEW.ma_hoa_don;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `khach_hang`
--

CREATE TABLE `khach_hang` (
  `ma_khach_hang` varchar(100) NOT NULL,
  `ten_khach_hang` varchar(100) NOT NULL,
  `so_dien_thoai` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `is_deleted` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `loai`
--

CREATE TABLE `loai` (
  `ma_loai` varchar(100) NOT NULL,
  `ten_loai` varchar(200) NOT NULL,
  `is_deleted` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `loai`
--

INSERT INTO `loai` (`ma_loai`, `ten_loai`, `is_deleted`) VALUES
('L001', 'Vợt cầu lông', 0),
('L002', 'Quả cầu lông', 0),
('L003', 'Lưới cầu lông', 0),
('L004', 'Giày cầu lông', 0),
('L005', 'Quần áo cầu lông', 0),
('L006', 'Túi đựng vợt', 0),
('L007', 'Băng quấn cán vợt', 0),
('L008', 'Balo cầu lông', 0),
('L009', 'Băng bảo vệ khớp', 0),
('L010', 'Phụ kiện cầu lông khác', 0);

-- --------------------------------------------------------

--
-- Table structure for table `nhan_vien`
--

CREATE TABLE `nhan_vien` (
  `ma_nhan_vien` varchar(100) NOT NULL,
  `ten_nhan_vien` varchar(100) NOT NULL,
  `dia_chi` varchar(100) NOT NULL,
  `so_dien_thoai` varchar(100) NOT NULL,
  `hinh_anh` varchar(200) NOT NULL,
  `is_deleted` tinyint(1) DEFAULT 0,
  `chuc_vu` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `nhan_vien`
--

INSERT INTO `nhan_vien` (`ma_nhan_vien`, `ten_nhan_vien`, `dia_chi`, `so_dien_thoai`, `hinh_anh`, `is_deleted`, `chuc_vu`) VALUES
('NV001', 'Nguyễn Văn A', 'Hà Nội', '0901234567', 'avatar1.jpg', 1, ''),
('NV002', 'Trần Thị B', 'TP. Hồ Chí Minh', '0912345678', '1743538861629_soan-bai-luat-tho-tiep-theo.png', 1, ''),
('NV003', 'Lê Văn C', 'Đà Nẵng', '0923456789', '1743774319722_2(4).png', 1, ''),
('NV004', 'Phạm Thị D', 'Cần Thơ', '0934567890', '1743774287102_DSCN1441.JPG', 1, ''),
('NV005', 'Hoàng Văn E', 'Hải Phòng', '0945678901', '1743774229387_263554970_1083194635830194_7616960133313018704_n.jpg', 0, ''),
('NV006', 'Đặng Thị F', 'Nha Trang', '0956789012', 'avatar6.jpg', 0, ''),
('NV007', 'Bùi Văn G', 'Huế', '0967890123', 'avatar7.jpg', 0, ''),
('NV008', 'Ngô Thị H', 'Quảng Ninh', '0978901234', '1743776284561_1354305.jpeg', 0, ''),
('NV009', 'Dương Văn I', 'Bình Dương', '0989012345', 'avatar9.jpg', 0, ''),
('NV010', 'Vũ Thị K', 'Bắc Giang', '0990123456', 'avatar10.jpg', 0, ''),
('NV011', 'Mai Văn L', 'Thanh Hóa', '0901122334', 'avatar11.jpg', 0, ''),
('NV012', 'Lý Thị M', 'Nghệ An', '0912233445', 'avatar12.jpg', 0, ''),
('NV013', 'Tô Văn N', 'Hải Dương', '0923344556', 'avatar13.jpg', 1, ''),
('NV014', 'Châu Thị O', 'Bến Tre', '0934455667', 'avatar14.jpg', 0, ''),
('NV015', 'Cao Văn P', 'Vĩnh Long', '0945566778', 'avatar15.jpg', 0, ''),
('NV016', 'Đỗ Thị Q', 'Đắk Lắk', '0956677889', 'avatar16.jpg', 0, ''),
('NV017', 'Tống Văn R', 'Sóc Trăng', '0967788990', 'avatar17.jpg', 0, ''),
('NV018', 'Lương Thị S', 'Tây Ninh', '0978899001', 'avatar18.jpg', 0, ''),
('NV019', 'Quách Văn T', 'Bình Thuận', '0989900112', 'avatar19.jpg', 0, ''),
('NV020', 'Hà Thị U', 'Hòa Bình', '0991001123', '1743776053761_2(1).png', 1, ''),
('NV021', 'Tạ Văn V', 'Lào Cai', '0902102234', 'avatar21.jpg', 0, ''),
('NV022', 'Từ Thị X', 'Hà Giang', '0913203345', 'avatar22.jpg', 0, ''),
('NV023', 'La Văn Y', 'Nam Định', '0924304456', 'avatar23.jpg', 0, ''),
('NV024', 'Triệu Thị Z', 'Thái Nguyên', '0935405567', 'avatar24.jpg', 0, ''),
('NV025', 'Nguyễn Văn AA', 'Phú Thọ', '0946506678', 'avatar25.jpg', 0, ''),
('NV026', 'Trần Thị BB', 'Quảng Trị', '0957607896', '1743774390476_RobloxScreenShot20230505_234240948.png', 1, ''),
('NV027', 'Lê Văn CC', 'Quảng Nam', '0968708890', 'avatar27.jpg', 1, ''),
('NV028', 'Phạm Thị DD', 'Bắc Ninh', '0979809991', 'avatar28.jpg', 1, ''),
('NV029', '??', '???', '??', '1743528188377_162723790_450399786046160_3059394344420075342_n.jpg', 1, ''),
('NV030', 'OH la la', '123', '123456', '1743695696827_soan-bai-luat-tho-tiep-theo.png', 0, '');

-- --------------------------------------------------------

--
-- Table structure for table `nhap_hang`
--

CREATE TABLE `nhap_hang` (
  `ma_nhap_hang` varchar(100) NOT NULL,
  `ma_nhan_vien` varchar(100) NOT NULL,
  `tong_tien` int(11) NOT NULL,
  `ngay_nhap` datetime DEFAULT current_timestamp(),
  `is_deleted` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `nha_cung_cap`
--

CREATE TABLE `nha_cung_cap` (
  `ma_nha_cung_cap` varchar(100) NOT NULL,
  `ten_nha_cung_cap` varchar(100) NOT NULL,
  `dia_chi` varchar(200) NOT NULL,
  `so_dien_thoai` varchar(100) NOT NULL,
  `is_deleted` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `nha_cung_cap`
--

INSERT INTO `nha_cung_cap` (`ma_nha_cung_cap`, `ten_nha_cung_cap`, `dia_chi`, `so_dien_thoai`, `is_deleted`) VALUES
('NCC001', 'Lining Việt Nam', 'Cần Thơ', '0981000005', 0),
('NCC002', 'Yonex Official Store', 'Hà Nội', '0981000006', 0),
('NCC003', 'ProAce Việt Nam', 'TP. Hồ Chí Minh', '0981000007', 0),
('NCC004', 'Victor Sport', 'Đà Nẵng', '0981000008', 0),
('NCC005', 'Shop Cầu Lông 24h', 'Hải Phòng', '0981000009', 0);

-- --------------------------------------------------------

--
-- Table structure for table `phan_quyen`
--

CREATE TABLE `phan_quyen` (
  `ma_quyen` varchar(100) NOT NULL,
  `ma_chuc_nang` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `phan_quyen`
--

INSERT INTO `phan_quyen` (`ma_quyen`, `ma_chuc_nang`) VALUES
('3', 'CN001'),
('3', 'CN002'),
('3', 'CN011'),
('3', 'CN012'),
('3', 'CN013'),
('3', 'CN014'),
('3', 'CN015'),
('3', 'CN016'),
('3', 'CN017'),
('2', 'CN001'),
('2', 'CN002'),
('1', 'CN001'),
('1', 'CN002'),
('1', 'CN003'),
('1', 'CN004'),
('1', 'CN005'),
('1', 'CN006'),
('1', 'CN007'),
('1', 'CN008'),
('1', 'CN009'),
('1', 'CN010'),
('1', 'CN011'),
('1', 'CN012'),
('1', 'CN013'),
('1', 'CN014'),
('1', 'CN015'),
('1', 'CN016'),
('1', 'CN017'),
('1', 'CN018'),
('1', 'CN019'),
('1', 'CN020'),
('1', 'CN021'),
('1', 'CN022'),
('1', 'CN023'),
('1', 'CN024'),
('1', 'CN025'),
('1', 'CN026'),
('1', 'CN027'),
('1', 'CN028'),
('1', 'CN029'),
('1', 'CN030'),
('1', 'CN031'),
('1', 'CN032');

-- --------------------------------------------------------

--
-- Table structure for table `quyen`
--

CREATE TABLE `quyen` (
  `ma_quyen` varchar(100) NOT NULL,
  `ten_quyen` varchar(200) NOT NULL,
  `is_deleted` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `quyen`
--

INSERT INTO `quyen` (`ma_quyen`, `ten_quyen`, `is_deleted`) VALUES
('1', 'Admin', 0),
('2', 'Nhân Viên Bán Hàng', 0),
('3', 'Nhân Viên Kho', 0);

-- --------------------------------------------------------

--
-- Table structure for table `san_pham`
--

CREATE TABLE `san_pham` (
  `ma_san_pham` varchar(100) NOT NULL,
  `ten_san_pham` varchar(200) NOT NULL,
  `gia` int(11) NOT NULL,
  `so_luong` int(11) NOT NULL DEFAULT 0,
  `ma_nha_cung_cap` varchar(100) NOT NULL,
  `thong_so_ki_thuat` varchar(200) NOT NULL,
  `ma_loai` varchar(100) NOT NULL,
  `hinh_anh` varchar(200) NOT NULL,
  `is_deleted` tinyint(4) NOT NULL,
  `gia_goc` int(11) NOT NULL,
  `khuyen_mai` varchar(100) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `san_pham`
--

INSERT INTO `san_pham` (`ma_san_pham`, `ten_san_pham`, `gia`, `so_luong`, `ma_nha_cung_cap`, `thong_so_ki_thuat`, `ma_loai`, `hinh_anh`, `is_deleted`, `gia_goc`, `khuyen_mai`) VALUES
('SP001', 'Vợt cầu lông Yonex Astrox 99', 2300000, 0, 'NCC002', 'Vợt cao cấp cho người chơi chuyên nghiệp', 'L001', 'SP001.jpg', 0, 1840000, '0'),
('SP002', 'Vợt cầu lông Lining Turbo X', 9700000, 0, 'NCC001', 'Vợt nhẹ, dễ điều khiển', 'L001', 'icon_docgia.png', 0, 7760000, '0'),
('SP003', 'Vợt cầu lông Victor Brave Sword', 3500000, 0, 'NCC004', 'Công nghệ hiện đại giúp tăng sức mạnh', 'L001', 'SP003.jpg', 0, 2800000, '0'),
('SP004', 'Vợt cầu lông ProAce Sweet Spot', 2200000, 0, 'NCC003', 'Cán vợt bọc chống trơn', 'L001', 'SP004.jpg', 0, 1760000, '0'),
('SP005', 'Vợt cầu lông Kawasaki Power 600', 1500000, 0, 'NCC005', 'Phù hợp cho người mới chơi', 'L001', 'SP005.jpg', 0, 1200000, '0'),
('SP006', 'Quả cầu lông Yonex Mavis 350', 120000, 0, 'NCC002', 'Cầu lông nylon bền, bay chuẩn', 'L002', 'SP006.jpg', 0, 96000, '0'),
('SP007', 'Quả cầu lông Lining A+62', 130000, 0, 'NCC001', 'Lông vũ tự nhiên, tốc độ ổn định', 'L002', 'SP007.jpg', 0, 104000, '0'),
('SP008', 'Quả cầu lông Victor Gold Champion', 140000, 0, 'NCC004', 'Chất lượng thi đấu chuyên nghiệp', 'L002', 'SP008.jpg', 0, 112000, '0'),
('SP009', 'Quả cầu lông ProAce 500', 110000, 0, 'NCC003', 'Bền, phù hợp tập luyện', 'L002', 'SP009.jpg', 0, 88000, '0'),
('SP010', 'Quả cầu lông Kawasaki 700', 100000, 0, 'NCC005', 'Giá rẻ, chất lượng tốt', 'L002', 'SP010.jpg', 0, 80000, '0'),
('SP011', 'Lưới cầu lông Yonex Tournament', 500000, 0, 'NCC002', 'Lưới tiêu chuẩn thi đấu', 'L003', 'SP011.jpg', 0, 400000, '0'),
('SP012', 'Lưới cầu lông Lining Pro Net', 450000, 0, 'NCC001', 'Lưới chắc chắn, dễ lắp đặt', 'L003', 'SP012.jpg', 0, 360000, '0'),
('SP013', 'Lưới cầu lông Victor Standard', 400000, 0, 'NCC004', 'Dùng cho sân tập', 'L003', 'SP013.jpg', 0, 320000, '0'),
('SP014', 'Lưới cầu lông ProAce Durable', 380000, 0, 'NCC003', 'Dây viền chắc chắn', 'L003', 'SP014.jpg', 0, 304000, '0'),
('SP015', 'Lưới cầu lông Kawasaki 500', 300000, 0, 'NCC005', 'Giá rẻ, chất lượng khá', 'L003', 'SP015.jpg', 0, 240000, '0'),
('SP016', 'Giày cầu lông Yonex 88D', 2500000, 0, 'NCC002', 'Giày chuyên dụng, bám sân tốt', 'L004', 'SP016.jpg', 0, 2000000, '0'),
('SP017', 'Giày cầu lông Lining Ranger', 2200000, 0, 'NCC001', 'Thiết kế đẹp, êm chân', 'L004', 'SP017.jpg', 0, 1760000, '0'),
('SP018', 'Giày cầu lông Victor SH-A920', 2000000, 0, 'NCC004', 'Công nghệ giảm chấn hiện đại', 'L004', 'SP018.jpg', 0, 1600000, '0'),
('SP019', 'Giày cầu lông ProAce Fast Foot', 1800000, 0, 'NCC003', 'Phù hợp chơi phong trào', 'L004', 'SP019.jpg', 0, 1440000, '0'),
('SP020', 'Giày cầu lông Kawasaki Pro', 1500000, 0, 'NCC005', 'Đế bám tốt, chống trượt', 'L004', 'SP020.jpg', 0, 1200000, '0'),
('SP021', 'Áo cầu lông Yonex Pro', 600000, 0, 'NCC002', 'Vải thoáng khí, co giãn tốt', 'L005', 'SP021.jpg', 0, 480000, '0'),
('SP022', 'Áo cầu lông Lining Elite', 550000, 0, 'NCC001', 'Kiểu dáng thể thao, thoải mái', 'L005', 'SP022.jpg', 0, 440000, '0'),
('SP023', 'Áo cầu lông Victor Performance', 500000, 0, 'NCC004', 'Chất liệu nhẹ, hút mồ hôi tốt', 'L005', 'SP023.jpg', 0, 400000, '0'),
('SP024', 'Áo cầu lông ProAce Speed', 450000, 0, 'NCC003', 'Mềm mại, dễ chịu khi vận động', 'L005', 'SP024.jpg', 0, 360000, '0'),
('SP025', 'Áo cầu lông Kawasaki Pro Fit', 400000, 0, 'NCC005', 'Thiết kế trẻ trung, thời trang', 'L005', 'SP025.jpg', 0, 320000, '0'),
('SP026', 'Túi đựng vợt Yonex 3 ngăn', 900000, 0, 'NCC002', 'Chứa được nhiều vợt và phụ kiện', 'L006', 'SP026.jpg', 0, 720000, '0'),
('SP027', 'Túi đựng vợt Lining Pro', 850000, 0, 'NCC001', 'Chất liệu bền, chống thấm nước', 'L006', 'SP027.jpg', 0, 680000, '0'),
('SP028', 'Túi đựng vợt Victor Tournament', 800000, 0, 'NCC004', 'Ngăn chứa rộng rãi', 'L006', 'SP028.jpg', 0, 640000, '0'),
('SP029', 'Túi đựng vợt ProAce 2 ngăn', 750000, 0, 'NCC003', 'Nhỏ gọn, tiện lợi', 'L006', 'SP029.jpg', 0, 600000, '0'),
('SP030', 'Túi đựng vợt Kawasaki Standard', 700000, 0, 'NCC005', 'Chống thấm nước, bền đẹp', 'L006', 'SP030.jpg', 0, 560000, '0'),
('SP031', 'Băng quấn cán vợt Yonex Super Grip', 70000, 0, 'NCC002', 'Thấm mồ hôi tốt, độ bám cao', 'L007', 'SP031.jpg', 0, 56000, '0'),
('SP032', 'Băng quấn cán vợt Lining Pro', 65000, 0, 'NCC001', 'Chống trơn trượt, êm tay', 'L007', 'SP032.jpg', 0, 52000, '0'),
('SP033', 'Băng quấn cán vợt Victor Comfort', 60000, 0, 'NCC004', 'Bền, không bị bong tróc', 'L007', 'SP033.jpg', 0, 48000, '0'),
('SP034', 'Băng quấn cán vợt ProAce Tacky', 55000, 0, 'NCC003', 'Mềm mại, không gây đau tay', 'L007', 'SP034.jpg', 0, 44000, '0'),
('SP035', 'Băng quấn cán vợt Kawasaki Standard', 50000, 0, 'NCC005', 'Giá rẻ, chất lượng tốt', 'L007', 'SP035.jpg', 0, 40000, '0'),
('SP036', 'Balo cầu lông Yonex Tournament', 950000, 0, 'NCC002', 'Nhiều ngăn, chống nước', 'L008', 'SP036.jpg', 0, 760000, '0'),
('SP037', 'Balo cầu lông Lining Elite', 900000, 0, 'NCC001', 'Thiết kế đẹp, bền chắc', 'L008', 'SP037.jpg', 0, 720000, '0'),
('SP038', 'Balo cầu lông Victor Pro', 850000, 0, 'NCC004', 'Dây đeo êm ái, thoáng khí', 'L008', 'SP038.jpg', 0, 680000, '0'),
('SP039', 'Balo cầu lông ProAce Fit', 800000, 0, 'NCC003', 'Nhỏ gọn, phù hợp di chuyển', 'L008', 'SP039.jpg', 0, 640000, '0'),
('SP040', 'Balo cầu lông Kawasaki Durable', 750000, 0, 'NCC005', 'Chất liệu chống thấm nước', 'L008', 'SP040.jpg', 0, 600000, '0'),
('SP041', 'Băng bảo vệ khớp Yonex Support', 250000, 0, 'NCC002', 'Giảm áp lực lên khớp', 'L009', 'SP041.jpg', 0, 200000, '0'),
('SP042', 'Băng bảo vệ khớp Lining Strong', 230000, 0, 'NCC001', 'Chất liệu co giãn, thoáng khí', 'L009', 'SP042.jpg', 0, 184000, '0'),
('SP043', 'Băng bảo vệ khớp Victor Performance', 220000, 0, 'NCC004', 'Dùng cho thi đấu chuyên nghiệp', 'L009', 'SP043.jpg', 0, 176000, '0'),
('SP044', 'Băng bảo vệ khớp ProAce Flexible', 200000, 0, 'NCC003', 'Mềm mại, dễ chịu khi đeo', 'L009', 'SP044.jpg', 0, 160000, '0'),
('SP045', 'Băng bảo vệ khớp Kawasaki Fit', 180000, 0, 'NCC005', 'Chống sốc tốt, bảo vệ khớp', 'L009', 'SP045.jpg', 0, 144000, '0'),
('SP046', 'Dây đan vợt Yonex BG65', 150000, 0, 'NCC002', 'Dây bền, độ căng tốt', 'L010', 'SP046.jpg', 0, 120000, '0'),
('SP047', 'Dây đan vợt Lining No.1', 140000, 0, 'NCC001', 'Tốc độ cao, bền bỉ', 'L010', 'SP047.jpg', 0, 112000, '0'),
('SP048', 'Dây đan vợt Victor VS850', 130000, 0, 'NCC004', 'Dành cho người chơi chuyên nghiệp', 'L010', 'SP048.jpg', 0, 104000, '0'),
('SP049', 'Dây đan vợt ProAce Ti-68', 120000, 0, 'NCC003', 'Giá rẻ, chất lượng ổn', 'L010', 'SP049.jpg', 0, 96000, '0'),
('SP050', 'Dây đan vợt Kawasaki Power', 110000, 0, 'NCC005', 'Dễ sử dụng, bền lâu', 'L010', 'SP050.jpg', 0, 88000, '0');

-- --------------------------------------------------------

--
-- Table structure for table `tai_khoan`
--

CREATE TABLE `tai_khoan` (
  `ma_tai_khoan` varchar(100) NOT NULL,
  `ten_dang_nhap` varchar(100) NOT NULL,
  `mat_khau` varchar(100) NOT NULL,
  `ma_quyen` varchar(100) NOT NULL,
  `is_deleted` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tai_khoan`
--

INSERT INTO `tai_khoan` (`ma_tai_khoan`, `ten_dang_nhap`, `mat_khau`, `ma_quyen`, `is_deleted`) VALUES
('TK001', 'NV001', '123456', '1', 0),
('TK002', 'NV002', '123456', '2', 0),
('TK003', 'NV003', '123456', '2', 0),
('TK004', 'NV004', '123456', '2', 0),
('TK005', 'NV005', '123456', '2', 0),
('TK006', 'NV006', '123456', '2', 0),
('TK007', 'NV007', '123456', '2', 0),
('TK008', 'NV008', '123456', '2', 0),
('TK009', 'NV009', '123456', '2', 0),
('TK010', 'NV010', '123456', '2', 0),
('TK011', 'NV011', '123456', '2', 0),
('TK012', 'NV012', '123456', '2', 0),
('TK013', 'NV013', '123456', '2', 0),
('TK014', 'NV014', '123456', '2', 0),
('TK015', 'NV015', '123456', '2', 0),
('TK016', 'NV016', '123456', '3', 0),
('TK017', 'NV017', '123456', '3', 0),
('TK018', 'NV018', '123456', '3', 0),
('TK019', 'NV019', '123456', '3', 0),
('TK020', 'NV020', '123456', '3', 0),
('TK021', 'NV021', '123456', '3', 0),
('TK022', 'NV022', '123456', '3', 0),
('TK023', 'NV023', '123456', '3', 0),
('TK024', 'NV024', '123456', '3', 0),
('TK025', 'NV025', '123456', '3', 0),
('TK026', 'NV026', '123456', '3', 0),
('TK027', 'NV027', '123456', '3', 0),
('TK028', 'NV030', '123', '1', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bao_hanh`
--
ALTER TABLE `bao_hanh`
  ADD PRIMARY KEY (`ma_bao_hanh`),
  ADD KEY `ma_serial` (`ma_serial`);

--
-- Indexes for table `chi_tiet_hoa_don`
--
ALTER TABLE `chi_tiet_hoa_don`
  ADD PRIMARY KEY (`ma_chi_tiet_hoa_don`),
  ADD KEY `ma_hoa_don` (`ma_hoa_don`),
  ADD KEY `ma_san_pham` (`ma_san_pham`),
  ADD KEY `ma_serial` (`ma_serial`);

--
-- Indexes for table `chi_tiet_nhap_hang`
--
ALTER TABLE `chi_tiet_nhap_hang`
  ADD PRIMARY KEY (`ma_chi_tiet_nhap_hang`),
  ADD KEY `ma_san_pham` (`ma_san_pham`),
  ADD KEY `ma_nhap_hang` (`ma_nhap_hang`),
  ADD KEY `ma_nha_cung_cap` (`ma_nha_cung_cap`);

--
-- Indexes for table `chuc_nang`
--
ALTER TABLE `chuc_nang`
  ADD PRIMARY KEY (`ma_chuc_nang`);

--
-- Indexes for table `danh_sach_san_pham`
--
ALTER TABLE `danh_sach_san_pham`
  ADD PRIMARY KEY (`ma_serial`),
  ADD KEY `ma_san_pham` (`ma_san_pham`);

--
-- Indexes for table `hoa_don`
--
ALTER TABLE `hoa_don`
  ADD PRIMARY KEY (`ma_hoa_don`),
  ADD KEY `ma_khach_hang` (`ma_khach_hang`),
  ADD KEY `ma_nhan_vien` (`ma_nhan_vien`);

--
-- Indexes for table `khach_hang`
--
ALTER TABLE `khach_hang`
  ADD PRIMARY KEY (`ma_khach_hang`);

--
-- Indexes for table `loai`
--
ALTER TABLE `loai`
  ADD PRIMARY KEY (`ma_loai`);

--
-- Indexes for table `nhan_vien`
--
ALTER TABLE `nhan_vien`
  ADD PRIMARY KEY (`ma_nhan_vien`);

--
-- Indexes for table `nhap_hang`
--
ALTER TABLE `nhap_hang`
  ADD PRIMARY KEY (`ma_nhap_hang`),
  ADD KEY `ma_nhan_vien` (`ma_nhan_vien`);

--
-- Indexes for table `nha_cung_cap`
--
ALTER TABLE `nha_cung_cap`
  ADD PRIMARY KEY (`ma_nha_cung_cap`);

--
-- Indexes for table `phan_quyen`
--
ALTER TABLE `phan_quyen`
  ADD KEY `ma_chuc_nang` (`ma_chuc_nang`),
  ADD KEY `ma_quyen` (`ma_quyen`);

--
-- Indexes for table `quyen`
--
ALTER TABLE `quyen`
  ADD PRIMARY KEY (`ma_quyen`);

--
-- Indexes for table `san_pham`
--
ALTER TABLE `san_pham`
  ADD PRIMARY KEY (`ma_san_pham`),
  ADD KEY `ma_loai` (`ma_loai`),
  ADD KEY `ma_nha_cung_cap` (`ma_nha_cung_cap`);

--
-- Indexes for table `tai_khoan`
--
ALTER TABLE `tai_khoan`
  ADD PRIMARY KEY (`ma_tai_khoan`),
  ADD KEY `ten_dang_nhap` (`ten_dang_nhap`),
  ADD KEY `ma_quyen` (`ma_quyen`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bao_hanh`
--
ALTER TABLE `bao_hanh`
  ADD CONSTRAINT `bao_hanh_ibfk_1` FOREIGN KEY (`ma_serial`) REFERENCES `danh_sach_san_pham` (`ma_serial`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `chi_tiet_hoa_don`
--
ALTER TABLE `chi_tiet_hoa_don`
  ADD CONSTRAINT `chi_tiet_hoa_don_ibfk_1` FOREIGN KEY (`ma_hoa_don`) REFERENCES `hoa_don` (`ma_hoa_don`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `chi_tiet_hoa_don_ibfk_2` FOREIGN KEY (`ma_san_pham`) REFERENCES `san_pham` (`ma_san_pham`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `chi_tiet_hoa_don_ibfk_3` FOREIGN KEY (`ma_serial`) REFERENCES `danh_sach_san_pham` (`ma_serial`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `chi_tiet_nhap_hang`
--
ALTER TABLE `chi_tiet_nhap_hang`
  ADD CONSTRAINT `chi_tiet_nhap_hang_ibfk_1` FOREIGN KEY (`ma_san_pham`) REFERENCES `san_pham` (`ma_san_pham`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `chi_tiet_nhap_hang_ibfk_2` FOREIGN KEY (`ma_nhap_hang`) REFERENCES `nhap_hang` (`ma_nhap_hang`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `chi_tiet_nhap_hang_ibfk_3` FOREIGN KEY (`ma_nha_cung_cap`) REFERENCES `nha_cung_cap` (`ma_nha_cung_cap`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `danh_sach_san_pham`
--
ALTER TABLE `danh_sach_san_pham`
  ADD CONSTRAINT `danh_sach_san_pham_ibfk_1` FOREIGN KEY (`ma_san_pham`) REFERENCES `san_pham` (`ma_san_pham`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `hoa_don`
--
ALTER TABLE `hoa_don`
  ADD CONSTRAINT `hoa_don_ibfk_1` FOREIGN KEY (`ma_khach_hang`) REFERENCES `khach_hang` (`ma_khach_hang`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `hoa_don_ibfk_2` FOREIGN KEY (`ma_nhan_vien`) REFERENCES `nhan_vien` (`ma_nhan_vien`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `nhap_hang`
--
ALTER TABLE `nhap_hang`
  ADD CONSTRAINT `nhap_hang_ibfk_1` FOREIGN KEY (`ma_nhan_vien`) REFERENCES `nhan_vien` (`ma_nhan_vien`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `phan_quyen`
--
ALTER TABLE `phan_quyen`
  ADD CONSTRAINT `phan_quyen_ibfk_1` FOREIGN KEY (`ma_chuc_nang`) REFERENCES `chuc_nang` (`ma_chuc_nang`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `phan_quyen_ibfk_2` FOREIGN KEY (`ma_quyen`) REFERENCES `quyen` (`ma_quyen`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `san_pham`
--
ALTER TABLE `san_pham`
  ADD CONSTRAINT `san_pham_ibfk_1` FOREIGN KEY (`ma_loai`) REFERENCES `loai` (`ma_loai`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `san_pham_ibfk_2` FOREIGN KEY (`ma_nha_cung_cap`) REFERENCES `nha_cung_cap` (`ma_nha_cung_cap`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tai_khoan`
--
ALTER TABLE `tai_khoan`
  ADD CONSTRAINT `tai_khoan_ibfk_1` FOREIGN KEY (`ten_dang_nhap`) REFERENCES `nhan_vien` (`ma_nhan_vien`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tai_khoan_ibfk_2` FOREIGN KEY (`ma_quyen`) REFERENCES `quyen` (`ma_quyen`) ON DELETE CASCADE ON UPDATE CASCADE;

DELIMITER $$
--
-- Events
--
CREATE DEFINER=`root`@`localhost` EVENT `cap_nhat_bao_hanh` ON SCHEDULE EVERY 5 MINUTE STARTS '2025-03-28 01:22:59' ON COMPLETION PRESERVE ENABLE DO UPDATE bao_hanh bh  
JOIN chi_tiet_hoa_don cthd ON bh.ma_serial = cthd.ma_serial  
JOIN hoa_don hd ON cthd.ma_hoa_don = hd.ma_hoa_don  
SET bh.thoi_gian_bao_hanh = DATEDIFF(CURDATE(), hd.ngay_xuat)$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
