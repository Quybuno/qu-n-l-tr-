## Nguoi 1 — Core + DB + Khung ung dung (OOP)

Phu trach:
- `Main.java`
- `ui/MainFrame.java`
- `context/DayTroContext.java`
- `ui/UiUtils.java`
- `ui/Refreshable.java`
- `utils/*`
- `db/SchemaMigrator.java`
- `resources/db/*` (`schema.sql`, `migration_v2_chi_so.sql`)
- `resources/application.properties`

Vai tro chinh:
- Ket noi DB (doc config, tao `Connection`).
- Migration/schema (tu dong dam bao CSDL o dung phien ban).
- Khoi dong ung dung, look & feel.
- Combo “Day tro dang lam viec” + luu trang thai lua chon.
- Refresh du lieu toan cua so khi chuyen tab / doi day.

---

## 1) Dong goi (Encapsulation)

### 1.1. `utils/DatabaseUtil.java` — dong goi cau hinh + ket noi
- An chi tiet doc file cau hinh bang `Properties` trong **khoi `static`**.
- Export mot API cong khai duy nhat `getConnection()`; cac lop khac (DAO/Migrator) **khong can biet** file o dau, doc the nao.
- `PROPS` la `private static final`, constructor `private` -> ep dung theo dang utility class.

Y nghia OOP:
- Trang thai (properties) va co che nap config bi “giam sat” trong 1 noi; de doi DB/url/username khong phai sua nhieu noi.

### 1.2. `db/SchemaMigrator.java` — dong goi logic migration
- An toan bo quy trinh cap nhat schema trong `ensureLatest()`.
- Su dung cac ham `private static` nho: `addColumnIgnoreDuplicate`, `createDayTroTableIfNotExists`, `addCompositeUniqueIfNeeded`, ...
- `DUPLICATE_COLUMN` la hang so `private static final` (thong tin DB-specific), khong “ro ri” ra ngoai.
- Lop `final` + constructor `private`: khong cho khoi tao doi tuong migrator.

Y nghia OOP:
- Migraion la 1 “doi tuong nhiem vu” (task object) duoc dong goi; cac lop khac chi biet goi 1 ham `ensureLatest()`.

### 1.3. `context/DayTroContext.java` — dong goi trang thai ung dung
- Dong goi “day tro dang chon” bang bien `private static volatile selectedDayTroId` + `get/set`.
- `volatile`: dam bao luong Swing event thread thay doi thay duoc ngay (tinh nhat quan tri nho) neu co nhieu thread (duong dai, DB call, v.v.).
- Lop `final` + constructor `private`: dam bao chi dung nhu 1 “context” chung.

Y nghia OOP:
- Thay vi truyen tham so “selectedDayTroId” qua tat ca panel/dao, trang thai duoc gom ve 1 noi.

### 1.4. `ui/UiUtils.java` — dong goi hanh vi UI lap lai
- `error(...)` va `info(...)` dong goi `JOptionPane` voi tieu de/icon thong nhat.
- `refreshWhenPanelShown(...)` dong goi co che `HierarchyListener` (SHOWING_CHANGED) -> caller chi truyen `Runnable`.
- Lop `final` + constructor `private`: utility class dung chung.

---

## 2) Ke thua (Inheritance)

### 2.1. `ui/MainFrame.java` ke thua `JFrame`
- `public class MainFrame extends JFrame` tai su dung toan bo co che window lifecycle cua Swing.
- `MainFrame.applyLookAndFeel()` la mot hook khoi tao giao dien (ke thua he thong UIManager).

### 2.2. Ke thua “tai cho” bang lop vo danh (anonymous class)
- `cbDay.setRenderer(new DefaultListCellRenderer() { ... })`:
  - Ke thua `DefaultListCellRenderer` va **ghi de** `getListCellRendererComponent` de hien thi `DayTro` theo dinh dang `TenDay (MaDay)`.

Y nghia OOP:
- Dung ke thua de “tuy bien hanh vi” ma khong can tao file class rieng.

---

## 3) Da hinh (Polymorphism)

### 3.1. Da hinh qua interface `Refreshable`
- `ui/Refreshable.java` dinh nghia hop dong: `void refreshData()`.
- Cac panel (Phong/HopDong/ChiSo/HoaDon/ThongKe/DayTro/Khach...) implement `Refreshable`.
- `MainFrame.refreshAllPanels()` goi `refreshData()` len nhieu panel -> **cung loi goi, hanh vi khac nhau** tuy panel.

Y nghia OOP:
- `MainFrame` khong can biet chi tiet refresh tung tab (SQL nao, bang nao), chi can biet hop dong.

### 3.2. Da hinh qua ghi de (`@Override`)
- Renderer combo va `DefaultTableModel` (o cac panel) deu ghi de de thay doi hanh vi mac dinh.

### 3.3. Da hinh qua `instanceof` pattern matching
- Trong renderer: `if (value instanceof DayTro d) { ... }` -> xu ly dung kieu thuc te, tranh ep kieu nguy hiem.

---

## 4) Truu tuong hoa (Abstraction)

### 4.1. Truu tuong hoa DB config
- `application.properties` truu tuong hoa thong tin ket noi (URL/user/password).
- `DatabaseUtil` la lop truu tuong hoa “cach lay Connection” (code khac chi goi `getConnection()`).

Luu y bao mat:
- File nay chua `db.password`. Khi viet bao cao/MD, nen **che gia tri** (VD: `db.password=******`) khi screenshot/commit.

### 4.2. Truu tuong hoa migration
- `SchemaMigrator.ensureLatest()` la cap truu tuong: “dam bao DB da o dung schema”.
- Chi tiet SQL (ALTER/CREATE/INDEX/FK) duoc dong goi trong migrator.

### 4.3. Truu tuong hoa refresh theo “y dinh”
- `Refreshable` truu tuong hoa y dinh “tab/panel co the tu cap nhat khi hien thi/doi context”.
- `UiUtils.refreshWhenPanelShown(panel, onRefresh)` truu tuong hoa event UI “panel bat dau showing”.

---

## 5) Composition root + Dependency Injection thu cong (Core app)

### 5.1. `Main.java` la noi lap rap doi tuong (composition root)
- Trinh tu khoi dong:
  1. `SchemaMigrator.ensureLatest()` (dam bao DB dung schema truoc khi thao tac).
  2. Tao cac DAO.
  3. Tao cac Controller bang cach truyen DAO vao constructor.
  4. Tao `MainFrame` bang cach truyen cac Controller vao.
  5. `SwingUtilities.invokeLater(...)` dam bao UI khoi tao tren EDT.

Y nghia OOP/thiet ke:
- Phu thuoc duoc “day tu ngoai vao” (constructor injection thu cong):
  - Controller khong tu tao DAO.
  - UI khong tu tao Controller.
- De test/moc sau nay: co the thay DAO/Controller bang ban khac (stub/mock) ma khong doi code UI.

---

## 6) Combo “Day tro dang lam viec” + refresh toan cua so

### 6.1. Combo `cbDay` trong `MainFrame`
- Du lieu combo lay tu `dayTroController.danhSachDayTro()`.
- Khi user chon day:
  - Luu `DayTroContext.setSelectedDayTroId(d.getId())`.
  - Goi `refreshAllPanels()` -> moi panel tu nap du lieu theo context.

### 6.2. Giu lua chon cu (state restore)
- `reloadDayComboAndRefresh()`:
  - Doc `prev = DayTroContext.getSelectedDayTroId()`.
  - Neu `prev` con ton tai trong list moi -> chon lai.
  - Neu khong, chon phan tu dau (neu co) hoac set null.

Y nghia OOP:
- `DayTroContext` dong goi state, `MainFrame` chi dong vai tro “dieu phoi view + state”, tung panel dong vai tro “tu cap nhat”.

---

## 7) resources/db/* va su lien ket voi code

### 7.1. `resources/db/schema.sql`
- Dinh nghia toan bo schema “moi”: cac bang `day_tro`, `phong_tro`, `nguoi_thue`, `hop_dong`, `chi_so_dien_nuoc`, `hoa_don`.
- Rang buoc OOP-du-lieu:
  - FK the hien quan he giua thuc the (PhongTro -> DayTro, HopDong -> PhongTro/NguoiThue, ChiSo -> HopDong...).
  - Unique key (VD `uk_day_ma_phong`) phu hop y nghia “ma phong unique trong cung 1 day”.

### 7.2. `resources/db/migration_v2_chi_so.sql`
- Script nang cap cho DB cu (them don gia, tao bang chi so).
- Trong app, `SchemaMigrator` thuc hien tuong tu (ALTER/CREATE) de user khong phai chay tay.

---

## 8) utils/* (tong hop vai tro OOP)

- `AppConstant`: hang so dung chung (dong goi gia tri mac dinh).
- `DateUtils`: dong goi dinh dang/parse ngay (tranh lap code + tap trung quy uoc).
- `ValidationUtils`: dong goi luat regex (CMND/SDT) -> controller/goi bat ky dung chung.
- `DatabaseUtil`: dong goi ket noi DB.

Tat ca theo phong cach **utility class**: `final` + constructor `private` + phuong thuc `static`.

---

## 9) Ket luan (OOP duoc the hien o phan Nguoi 1)

- **Dong goi**: an config DB, an migration, an state day tro, an hanh vi UI lap lai.
- **Ke thua**: `MainFrame extends JFrame`, anonymous renderer ke thua `DefaultListCellRenderer`.
- **Da hinh**: interface `Refreshable` giup refresh toan cua so ma khong phu thuoc vao tung panel.
- **Truu tuong hoa**: hop dong interface + tang trach nhiem (Core -> UI, DB util, Migrator), giam phu thuoc truc tiep.

