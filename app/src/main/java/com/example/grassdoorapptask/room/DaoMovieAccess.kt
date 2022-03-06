package com.example.grassdoorapptask.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grassdoorapptask.model.Result

@Dao
interface DaoMovieAccess {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Result)

    @Query("Select * from Result order by id ASC")
    fun getAllMovie(): List<Result>

    @Query("Select * from Result WHERE id=:id")
    fun getMovieByID(id:Int?): Result

    @Query("Select * from Result WHERE title=:titleVal")
    fun getMovieByTitle(titleVal:String?): Result

    @Query("SELECT COUNT(id) FROM Result")
    fun getMovieCount(): Int
}