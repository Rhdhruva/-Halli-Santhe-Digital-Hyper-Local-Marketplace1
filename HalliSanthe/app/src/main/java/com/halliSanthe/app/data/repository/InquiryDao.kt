package com.halliSanthe.app.data.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import com.halliSanthe.app.data.model.Inquiry

@Dao
interface InquiryDao {

    @Query("SELECT * FROM inquiries WHERE productId = :productId ORDER BY timestamp DESC")
    fun getInquiriesForProduct(productId: Long): LiveData<List<Inquiry>>

    @Query("SELECT COUNT(*) FROM inquiries WHERE isRead = 0")
    fun getUnreadCount(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInquiry(inquiry: Inquiry): Long

    @Query("UPDATE inquiries SET isRead = 1 WHERE productId = :productId")
    suspend fun markAllReadForProduct(productId: Long)

    @Delete
    suspend fun deleteInquiry(inquiry: Inquiry)
}
