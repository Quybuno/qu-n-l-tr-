# Kịch bản thuyết trình 20 phút — Đề tài: Hệ thống quản lý phòng trọ (Java Swing, MVC + DAO)

> Mục tiêu của kịch bản: giúp bạn nói trôi chảy trong 20 phút, đúng trọng tâm môn OOP/MVC/DAO, có chỗ **demo** và **chốt OOP** rõ ràng.  
> Tài liệu tham chiếu trong repo: `docs/DETAI_THIET_KE.md`, `OOP-DAC-DIEM-DU-AN.md`, `NGUOI1_CORE_DB_KHUNG_UNG_DUNG_OOP.md`.

---

## Chuẩn bị trước khi lên trình (2–3 phút, không tính vào 20p nếu có thể)

- **Mở sẵn ứng dụng** ở màn hình chính (`MainFrame`) và DB đã có dữ liệu mẫu.
- **Chuẩn bị dữ liệu demo tối thiểu**:
  - 1 dãy trọ (DayTro) có 2–3 phòng.
  - 1 khách thuê.
  - 1 hợp đồng gắn phòng + khách.
  - 1 kỳ nhập chỉ số điện nước.
  - 1 hóa đơn (có trạng thái chưa thanh toán → thanh toán).
- **Mở sẵn 2 tab quan trọng** để chuyển nhanh: `Phòng`, `Hợp đồng`, `Chỉ số`, `Hóa đơn`, `Thống kê`.
- Nếu được phép: chuẩn bị 1–2 ảnh/screenshot sơ đồ kiến trúc và schema (hoặc chiếu file `.md`).

---

## Dàn ý slide đề xuất (10–12 slide)

1. **Tiêu đề + nhóm + bối cảnh**
2. **Vấn đề & mục tiêu**
3. **Phạm vi chức năng (MVP)**
4. **Công nghệ & lý do chọn**
5. **Kiến trúc tổng quan: MVC + DAO**
6. **Thiết kế dữ liệu (Entity/quan hệ)**
7. **Luồng nghiệp vụ 1: Khách → Hợp đồng → Phòng**
8. **Luồng nghiệp vụ 2: Chỉ số → Hóa đơn → Thanh toán**
9. **Điểm nhấn OOP (4 trụ cột + ví dụ trong code)**
10. **Demo nhanh**
11. **Kết quả đạt được + hạn chế**
12. **Hướng phát triển + Q&A**

---

## Timeline 20 phút (nói theo từng mốc phút)

### 0:00 – 1:00 | Mở bài
**Bạn nói (gợi ý lời thoại):**
> Chào thầy/cô và các bạn, nhóm em xin thuyết trình đề tài **Hệ thống quản lý phòng trọ** chạy trên máy tính.  
> Ứng dụng nhằm số hóa các nghiệp vụ cơ bản như **phòng – khách – hợp đồng – chỉ số điện nước – hóa đơn – thống kê**, triển khai theo **MVC kết hợp DAO** và thể hiện rõ các tính chất **OOP**.

**Mục tiêu đoạn này:** nói 1 phút, rõ “đề tài là gì” + “điểm nhấn kỹ thuật”.

---

### 1:00 – 3:00 | Bối cảnh, vấn đề, mục tiêu
**Bạn nói:**
> Thực tế quản lý phòng trọ dễ sai ở khâu tính tiền và theo dõi công nợ: nhập chỉ số thủ công, tính sai điện nước, thất lạc hợp đồng, khó biết phòng nào chưa thanh toán.  
> Mục tiêu của hệ thống là:
> - Quản lý tập trung dữ liệu phòng, khách, hợp đồng
> - Nhập chỉ số theo kỳ và **tự tính tiêu thụ**
> - Tạo hóa đơn, theo dõi trạng thái thanh toán
> - Có báo cáo nhanh: doanh thu theo tháng, danh sách phòng chưa thanh toán

**Tip:** đừng đi quá sâu; giữ đúng “vì sao làm”.

---

### 3:00 – 5:00 | Phạm vi chức năng (MVP)
**Bạn nói:**
> Trong phạm vi MVP, nhóm em tập trung vào lõi: **Phòng – Khách – Hợp đồng – Hóa đơn**; phần điện nước và thống kê là hỗ trợ trực tiếp cho bài toán tính tiền.

**Gạch đầu dòng nêu nhanh (theo module):**
- **Dãy trọ / Phòng**: thêm/sửa/xóa, trạng thái phòng (trống/đang thuê/bảo trì).
- **Khách & Hợp đồng**: hồ sơ khách, hợp đồng gắn phòng, ngày bắt đầu/kết thúc, tiền cọc.
- **Chỉ số điện nước**: nhập theo tháng, tính mức tiêu thụ.
- **Hóa đơn & Thanh toán**: tiền phòng + điện + nước + phụ phí, trạng thái, ghi nhận thanh toán.
- **Thống kê**: doanh thu theo tháng, danh sách chưa thanh toán.

---

### 5:00 – 7:30 | Công nghệ & kiến trúc tổng quan
**Bạn nói:**
> Về công nghệ, nhóm em dùng **Java (Maven)** và giao diện **Swing** để phù hợp ứng dụng desktop và yêu cầu môn học.  
> Về kiến trúc, nhóm em triển khai theo **MVC + DAO** để tách bạch trách nhiệm: UI chỉ hiển thị, Controller điều phối nghiệp vụ, DAO xử lý truy vấn DB.

**Bạn chiếu sơ đồ (từ `docs/DETAI_THIET_KE.md`):**
> View → Controller → DAO → Database, còn Model/Entity dùng chung cho Controller/DAO.

**Điểm nhấn 1 câu:**
> Lợi ích là dễ mở rộng, dễ bảo trì, và thể hiện rõ trừu tượng hóa của OOP theo tầng.

---

### 7:30 – 10:00 | Thiết kế dữ liệu (CSDL) + mapping Model
**Bạn nói:**
> Dữ liệu được tổ chức theo các thực thể chính: **DayTro, PhongTro, NguoiThue, HopDong, ChiSoDienNuoc, HoaDon**.  
> Các bảng liên kết bằng khóa ngoại để phản ánh đúng nghiệp vụ: hợp đồng gắn phòng và khách; chỉ số gắn theo hợp đồng/kỳ; hóa đơn gắn theo hợp đồng.

**Nêu 2–3 ràng buộc “có ý nghĩa”:**
- “Mã phòng” unique trong cùng một dãy (không trùng phòng trong cùng dãy).
- Quan hệ 1–n: Dãy trọ → nhiều phòng; Khách/Hợp đồng theo thời gian.

**Nếu cần chốt nhanh OOP tại đây:**
> Các entity trong `model` dùng encapsulation: thuộc tính `private`, getter/setter, và có `toString()` cho hiển thị.

---

### 10:00 – 12:30 | Luồng nghiệp vụ 1 (Khách → Hợp đồng → Phòng)
**Bạn nói theo “story”:**
> Luồng cơ bản: tạo khách thuê → tạo hợp đồng gắn với một phòng → phòng chuyển trạng thái sang đang thuê.  
> UI gửi sự kiện sang Controller, Controller validate rồi gọi DAO để thao tác DB, sau đó UI refresh lại bảng.

**Bạn nhấn đúng kiến trúc:**
- UI **không viết SQL**
- Controller **không đụng JDBC trực tiếp**
- DAO **đóng gói JDBC + mapping ResultSet → Entity**

---

### 12:30 – 15:00 | Luồng nghiệp vụ 2 (Chỉ số → Hóa đơn → Thanh toán)
**Bạn nói:**
> Hàng tháng, chủ trọ nhập chỉ số điện nước đầu/kết thúc kỳ. Hệ thống tính chênh lệch tiêu thụ, nhân đơn giá, cộng tiền phòng và phụ phí để tạo hóa đơn.  
> Khi thanh toán, hóa đơn đổi trạng thái để theo dõi công nợ và phục vụ thống kê.

**Nếu có enum trạng thái:**
> Trạng thái hóa đơn/phòng dùng `enum` để type-safe, tránh sai chính tả so với dùng chuỗi.

---

### 15:00 – 17:30 | Điểm nhấn OOP (4 trụ cột) gắn trực tiếp vào project
> Mục tiêu đoạn này: nói “đã áp dụng OOP như thế nào” với **ví dụ cụ thể** trong codebase.

#### 1) Đóng gói (Encapsulation)
**Bạn nói:**
> Các entity `model` đóng gói dữ liệu bằng `private` + getter/setter.  
> DAO đóng gói chi tiết JDBC/SQL; UI chỉ gọi Controller, không biết câu lệnh SQL.

Ví dụ bạn có thể nhắc nhanh:
- `DatabaseUtil.getConnection()` che giấu cấu hình DB.
- `SchemaMigrator.ensureLatest()` che giấu logic migration.

#### 2) Kế thừa (Inheritance)
**Bạn nói:**
> UI kế thừa từ Swing: `MainFrame extends JFrame`, các màn hình `*Panel extends JPanel`.  
> Ngoài ra có kế thừa “tại chỗ” bằng anonymous class để tùy biến renderer hoặc table model.

#### 3) Đa hình (Polymorphism)
**Bạn nói (chọn 1–2 ý nổi bật):**
> Hệ thống có interface `Refreshable`; mỗi panel implement `refreshData()` theo cách riêng. Khi đổi dãy trọ hoặc đổi tab, `MainFrame` chỉ cần gọi `refreshData()` mà không cần biết chi tiết từng panel.  
> Tầng DAO có `IGenericDAO<T>` dùng generics để dùng chung contract CRUD cho nhiều entity.

#### 4) Trừu tượng hóa (Abstraction)
**Bạn nói:**
> Các interface như `IGenericDAO<T>` và `Refreshable` là lớp trừu tượng hóa hành vi. Theo tầng, Controller trừu tượng hóa nghiệp vụ so với UI; DAO trừu tượng hóa lưu trữ so với Controller.

**Chốt 1 câu:**
> Nhờ đó code tách bạch, dễ mở rộng module mới mà ít ảnh hưởng phần còn lại.

---

### 17:30 – 19:15 | Demo nhanh (1–2 phút)
**Kịch bản demo tối thiểu (đủ “wow” nhưng an toàn):**
1. **Chọn dãy trọ** ở combo (nếu có) → các tab tự refresh.
2. Vào **Khách/Hợp đồng**: tạo nhanh hoặc mở bản ghi có sẵn (đừng nhập quá nhiều).
3. Vào **Chỉ số**: mở kỳ gần nhất, cho thấy tiêu thụ.
4. Vào **Hóa đơn**: tạo/hiển thị hóa đơn, đổi trạng thái thanh toán.
5. Vào **Thống kê**: xem doanh thu theo tháng hoặc danh sách chưa thanh toán.

**Bạn nói trong demo (ngắn):**
> Em đổi dãy trọ, các màn hình tự cập nhật nhờ `Refreshable` và state dùng `DayTroContext`.  
> Mỗi thao tác UI đều đi qua Controller và DAO, đảm bảo đúng phân tầng.

---

### 19:15 – 20:00 | Kết luận + hướng phát triển + mời Q&A
**Bạn nói:**
> Tóm lại, nhóm em đã xây dựng ứng dụng quản lý phòng trọ theo MVC + DAO, triển khai đầy đủ luồng lõi và thể hiện rõ 4 trụ cột OOP.  
> Hướng phát triển: phân quyền đăng nhập đầy đủ hơn, xuất hóa đơn PDF, nhắc nợ tự động, và cải thiện báo cáo.  
> Nhóm em xin cảm ơn và sẵn sàng trả lời câu hỏi.

---

## “Bài nói” rút gọn (để bạn học thuộc nhanh)

> Đề tài quản lý phòng trọ nhằm số hóa phòng – khách – hợp đồng – điện nước – hóa đơn.  
> App desktop Java Swing, kiến trúc MVC + DAO: View nhận tương tác, Controller điều phối và validate, DAO đóng gói JDBC/SQL, Model là entity.  
> Luồng 1: khách → hợp đồng → phòng đổi trạng thái. Luồng 2: nhập chỉ số → tính tiền → tạo hóa đơn → thanh toán → thống kê.  
> OOP: đóng gói (entity/DAO), kế thừa (JFrame/JPanel), đa hình (`Refreshable`, `IGenericDAO<T>`), trừu tượng hóa (interface + phân tầng).  
> Demo nhanh: đổi dãy trọ refresh toàn bộ, xem hóa đơn và trạng thái. Kết thúc và Q&A.

---

## Dự phòng câu hỏi (Q&A) và cách trả lời ngắn

- **Vì sao chọn MVC + DAO thay vì viết hết trong UI?**  
  Vì tách trách nhiệm, dễ bảo trì/mở rộng, UI không dính SQL, thay DB/logic dễ hơn.

- **DAO và Controller khác nhau thế nào?**  
  DAO lo truy vấn + mapping dữ liệu; Controller lo luồng nghiệp vụ + validate + gọi DAO.

- **Điểm OOP rõ nhất của nhóm là gì?**  
  `Refreshable` (đa hình runtime) và `IGenericDAO<T>` (trừu tượng hóa + generics) + đóng gói JDBC trong DAO.

- **Nếu mở rộng lên web/app mobile thì thay đổi gì?**  
  Tầng UI thay đổi (View), còn Controller/Service/DAO có thể tái sử dụng một phần (tùy kiến trúc).

---

## Checklist “chống lố thời gian”

- **Đúng 20p**: giữ phần OOP 2.5 phút, demo 1.5 phút.
- **Không kể quá chi tiết code**: chỉ nêu class/package tiêu biểu.
- **Luôn gắn với nghiệp vụ**: phòng/khách/hợp đồng/hóa đơn (đừng nói kiến trúc suông).

