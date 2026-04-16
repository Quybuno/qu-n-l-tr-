# Kịch bản **demo** 20 phút trước toàn lớp  
**Project**: Hệ thống quản lý phòng trọ (Java Swing, MVC + DAO, OOP)  

> Mục tiêu: trong **20 phút** bạn vừa **chạy demo trơn tru**, vừa “chốt” được **các chức năng chính** và **tính OOP** (4 trụ cột + tách tầng MVC/DAO).  
> Tài liệu tham chiếu trong repo: `docs/DETAI_THIET_KE.md`, `OOP-DAC-DIEM-DU-AN.md`, `docs/KICH_BAN_THUYET_TRINH_20P.md`.

---

## Chuẩn bị trước giờ lên trình (làm sẵn để demo không vỡ)

- **Dữ liệu mẫu tối thiểu (khuyến nghị)**:
  - 1 **Dãy trọ** có 3 phòng: 1 trống, 1 đang thuê, 1 bảo trì (nếu có).
  - 1 **Khách**.
  - 1 **Hợp đồng** gắn khách + phòng đang thuê.
  - 1 kỳ **Chỉ số điện nước** (tháng gần nhất) cho hợp đồng đó.
  - 1 **Hóa đơn** trạng thái *Chưa thanh toán* (để demo chuyển trạng thái).
- **Mở sẵn app** ở `MainFrame`, đã kết nối DB ok.
- **Chuẩn bị “2 cửa sổ”** (nếu trình chiếu cho phép):
  - Cửa sổ 1: chạy ứng dụng (demo).
  - Cửa sổ 2: mở nhanh code/package để chỉ ra OOP (không cần đọc code dài).

---

## Bản đồ nhanh để “nói trúng OOP”

Bạn chỉ cần nhớ 6 điểm chốt sau (nói ngắn nhưng đúng trọng tâm):

- **MVC + DAO**: `ui` (View) → `controller` → `dao` → DB, `model` dùng chung.
- **Encapsulation (đóng gói)**: entity `model` dùng `private` + getter/setter; JDBC/SQL được **đóng trong DAO**.
- **Inheritance (kế thừa)**: `MainFrame extends JFrame`, các `*Panel extends JPanel`.
- **Polymorphism (đa hình)**:
  - `Refreshable.refreshData()` — mỗi panel implement khác nhau, nhưng gọi chung.
  - `IGenericDAO<T>` — nhiều DAO implement cùng “hợp đồng CRUD”.
- **Abstraction (trừu tượng hóa)**: interface `Refreshable`, `IGenericDAO<T>`; “tách tầng” cũng là trừu tượng hóa trách nhiệm.
- **Enum type-safe**: `TrangThaiPhong`, `TrangThaiHoaDon` tránh sai chuỗi.

---

## Timeline demo 20 phút (có lời thoại + thao tác)

### 0:00 – 1:00 | Mở bài (đi thẳng vào demo)
**Bạn nói:**
> Chào thầy/cô và các bạn, nhóm em demo nhanh hệ thống **quản lý phòng trọ**: phòng – khách – hợp đồng – chỉ số điện nước – hóa đơn – thống kê.  
> Ứng dụng desktop Java Swing, thiết kế theo **MVC + DAO** và thể hiện rõ các tính chất **OOP**.

**Bạn làm:** mở màn hình chính `MainFrame`, chỉ nhanh các tab/module.

---

### 1:00 – 3:00 | “Tour” cấu trúc module (để cả lớp hiểu mình sắp demo gì)
**Bạn nói (30–45s):**
> Em demo theo đúng luồng nghiệp vụ thật: **(1) Dãy/Phòng → (2) Khách & Hợp đồng → (3) Chỉ số → (4) Hóa đơn & thanh toán → (5) Thống kê**.

**Bạn làm:** click nhanh từng tab (không thao tác sâu).

**Chốt OOP 1 câu:**
> UI chỉ hiển thị, không viết SQL; mọi thao tác đi qua Controller/DAO.

---

### 3:00 – 6:00 | Demo 1: Dãy trọ / Phòng (trạng thái + lọc dữ liệu)
**Bạn làm (mục tiêu: “nhìn phát hiểu ngay”):**
- Vào **Dãy trọ** (hoặc khu vực chọn dãy): chọn 1 dãy.
- Vào **Phòng**: cho thấy danh sách phòng + **trạng thái** (trống/đang thuê/bảo trì).
- Nếu có chức năng: thêm/sửa/xóa nhanh 1 phòng (nhập tối thiểu, tránh dài).

**Bạn nói (đúng trọng tâm):**
> Khi em đổi dãy trọ, các màn hình liên quan tự cập nhật theo state chung.

**Chốt OOP (10–15s, không mở code dài):**
- **Enum**: trạng thái phòng dùng `TrangThaiPhong` (type-safe).
- **Kế thừa**: các màn hình đều là `JPanel` (tái sử dụng UI).

---

### 6:00 – 10:00 | Demo 2: Khách → Hợp đồng → Phòng đổi trạng thái (luồng “xương sống”)
**Bạn làm:**
- Vào **Khách**:
  - Tạo nhanh 1 khách (hoặc mở bản ghi có sẵn).
  - Cho thấy validate cơ bản (nếu có): thiếu thông tin thì báo lỗi.
- Vào **Hợp đồng**:
  - Tạo hợp đồng gắn **khách + phòng trống**.
  - Lưu xong quay lại **Phòng** để thấy phòng đó đổi sang “đang thuê” (nếu hệ thống cập nhật trạng thái).

**Bạn nói:**
> Luồng này thể hiện rõ phân tầng: UI phát sinh sự kiện → Controller validate → DAO ghi DB → UI refresh lại bảng.

**Chốt OOP (20–30s):**
- **Đóng gói**: UI không biết chi tiết DB/JDBC; DAO lo mapping dữ liệu ↔ entity.
- **Composition (has-a)**: Controller “giữ” DAO để làm việc (không kế thừa DAO).

---

### 10:00 – 14:00 | Demo 3: Chỉ số điện nước (tính tiêu thụ) → tạo dữ liệu tính tiền
**Bạn làm:**
- Vào **Chỉ số**:
  - Chọn hợp đồng/phòng đang thuê.
  - Nhập chỉ số đầu/kết thúc (hoặc mở kỳ đã có).
  - Cho thấy hệ thống tính **tiêu thụ = cuối − đầu** (và/hoặc tiền điện nước).

**Bạn nói (ngắn):**
> Đây là đoạn giảm sai sót: hệ thống tự tính tiêu thụ và chuẩn hóa dữ liệu đầu vào cho hóa đơn.

**Chốt OOP 1 câu:**
> `model` đại diện thực thể nghiệp vụ; Controller kiểm soát logic/validate; DAO lo truy xuất.

---

### 14:00 – 17:30 | Demo 4: Hóa đơn → thanh toán → trạng thái
**Bạn làm:**
- Vào **Hóa đơn**:
  - Tạo hóa đơn từ kỳ chỉ số (hoặc mở hóa đơn đã có).
  - Chỉ rõ các thành phần: tiền phòng + điện + nước + phụ phí (nếu có).
  - Bấm **Thanh toán** (hoặc đổi trạng thái) để chuyển từ *Chưa thanh toán* → *Đã thanh toán*.

**Bạn nói (đúng nghiệp vụ):**
> Điểm quan trọng là theo dõi công nợ bằng trạng thái hóa đơn, phục vụ thống kê.

**Chốt OOP (20s):**
- **Enum**: `TrangThaiHoaDon` tránh sai chính tả trạng thái.
- **Đa hình qua interface**: nhiều module cùng “cách gọi refresh” nhưng implement khác nhau (nếu có `Refreshable`).

---

### 17:30 – 19:00 | Demo 5: Thống kê (kết quả nhìn thấy ngay)
**Bạn làm:**
- Vào **Thống kê**:
  - Xem doanh thu theo tháng (hoặc danh sách phòng/hóa đơn chưa thanh toán).
  - Nếu vừa thanh toán, chỉ ra số liệu/thống kê thay đổi (nếu UI cập nhật).

**Bạn nói:**
> Thống kê lấy dữ liệu từ DB thông qua DAO, nên phản ánh đúng trạng thái mới sau thanh toán.

---

### 19:00 – 20:00 | Chốt OOP + kết luận (đừng kéo dài)
**Bạn nói (mẫu 40–50s):**
> Tóm lại, nhóm em đã triển khai app quản lý phòng trọ theo **MVC + DAO**: View chỉ hiển thị, Controller điều phối nghiệp vụ, DAO đóng gói JDBC/SQL, Model là entity.  
> OOP thể hiện qua: **đóng gói** (entity/DAO), **kế thừa** (JFrame/JPanel), **đa hình** (`Refreshable`, `IGenericDAO<T>`), **trừu tượng hóa** (interface + phân tầng), cộng thêm **enum** để type-safe.  
> Nhóm em xin cảm ơn và sẵn sàng Q&A.

---

## “Phím tắt” xử lý sự cố khi demo

- **Nếu DB trống / dữ liệu lỗi**: chuyển sang “mở bản ghi có sẵn” (chuẩn bị trước), tránh tạo mới quá nhiều.
- **Nếu chức năng A gặp lỗi**: bỏ qua ngay, nhảy sang tab khác theo kịch bản (ưu tiên: Phòng → Hợp đồng → Hóa đơn → Thống kê).
- **Nếu app chậm**: nói 1 câu “đang truy vấn qua DAO” và chuyển sang giải thích OOP/MVC 15–20s.

---

## Gợi ý Q&A (trả lời 1–2 câu)

- **Vì sao không viết SQL trong UI?**  
  Vì tách trách nhiệm: UI chỉ hiển thị; DAO quản lý truy cập DB giúp dễ bảo trì/mở rộng.

- **DAO khác Controller chỗ nào?**  
  Controller lo luồng nghiệp vụ + validate; DAO lo truy vấn + mapping dữ liệu.

- **OOP rõ nhất của dự án là gì?**  
  Interface `Refreshable`/`IGenericDAO<T>` (đa hình + trừu tượng hóa) và đóng gói JDBC trong DAO.

