package com.malikrafsan.restaurant_mobile_app.di

import android.app.Application
import androidx.room.Room
import com.malikrafsan.restaurant_mobile_app.database.RestoDB
import com.malikrafsan.restaurant_mobile_app.repository.CartRepository
import com.malikrafsan.restaurant_mobile_app.repository.CartRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRestoDB(app: Application): RestoDB {
        return Room.databaseBuilder(
            app,
            RestoDB::class.java,
            "resto_db",
        ).build()
    }

    @Provides
    @Singleton
    fun provideCartRepository(db: RestoDB): CartRepository{
        return CartRepositoryImpl(db.cartDAO)
    }
}