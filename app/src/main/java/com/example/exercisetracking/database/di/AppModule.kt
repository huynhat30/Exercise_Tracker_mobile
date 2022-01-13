package com.example.exercisetracking.database.di

import android.content.Context
import androidx.room.Room
import com.example.exercisetracking.database.ApplicationDBModel
import com.example.exercisetracking.Contants.ACTIVITY_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        ApplicationDBModel::class.java,
        ACTIVITY_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideRunDao(db: ApplicationDBModel) = db.getDataRecordDAO()
}