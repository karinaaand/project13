package com.example.project13.database

// Import yang diperlukan untuk membuat DAO di Room
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

// DAO (Data Access Object) untuk entitas Buku
@Dao
interface BukuDao {

    // Menambahkan data baru ke dalam tabel buku_table.
    // Jika terjadi konflik (misalnya data dengan primary key yang sama), data akan diabaikan.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(buku: Buku)

    // Memperbarui data buku yang ada di dalam tabel.
    @Update
    fun update(buku: Buku)

    // Menghapus data buku dari tabel.
    @Delete
    fun delete(buku: Buku)

    // Mendapatkan semua data buku dari tabel, diurutkan berdasarkan id secara ascending.
    // Data dikembalikan dalam bentuk LiveData, sehingga perubahan data pada database
    // akan otomatis diperbarui pada pengamat.
    @get:Query("SELECT * from buku_table ORDER BY id ASC")
    val allBuku: LiveData<List<Buku>>

    // Memperbarui status penanda buku (bookmark) pada buku tertentu berdasarkan id.
    @Query("UPDATE buku_table SET bookmark = :bookmark WHERE id = :id")
    fun updateBookmarkById(id: Int, bookmark: Boolean)

    // Mendapatkan semua data buku yang memiliki status bookmark TRUE, diurutkan berdasarkan id secara ascending.
    // Data dikembalikan dalam bentuk LiveData.
    @get:Query("SELECT * from buku_table WHERE bookmark = TRUE ORDER BY id ASC")
    val BookmarkBuku: LiveData<List<Buku>>
}
