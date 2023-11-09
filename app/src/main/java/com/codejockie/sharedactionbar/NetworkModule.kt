package com.codejockie.sharedactionbar

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        val DATETIME_FORMAT = "yyyy-MM-dd'T'hh:mm:ss'Z'"
        val formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT)
        val gson = GsonBuilder()
            // LocalDateTime deserializer
            .registerTypeAdapter(
                LocalDateTime::class.java,
                JsonDeserializer { json: JsonElement, _: Type, _: JsonDeserializationContext ->
                    LocalDateTime.parse(json.asJsonPrimitive.asString)
                }
            )
            // LocalDateTime serializer
            .registerTypeAdapter(
                LocalDateTime::class.java,
                JsonSerializer<LocalDateTime?> { ldt, _, _ ->
                    val zdt = ldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC)
                    JsonPrimitive(formatter.format(zdt))
                }
            )
            .create()
        return GsonConverterFactory.create(gson)
    }


    @Provides
    @Singleton
    fun provideRetrofitClient(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("")
            .addConverterFactory(provideGsonConverterFactory())
            .build()
    }
}