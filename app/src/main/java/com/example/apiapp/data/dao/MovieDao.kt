package com.example.apiapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.apiapp.data.entity.Movie


@Dao
interface MovieDao {
    @Query("SELECT * FROM movies WHERE page = :page")
    suspend fun getMoviesByPage(page: Int): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Transaction
    @Query("DELETE FROM movies WHERE page NOT IN (:pages)")
    suspend fun deleteMoviesNotInPages(pages: List<Int>)

    @Transaction
    @Query("SELECT * FROM movies")
    suspend fun getCachedMovies(): List<Movie>
}