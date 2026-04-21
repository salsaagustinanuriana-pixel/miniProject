# Dokumen Spesifikasi Proyek: Sistem Mutasi Stok Barang

**Tujuan Proyek:**
Membangun RESTful API menggunakan Spring Boot untuk mengelola stok barang di berbagai lokasi gudang. Sistem harus menjamin integritas data saat terjadi perpindahan barang antar gudang dan mencegah duplikasi transaksi saat diakses secara bersamaan.

---

## Bagian 1: Desain Database & Entitas
Peserta diwajibkan mengimplementasikan 5 entitas menggunakan anotasi Lombok untuk menghindari *boilerplate code*.

1.  **`Category` (Kategori Barang)**
    * `id` (Long, Primary Key, Auto Increment)
    * `name` (String)
2.  **`Product` (Data Master Barang)**
    * `id` (Long, Primary Key)
    * `is_active` (Boolean)
    * `sku_code` (String, Unique)
    * `name` (String)
    * `price` (Double)
    * `category_id` (Relasi Many-to-One ke `Category`)
    * **`version` (Long, gunakan anotasi `@Version` untuk *Optimistic Locking*)**
3.  **`Warehouse` (Data Master Gudang)**
    * `id` (Long, Primary Key)
    * `code` (String, Unique)
    * `name` (String)
4.  **`WarehouseStock` (Tabel Perantara Stok Fisik)**
    * `id` (Long, Primary Key)
    * `product_id` (Relasi Many-to-One ke `Product`)
    * `warehouse_id` (Relasi Many-to-One ke `Warehouse`)
    * `stock` (Integer)
    * **`version` (Long, gunakan anotasi `@Version` untuk *Optimistic Locking*)**
5.  **`StockMutation` (Riwayat Pergerakan Barang)**
    * `id` (Long, Primary Key)
    * `product_id` (Relasi Many-to-One ke `Product`)
    * `from_warehouse_id` (Relasi Many-to-One ke `Warehouse`, bisa *Null*)
    * `to_warehouse_id` (Relasi Many-to-One ke `Warehouse`, bisa *Null*)
    * `quantity` (Integer)
    * `type` (String: "IN" dan "TRANSFER")
    * `timestamp` (LocalDateTime)

---

## Bagian 2: Standarisasi API & Validasi Input
Semua *endpoint* yang dibangun harus mengikuti aturan keamanan dan format berikut:

* **Format Response Universal (`WebResponse<T>`)**
  Semua balikan API baik saat sukses maupun gagal harus memiliki struktur:
    ```json
    {
      "status": "Success/Fail/Error",
      "message": "Pesan deskriptif...",
      "data": { ... } // Berisi object atau null
    }
    ```
* **Validasi Input (`@Valid`)**
    * Saat menambah produk, harga (`price`) minimal bernilai 1.000.
    * Saat melakukan mutasi, jumlah (`quantity`) tidak boleh 0 atau negatif.
    * Kode SKU tidak boleh kosong (`@NotBlank`).
* **Global Exception Handling (`@RestControllerAdvice`)**
    * Jika data tidak ditemukan kembalikan response message bahwa data yang di cari itu tidak ditemukan dengan menyertakan nama datanya apa misal product dll.
    * Jika validasi input gagal kembalikas response data validasi nya karena apa

---

## Bagian 3: Fitur & Logika Bisnis Utama
Sistem harus memiliki *endpoint* dengan fungsionalitas berikut:

### Fitur A: Master Data Management (CRUD Dasar)
* **POST /api/products** : Menambahkan produk baru.
* **PUT /api/products/{id}** : Update produk baru.
* **POST /api/warehouses** : Menambahkan gudang baru.
* **POST /api/stock/in** : Menambah stok awal barang ke gudang tertentu (Insert ke `WarehouseStock` dan catat log `StockMutation` tipe "IN").
* **PUT /api/products/restore/{id}** : Mengubah kolom `is_active` menjadi `true`.
* **PUT /api/products/delete/{id}** : Peserta tidak boleh menggunakan `repository.deleteById()`. Mereka harus mengimplementasikan mekanisme *Soft Delete* (misalnya mengubah kolom `is_active` menjadi `false`).
* **Aturan Bisnis:** Produk yang sudah di-*soft delete* tidak boleh muncul dalam daftar pencarian dan **tidak boleh** bisa ditransfer ke gudang manapun.

### Fitur B: Pencarian Lanjutan (Advanced Query / JPQL)
Menguji kemampuan ekstraksi data dari beberapa tabel yang berelasi menggunakan JPQL atau Native Query.
* **GET /api/products/low-stock** : Menampilkan daftar produk beserta lokasi gudangnya yang stoknya sudah berada di bawah 10 unit (*Warning System*).
* **GET /api/products/{sku}/stock-summary** : Menjumlahkan total seluruh stok dari satu produk di *semua* gudang.
* **Aturan Bisnis:** Produk yang sudah di-*soft delete* tidak boleh muncul dalam daftar pencarian

### Fitur C: Transaksi Mutasi Antar Gudang (The Core Challenge)
Ini adalah ujian krusial untuk integritas data.
* **POST /api/stock/transfer**
    * Menerima JSON *request body*: `sku_code`, `from_warehouse_id`, `to_warehouse_id`, `quantity`.
* **Aturan Bisnis (Wajib menggunakan `@Transactional`):**
    1. Sistem memastikan produk sedang aktif (tidak di-*soft delete*).
    2. Cari `WarehouseStock` di gudang asal. Jika stok kurang dari `quantity`, lemparkan *exception* (seluruh proses batal).
    3. Kurangi stok di gudang asal.
    4. Cari `WarehouseStock` di gudang tujuan. Tambahkan stoknya jika ada, atau buat entri baru jika belum pernah ada barang tersebut di gudang tujuan.
    5. Catat aktivitas ke `StockMutation` dengan tipe "TRANSFER".

---