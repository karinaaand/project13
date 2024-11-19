package com.example.project13.database

// Import yang diperlukan untuk anotasi Room Database
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Definisi entitas (table) di database Room
@Entity(tableName = "buku_table") // Memberikan nama tabel "buku_table" untuk entitas ini.
data class Buku(
    @PrimaryKey(autoGenerate = true) // Menandai kolom "id" sebagai primary key dan nilainya akan digenerasikan secara otomatis.
    @NonNull // Menentukan bahwa nilai "id" tidak boleh null.
    val id: Int = 0, // Primary key tabel, default-nya adalah 0 jika tidak diatur.

    @ColumnInfo(name = "title") // Menentukan nama kolom di tabel sebagai "title".
    val title: String, // Kolom ini menyimpan judul buku.

    @ColumnInfo(name = "bookmark") // Menentukan nama kolom di tabel sebagai "bookmark".
    val bookmark: Boolean, // Kolom ini menyimpan status penanda buku (true atau false).

)
