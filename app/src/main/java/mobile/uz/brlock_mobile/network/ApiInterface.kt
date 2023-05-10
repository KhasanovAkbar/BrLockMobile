package mobile.uz.brlock_mobile.network

import io.reactivex.Single
import mobile.uz.brlock_mobile.domain.*
import mobile.uz.brlock_mobile.domain.edit.EditRequest
import mobile.uz.brlock_mobile.domain.edit.EditResponse
import mobile.uz.brlock_mobile.domain.getAll.AllUsers
import mobile.uz.brlock_mobile.domain.login.LoginRequest
import mobile.uz.brlock_mobile.domain.login.LoginResponse
import mobile.uz.brlock_mobile.domain.register.RegisterRequest
import mobile.uz.brlock_mobile.domain.register.RegisterResponse
import mobile.uz.brlock_mobile.domain.search.SearchUser
import retrofit2.http.*

interface ApiInterface {

    @POST("auth/login")
    fun login(@Body body: LoginRequest): Single<LoginResponse>

    @POST("user/register")
    fun register(@Body body: RegisterRequest): Single<RegisterResponse>


    @GET("user/logout")
    fun logout(): Single<MessageResp>
/*
    @POST("auth/forget_password")
    fun forgetPassword(): Single<MessageResp>
*/

    @POST("user/search")
    fun searchByPhoneNumber(@Body phoneNumber: String): Single<SearchUser>

    @GET("user/users")
    fun getAll(): Single<List<List<AllUsers>>>

    @PUT("user/edit")
    fun editUser(@Body body: EditRequest): Single<EditResponse>

    /* @DELETE("user/id/{sequence}")
     fun delete(@Field("sequence") sequence: String): Single<>*/

}

data class MessageResp(val message: String)

data class ErrorResp(val message: String, val error: RegisterResponse? = null)

data class SuccessResp(val success: Boolean)



