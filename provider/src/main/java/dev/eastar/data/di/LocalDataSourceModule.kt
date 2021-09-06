package dev.eastar.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.eastar.data.provider.CheatDao
import dev.eastar.data.provider.CheatDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CheatDatabaseModule {

    @Singleton
    @Provides
    fun provideDaoSource(
        database: CheatDatabase // need if use room lib
    ): CheatDao {
        return database.dao()
    }

    @Singleton
    @Provides
    fun provideCheatDatabase(
        @ApplicationContext context: Context
    ): CheatDatabase =
        Room.inMemoryDatabaseBuilder(context, CheatDatabase::class.java).build()
    // Room.databaseBuilder(context!!, CheatDatabase::class.java, "CHEAT_DATABASE").fallbackToDestructiveMigration().build()
}
