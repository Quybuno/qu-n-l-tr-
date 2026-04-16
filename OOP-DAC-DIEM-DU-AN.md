# 6 điểm OOP/MVC + DAO trong dự án Quản lý phòng trọ

Tài liệu này giải thích **chi tiết 6 điểm** bạn liệt kê (MVC+DAO, Đóng gói, Kế thừa, Đa hình, Trừu tượng hóa, Enum type-safe) và **đối chiếu trực tiếp** với mã nguồn trong `src/main/java` (Swing UI + controller + DAO + DB).

---

## 1) MVC + DAO: `ui (View) → controller → dao → DB`, `model` dùng chung

### Ý nghĩa trong dự án

- **View (`ui`)**: chỉ lo **hiển thị + nhận thao tác người dùng** (button, table, form…). View không tự viết SQL, không tự quản lý `Connection`, và hạn chế xử lý nghiệp vụ phức tạp.
- **Controller (`controller`)**: là “người điều phối”:
  - Nhận dữ liệu từ UI (ví dụ: input từ textfield, selection từ combobox).
  - Kiểm tra/chuẩn hóa dữ liệu (nếu có).
  - Gọi DAO để thao tác DB.
  - Trả kết quả về cho UI (list, entity, số liệu thống kê…).
- **DAO (`dao`)**: chịu trách nhiệm **truy cập dữ liệu**:
  - Chứa **JDBC/SQL** (`Connection`, `PreparedStatement`, `ResultSet`).
  - Mapping dữ liệu bảng ↔ object model.
  - Cung cấp các hàm CRUD / truy vấn theo điều kiện.
- **DB**: nơi lưu trữ trạng thái bền vững.
- **Model (`model`)**: là **các entity** dùng chung cho mọi tầng, ví dụ `PhongTro`, `HopDong`, `HoaDon`,… để UI/Controller/DAO “nói chuyện” bằng cùng một kiểu dữ liệu.

### Luồng gọi minh họa đúng theo code hiện tại

- **UI gọi controller**: `MainFrame` tạo các panel và truyền controller vào panel.
- **Controller gọi DAO**: ví dụ `ThongKeController` nắm `PhongTroDAO` và `HoaDonDAO` để tính thống kê.
- **DAO thao tác DB**: ví dụ `HopDongDAO` dùng JDBC/SQL trong các hàm `add/update/delete/findById/getAll`.

Ví dụ cụ thể “đi một vòng” (dễ dùng để thuyết trình):

- **`PhongPanel.themPhong()` (UI)** đọc input, rồi gọi `phongController.themPhong(dayTroId, maPhong, dienTich, gia)`.
- **`PhongController.themPhong()` (Controller)** kiểm tra hợp lệ, tạo entity `PhongTro`, set `TrangThaiPhong.TRONG`, rồi gọi `phongTroDAO.add(p)`.
- **`PhongTroDAO.add()` (DAO)** thực thi câu lệnh SQL `INSERT` để ghi xuống DB.

Điểm lợi quan trọng của kiến trúc này:

- **Dễ bảo trì**: đổi SQL/DB schema chủ yếu chạm DAO; đổi UI chủ yếu chạm `ui`.
- **Dễ test logic**: controller tách được khỏi UI (ít phụ thuộc Swing component).
- **Giảm “lặp code JDBC”** ở UI: UI không phải copy-paste SQL.

---

## 2) Encapsulation (Đóng gói): entity private + getter/setter; JDBC/SQL đóng trong DAO

### 2.1. Đóng gói dữ liệu trong entity model

Trong model như `PhongTro`, các thuộc tính để **`private`** và chỉ truy cập qua getter/setter:

- Người dùng class không thể “đụng thẳng” vào biến nội bộ.
- Bạn có thể bổ sung validation/bất biến (invariant) trong setter/constructor mà không làm vỡ code bên ngoài.

Ví dụ trong `PhongTro`:

- `private String id;`
- `private BigDecimal giaThueThang;`
- `private TrangThaiPhong trangThai;`
- getter/setter tương ứng.

### 2.2. Đóng gói JDBC/SQL trong DAO

Toàn bộ chi tiết JDBC/SQL được “gói” trong DAO, ví dụ `HopDongDAO`:

- `DatabaseUtil.getConnection()` và câu SQL nằm trong `add/update/delete/findById/getAll`.
- Mapping row sang object được gom vào hàm helper như `mapRow(ResultSet rs)`.

Kết quả là:

- UI/Controller **không cần biết** DB đang là gì, query thế nào.
- Nếu thay đổi câu lệnh SQL hay schema, bạn chỉnh chủ yếu ở DAO.

---

## 3) Inheritance (Kế thừa): `MainFrame extends JFrame`, các `*Panel extends JPanel`

### Kế thừa trong Swing UI

- `MainFrame extends JFrame`: tái sử dụng toàn bộ khả năng cửa sổ Swing (`setSize`, `setDefaultCloseOperation`, layout, tabs…).
- Các màn hình con dạng tab:
  - `PhongPanel`, `KhachPanel`, `HopDongPanel`, `ChiSoPanel`, `HoaDonPanel`, `ThongKePanel`, `DayTroPanel`… **đều** kế thừa `JPanel` để trở thành một component có thể nhúng vào `JTabbedPane`.

### Kế thừa qua lớp ẩn danh (anonymous class)

Trong `MainFrame` có đoạn:

- `cbDay.setRenderer(new DefaultListCellRenderer() { ... override ... })`

Đây cũng là **kế thừa**, chỉ là bạn không tạo một file class riêng mà tạo subclass “tại chỗ” để override hành vi hiển thị.

---

## 4) Polymorphism (Đa hình)

Đa hình nghĩa là: **cùng một “lời gọi”/“kiểu tham chiếu” nhưng hành vi có thể khác nhau tùy đối tượng thật ở runtime** (đa hình động), hoặc nhiều hàm cùng tên khác tham số (đa hình tĩnh).

### 4.1. Đa hình qua `Refreshable.refreshData()`

`Refreshable` định nghĩa 1 “hợp đồng”:

- Mọi panel “có thể refresh” thì cung cấp `refreshData()`.
- `MainFrame` có thể gọi refresh cho nhiều panel theo cùng một cách.

Trong code hiện tại, `MainFrame.refreshAllPanels()` gọi:

- `phongPanel.refreshData();`
- `hopDongPanel.refreshData();`
- `chiSoPanel.refreshData();`
- `hoaDonPanel.refreshData();`
- `thongKePanel.refreshData();`

Mỗi panel sẽ implement `refreshData()` theo cách riêng (load bảng khác nhau, gọi controller khác nhau…), nhưng `MainFrame` **không cần biết chi tiết**.

### 4.2. Đa hình qua `IGenericDAO<T>` (đa hình + generics)

`IGenericDAO<T>` mô tả CRUD chung:

- `add(T)`, `update(T)`, `delete(id)`, `findById(id)`, `getAll()`

Nhiều DAO implement cùng interface này (với `T` khác nhau) tạo ra:

- **Tính thống nhất API**: các DAO “trông giống nhau” khi dùng ở controller.
- **Thay thế được**: về nguyên tắc, controller chỉ cần biết “đây là một DAO tuân theo hợp đồng CRUD”, không phụ thuộc chi tiết cài đặt.

### 4.3. Đa hình qua override (ghi đè)

- DAO ghi đè các hàm từ `IGenericDAO<T>`.
- `DefaultListCellRenderer` ghi đè `getListCellRendererComponent` để tùy biến hiển thị `DayTro` trong combobox.

---

## 5) Abstraction (Trừu tượng hóa): `Refreshable`, `IGenericDAO<T>`; tách tầng = trừu tượng hóa trách nhiệm

Trừu tượng hóa là **ẩn chi tiết**, chỉ “lộ ra” những gì cần thiết để sử dụng.

### 5.1. Trừu tượng hóa bằng interface

- `Refreshable`: trừu tượng hóa khả năng “làm mới dữ liệu UI” thành 1 hàm `refreshData()`.
- `IGenericDAO<T>`: trừu tượng hóa “một kho dữ liệu CRUD” mà không nói kho đó là MySQL/SQLite hay JDBC/ORM.

### 5.2. Trừu tượng hóa bằng cách tách tầng (separation of concerns)

Ngay cả khi bạn không dùng `abstract class`, việc chia tầng cũng là trừu tượng hóa trách nhiệm:

- UI trừu tượng hóa “màn hình” (chỉ quan tâm hiển thị).
- Controller trừu tượng hóa “nghiệp vụ/điều phối”.
- DAO trừu tượng hóa “lưu trữ và truy vấn dữ liệu”.

Nhờ vậy, mỗi tầng có thể thay đổi độc lập hơn.

---

## 6) Enum type-safe: `TrangThaiPhong`, `TrangThaiHoaDon` tránh sai chuỗi

### Vấn đề nếu dùng String

Nếu trạng thái lưu/điều kiện bằng chuỗi, rất dễ gặp lỗi:

- Sai chính tả: `"DA_THUE"` vs `"DATHUE"`
- Khác format: `"DA THANH TOAN"` vs `"DA_THANH_TOAN"`
- Case-sensitive: `"trong"` vs `"TRONG"`

Những lỗi này thường **chỉ lộ ra khi chạy** (runtime) và khó trace.

### Lợi ích của enum trong dự án

Trong project, enum được định nghĩa rõ ràng:

- `TrangThaiPhong { TRONG, DA_THUE }`
- `TrangThaiHoaDon { CHUA_THANH_TOAN, DA_THANH_TOAN }`

Lợi ích:

- **Type-safe**: biến trạng thái chỉ nhận đúng các giá trị cho phép.
- **Được IDE hỗ trợ**: autocomplete, refactor an toàn, compile-time checking.
- **Dễ dùng trong điều kiện nghiệp vụ**: ví dụ trong `ThongKeController`, lọc phòng trống bằng so sánh enum:
  - `p.getTrangThai() == TrangThaiPhong.TRONG`
  - `h.getTrangThai() == TrangThaiHoaDon.DA_THANH_TOAN`

---

## Gợi ý trình bày ngắn (để thuyết trình)

- **MVC+DAO**: UI không biết SQL; controller điều phối; DAO nói chuyện DB; model dùng chung.
- **Đóng gói**: entity private + getter/setter; JDBC/SQL chỉ nằm trong DAO.
- **Kế thừa**: `JFrame/JPanel` cho UI; renderer/model ẩn danh override hành vi.
- **Đa hình**: gọi chung `refreshData()` nhưng mỗi panel refresh khác; nhiều DAO cùng CRUD contract.
- **Trừu tượng hóa**: interface + tách tầng giúp ẩn chi tiết và phân trách nhiệm.
- **Enum type-safe**: tránh sai chuỗi trạng thái, lọc/so sánh an toàn.
